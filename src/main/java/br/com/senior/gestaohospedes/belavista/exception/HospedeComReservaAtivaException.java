package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HospedeComReservaAtivaException extends BusinessException {

  public HospedeComReservaAtivaException() {
    super("error.hospede.comReservaAtiva");
  }
}
