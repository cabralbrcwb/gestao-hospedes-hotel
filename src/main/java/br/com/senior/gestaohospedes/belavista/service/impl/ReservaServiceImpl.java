package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.dto.CheckoutResponseDTO;
import br.com.senior.gestaohospedes.belavista.dto.ReservaRequestDTO;
import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.exception.CheckInForaDoHorarioException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeNaoEncontradoException;
import br.com.senior.gestaohospedes.belavista.exception.ReservaNaoEncontradaException;
import br.com.senior.gestaohospedes.belavista.exception.ReservaStatusException;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import br.com.senior.gestaohospedes.belavista.repository.ReservaRepository;
import br.com.senior.gestaohospedes.belavista.service.ReservaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReservaServiceImpl implements ReservaService {

    private static final BigDecimal DIARIA_SEMANA = new BigDecimal("120.00");
    private static final BigDecimal DIARIA_FIM_DE_SEMANA = new BigDecimal("180.00");
    private static final BigDecimal ESTACIONAMENTO_SEMANA = new BigDecimal("15.00");
    private static final BigDecimal ESTACIONAMENTO_FIM_DE_SEMANA = new BigDecimal("20.00");
    private static final LocalTime HORA_CHECKIN = LocalTime.of(14, 0);
    private static final LocalTime HORA_CHECKOUT = LocalTime.of(12, 0);

    private final ReservaRepository reservaRepository;
    private final HospedeRepository hospedeRepository;
    private final Clock clock;

    public ReservaServiceImpl(ReservaRepository reservaRepository, HospedeRepository hospedeRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.hospedeRepository = hospedeRepository;
        this.clock = clock;
    }

    @Override
    public Reserva criarReserva(ReservaRequestDTO dto) {
        log.debug("Iniciando processo para criar nova reserva para o hóspede ID: {}", dto.getIdHospede());
        Hospede hospede = hospedeRepository.findById(dto.getIdHospede())
                .orElseThrow(() -> {
                    log.warn("Hóspede não encontrado para o ID: {}", dto.getIdHospede());
                    return new HospedeNaoEncontradoException(dto.getIdHospede());
                });

        Reserva reserva = new Reserva();
        reserva.setHospede(hospede);
        reserva.setDataEntrada(dto.getDataEntrada());
        reserva.setDataSaida(dto.getDataSaidaPrevista());
        reserva.setAdicionalVeiculo(dto.isAdicionalVeiculo());
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(BigDecimal.ZERO);

        log.debug("Salvando nova reserva no banco de dados: {}", reserva);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva realizarCheckIn(Long idReserva) {
        log.debug("Iniciando processo de check-in para a reserva ID: {}", idReserva);
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    log.warn("Reserva não encontrada para o ID: {}", idReserva);
                    return new ReservaNaoEncontradaException(idReserva);
                });

        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            log.warn("Tentativa de check-in inválida. Status da reserva é {} mas deveria ser PENDENTE.", reserva.getStatus());
            throw new ReservaStatusException(StatusReserva.PENDENTE, reserva.getStatus());
        }

        LocalDateTime agora = LocalDateTime.now(clock);
        if (agora.toLocalTime().isBefore(HORA_CHECKIN)) {
            log.warn("Tentativa de check-in fora do horário. Horário atual: {}, Horário permitido: {}", agora.toLocalTime(), HORA_CHECKIN);
            throw new CheckInForaDoHorarioException(HORA_CHECKIN);
        }

        reserva.setDataEntrada(agora);
        reserva.setStatus(StatusReserva.CHECK_IN);
        log.debug("Atualizando reserva para o status CHECK_IN: {}", reserva);
        return reservaRepository.save(reserva);
    }

    @Override
    public CheckoutResponseDTO realizarCheckOut(Long idReserva) {
        log.debug("Iniciando processo de check-out para a reserva ID: {}", idReserva);
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    log.warn("Reserva não encontrada para o ID: {}", idReserva);
                    return new ReservaNaoEncontradaException(idReserva);
                });

        if (reserva.getStatus() != StatusReserva.CHECK_IN) {
            log.warn("Tentativa de check-out inválida. Status da reserva é {} mas deveria ser CHECK_IN.", reserva.getStatus());
            throw new ReservaStatusException(StatusReserva.CHECK_IN, reserva.getStatus());
        }

        reserva.setDataSaida(LocalDateTime.now(clock));
        log.debug("Data de saída definida para: {}", reserva.getDataSaida());

        CheckoutResponseDTO checkoutResponseDTO = calcularCustoTotal(reserva);

        reserva.setValorTotal(checkoutResponseDTO.getValorTotalGeral());
        reserva.setStatus(StatusReserva.CHECK_OUT);
        log.debug("Atualizando reserva para o status CHECK_OUT: {}", reserva);
        reservaRepository.save(reserva);

        return checkoutResponseDTO;
    }

    @Override
    public List<Reserva> buscarReservas(StatusReserva status) {
        log.debug("Iniciando busca de reservas com filtro de status: {}", status);
        if (status != null) {
            return reservaRepository.findByStatus(status);
        }
        return reservaRepository.findAll();
    }

    private CheckoutResponseDTO calcularCustoTotal(Reserva reserva) {
        log.debug("Iniciando cálculo de custo total para a reserva ID: {}", reserva.getId());
        List<String> detalhesDiarias = new ArrayList<>();
        BigDecimal valorTotalDiarias = BigDecimal.ZERO;
        BigDecimal valorTotalEstacionamento = BigDecimal.ZERO;
        BigDecimal valorMultaAtraso = BigDecimal.ZERO;

        LocalDate dia = reserva.getDataEntrada().toLocalDate();
        LocalDate dataSaida = reserva.getDataSaida().toLocalDate();

        while (!dia.isAfter(dataSaida)) {
            DayOfWeek diaDaSemana = dia.getDayOfWeek();
            BigDecimal diaria = (diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY) ? DIARIA_FIM_DE_SEMANA : DIARIA_SEMANA;
            valorTotalDiarias = valorTotalDiarias.add(diaria);
            detalhesDiarias.add("Diária " + dia + ": R$" + diaria);
            log.trace("Custo da diária para o dia {}: {}", dia, diaria);

            if (reserva.isAdicionalVeiculo()) {
                BigDecimal estacionamento = (diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY) ? ESTACIONAMENTO_FIM_DE_SEMANA : ESTACIONAMENTO_SEMANA;
                valorTotalEstacionamento = valorTotalEstacionamento.add(estacionamento);
                log.trace("Custo do estacionamento para o dia {}: {}", dia, estacionamento);
            }
            dia = dia.plusDays(1);
        }

        if (reserva.getDataSaida().toLocalTime().isAfter(HORA_CHECKOUT)) {
            DayOfWeek diaDaSemana = reserva.getDataSaida().toLocalDate().getDayOfWeek();
            BigDecimal ultimaDiaria = (diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY) ? DIARIA_FIM_DE_SEMANA : DIARIA_SEMANA;
            valorMultaAtraso = ultimaDiaria.multiply(new BigDecimal("0.5"));
            log.debug("Multa por checkout tardio aplicada. Valor: {}", valorMultaAtraso);
        }

        BigDecimal valorTotalGeral = valorTotalDiarias.add(valorTotalEstacionamento).add(valorMultaAtraso);
        log.info("Cálculo de custo finalizado para a reserva ID: {}. Diárias: {}, Estacionamento: {}, Multa: {}, Total: {}", 
                reserva.getId(), valorTotalDiarias, valorTotalEstacionamento, valorMultaAtraso, valorTotalGeral);

        CheckoutResponseDTO dto = new CheckoutResponseDTO();
        dto.setDetalhesDiarias(detalhesDiarias);
        dto.setValorTotalDiarias(valorTotalDiarias);
        dto.setValorTotalEstacionamento(valorTotalEstacionamento);
        dto.setValorMultaAtraso(valorMultaAtraso);
        dto.setValorTotalGeral(valorTotalGeral);

        return dto;
    }
}
