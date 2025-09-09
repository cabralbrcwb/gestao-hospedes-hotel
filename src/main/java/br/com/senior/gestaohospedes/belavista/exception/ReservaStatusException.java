package br.com.senior.gestaohospedes.belavista.exception;

import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservaStatusException extends BusinessException {

  public ReservaStatusException(StatusReserva esperado, StatusReserva atual) {
    super("error.reserva.statusInvalido", esperado, atual);
  }
}
