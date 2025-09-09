package br.com.senior.gestaohospedes.belavista.config;

import br.com.senior.gestaohospedes.belavista.converters.StringToEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final StringToEnumConverter stringToEnumConverter;

  public WebConfig(StringToEnumConverter stringToEnumConverter) {
    this.stringToEnumConverter = stringToEnumConverter;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Permite CORS para todos os endpoints
        .allowedOrigins("http://localhost:4200") // Endereço padrão do Angular dev server
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
        .allowedHeaders("*") // Permite todos os cabeçalhos
        .allowCredentials(true);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(stringToEnumConverter);
  }
}
