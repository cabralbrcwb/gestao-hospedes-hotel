package br.com.senior.gestaohospedes.belavista.controller;

import br.com.senior.gestaohospedes.belavista.dto.CheckoutResponseDTO;
import br.com.senior.gestaohospedes.belavista.dto.ReservaRequestDTO;
import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.service.ReservaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public Reserva criarReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        log.info("Requisição para criar nova reserva: {}", dto);
        Reserva novaReserva = reservaService.criarReserva(dto);
        log.info("Reserva criada com sucesso: {}", novaReserva);
        return novaReserva;
    }

    @PostMapping("/{id}/check-in")
    public Reserva realizarCheckIn(@PathVariable Long id) {
        log.info("Requisição para realizar check-in da reserva com ID: {}", id);
        Reserva reserva = reservaService.realizarCheckIn(id);
        log.info("Check-in da reserva {} realizado com sucesso.", id);
        return reserva;
    }

    @PostMapping("/{id}/check-out")
    public CheckoutResponseDTO realizarCheckOut(@PathVariable Long id) {
        log.info("Requisição para realizar check-out da reserva com ID: {}", id);
        CheckoutResponseDTO checkout = reservaService.realizarCheckOut(id);
        log.info("Check-out da reserva {} realizado com sucesso. Valor total: {}", id, checkout.getValorTotalGeral());
        return checkout;
    }

    @GetMapping
    public List<Reserva> buscarReservas(@RequestParam(required = false) StatusReserva status) {
        log.info("Requisição para buscar reservas com status: {}", status);
        List<Reserva> reservas = reservaService.buscarReservas(status);
        log.debug("Total de reservas encontradas: {}", reservas.size());
        return reservas;
    }
}
