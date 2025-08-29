package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservaNaoEncontradaException extends BusinessException {

    public ReservaNaoEncontradaException(Long id) {
        super("error.reserva.notFound", id);
    }
}
