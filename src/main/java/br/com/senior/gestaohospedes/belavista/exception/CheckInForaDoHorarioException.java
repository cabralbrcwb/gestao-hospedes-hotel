package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckInForaDoHorarioException extends BusinessException {

    public CheckInForaDoHorarioException(LocalTime horario) {
        super("error.reserva.checkinForaDoHorario", horario.getHour());
    }
}
