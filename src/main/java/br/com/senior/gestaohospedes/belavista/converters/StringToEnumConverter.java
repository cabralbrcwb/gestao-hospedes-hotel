package br.com.senior.gestaohospedes.belavista.converters;

import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, StatusReserva> {

  @Override
  public StatusReserva convert(String source) {
    try {
      return StatusReserva.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null; // Ou lance uma exceção mais específica
    }
  }
}
