package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.dto.CheckoutResponseDTO;
import br.com.senior.gestaohospedes.belavista.dto.ReservaRequestDTO;
import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import br.com.senior.gestaohospedes.belavista.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HospedeRepository hospedeRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private void mockClock(LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    void criarReserva_ComHospedeExistente_DeveCriarReserva() {
        // Cenário
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setIdHospede(1L);
        dto.setDataEntrada(LocalDateTime.of(2025, 10, 10, 14, 0));
        dto.setDataSaidaPrevista(LocalDateTime.of(2025, 10, 12, 12, 0));

        when(hospedeRepository.findById(1L)).thenReturn(Optional.of(new Hospede()));
        when(reservaRepository.findAllByHospedeIdAndStatusIn(any(), any())).thenReturn(Collections.emptyList()); // Simula que não há reservas sobrepostas
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        // Ação
        Reserva reserva = reservaService.criarReserva(dto);

        // Verificação
        assertNotNull(reserva);
        assertEquals(StatusReserva.PENDENTE, reserva.getStatus());
    }

    @Test
    void realizarCheckIn_ComReservaPendenteEHorarioValido_DeveRealizarCheckIn() {
        Reserva reserva = new Reserva();
        reserva.setStatus(StatusReserva.PENDENTE);
        LocalDateTime checkinTime = LocalDateTime.of(2024, 5, 24, 15, 0); // Check-in às 15:00
        mockClock(checkinTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        Reserva reservaCheckedIn = reservaService.realizarCheckIn(1L);

        assertEquals(StatusReserva.CHECK_IN, reservaCheckedIn.getStatus());
        assertEquals(checkinTime, reservaCheckedIn.getDataEntrada());
    }

    @Test
    void realizarCheckOut_ComCheckoutNoPrazo_CalculaValorCorreto() {
        Reserva reserva = new Reserva();
        reserva.setStatus(StatusReserva.CHECK_IN);
        reserva.setDataEntrada(LocalDateTime.of(2024, 5, 24, 14, 0)); // Check-in na Sexta
        reserva.setAdicionalVeiculo(true);

        LocalDateTime checkoutTime = LocalDateTime.of(2024, 5, 26, 11, 0); // Checkout no Domingo às 11:00
        mockClock(checkoutTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutResponseDTO checkout = reservaService.realizarCheckOut(1L);

        // Diárias: Sexta (120) + Sábado (180) + Domingo (180) = 480
        // Estacionamento: Sexta (15) + Sábado (20) + Domingo (20) = 55
        // Total: 535
        assertEquals(0, new BigDecimal("480.00").compareTo(checkout.getValorTotalDiarias()));
        assertEquals(0, new BigDecimal("55.00").compareTo(checkout.getValorTotalEstacionamento()));
        assertEquals(0, BigDecimal.ZERO.compareTo(checkout.getValorMultaAtraso()));
        assertEquals(0, new BigDecimal("535.00").compareTo(checkout.getValorTotalGeral()));
        assertEquals(StatusReserva.CHECK_OUT, reserva.getStatus());
        assertEquals(checkoutTime, reserva.getDataSaida());
    }

    @Test
    void realizarCheckOut_ComCheckoutAtrasado_CalculaValorComMulta() {
        Reserva reserva = new Reserva();
        reserva.setStatus(StatusReserva.CHECK_IN);
        reserva.setDataEntrada(LocalDateTime.of(2024, 5, 22, 14, 0)); // Check-in na Quarta
        reserva.setAdicionalVeiculo(false);

        LocalDateTime checkoutTime = LocalDateTime.of(2024, 5, 23, 13, 0); // Checkout na Quinta às 13:00 (atrasado)
        mockClock(checkoutTime);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutResponseDTO checkout = reservaService.realizarCheckOut(1L);

        // Diárias: Quarta (120) + Quinta (120) = 240
        // Multa: 50% da diária de Quinta (120 * 0.5) = 60
        // Total: 300
        assertEquals(0, new BigDecimal("240.00").compareTo(checkout.getValorTotalDiarias()));
        assertEquals(0, BigDecimal.ZERO.compareTo(checkout.getValorTotalEstacionamento()));
        assertEquals(0, new BigDecimal("60.00").compareTo(checkout.getValorMultaAtraso()));
        assertEquals(0, new BigDecimal("300.00").compareTo(checkout.getValorTotalGeral()));
    }
}
