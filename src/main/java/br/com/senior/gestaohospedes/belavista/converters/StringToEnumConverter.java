package br.com.senior.gestaohospedes.belavista.converters;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.exception.EnumNaoEncontradoException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringToEnumConverter implements Converter<String, StatusReserva> {
  @Override
  public StatusReserva convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return Arrays.stream(StatusReserva.values())
        .filter(status -> status.name().equalsIgnoreCase(source))
        .findFirst()
        .orElseThrow(() -> new EnumNaoEncontradoException(source));
  }
}