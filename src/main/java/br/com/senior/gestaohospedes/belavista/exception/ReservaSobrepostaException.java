package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservaSobrepostaException extends BusinessException {

    public ReservaSobrepostaException() {
        super("error.reserva.sobreposta");
    }
}
