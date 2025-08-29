package br.com.senior.gestaohospedes.belavista.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global de CORS (Cross-Origin Resource Sharing) para a aplicação.
 * <p>
 * Esta classe permite que o front-end (rodando em http://localhost:4200)
 * se comunique com a API, definindo as origens, métodos e cabeçalhos permitidos.
 */
@Slf4j
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  /**
   * Configura as permissões de CORS para todos os endpoints da API ("/**").
   *
   * @param registry o registro de CORS para adicionar os mapeamentos.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.info("Configurando CORS para permitir requisições de http://localhost:4200");
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}