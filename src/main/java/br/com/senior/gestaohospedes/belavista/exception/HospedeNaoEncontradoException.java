package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HospedeNaoEncontradoException extends BusinessException {

  public HospedeNaoEncontradoException(Long id) {
    super("error.hospede.notFound", id);
  }
}
