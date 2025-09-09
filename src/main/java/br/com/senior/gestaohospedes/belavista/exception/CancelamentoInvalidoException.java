package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CancelamentoInvalidoException extends BusinessException {

  public CancelamentoInvalidoException() {
    super("error.reserva.cancelamentoInvalido");
  }
}
