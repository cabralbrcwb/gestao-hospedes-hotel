package br.com.senior.gestaohospedes.belavista.exception;

import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckInForaDoHorarioException extends BusinessException {

  public CheckInForaDoHorarioException(LocalTime horario) {
    super("error.reserva.checkinForaDoHorario", horario.getHour());
  }
}
