package br.com.senior.gestaohospedes.belavista.service;

import br.com.senior.gestaohospedes.belavista.dto.CheckoutResponseDTO;
import br.com.senior.gestaohospedes.belavista.dto.ReservaRequestDTO;
import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import java.util.List;

public interface ReservaService {

  Reserva criarReserva(ReservaRequestDTO dto);

  Reserva realizarCheckIn(Long idReserva);

  CheckoutResponseDTO realizarCheckOut(Long idReserva);

  List<Reserva> buscarReservas(StatusReserva status);

  void cancelarReserva(Long idReserva);
}
