package br.com.senior.gestaohospedes.belavista.security;

import br.com.senior.gestaohospedes.belavista.repository.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Value("${api.security.token.secret}")
  private String secret;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    var tokenJWT = recuperarToken(request);

    if (tokenJWT != null) {
      try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var subject = JWT.require(algorithm).withIssuer("API Bela Vista").build().verify(tokenJWT)
            .getSubject();

        var usuario = usuarioRepository.findByLogin(subject).orElseThrow();
        var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
            usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (JWTVerificationException exception) {
        // Token inválido, não faz nada, o Spring Security tratará como não autenticado.
      }
    }

    filterChain.doFilter(request, response);
  }

  private String recuperarToken(HttpServletRequest request) {
    var authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null) {
      return authorizationHeader.replace("Bearer ", "").trim();
    }
    return null;
  }
}
