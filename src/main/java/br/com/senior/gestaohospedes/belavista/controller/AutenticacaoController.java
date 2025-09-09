package br.com.senior.gestaohospedes.belavista.controller;

import br.com.senior.gestaohospedes.belavista.dto.auth.LoginRequestDTO;
import br.com.senior.gestaohospedes.belavista.dto.auth.TokenResponseDTO;
import br.com.senior.gestaohospedes.belavista.entity.Usuario;
import br.com.senior.gestaohospedes.belavista.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequestDTO dados) {
    var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getLogin(),
        dados.getSenha());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

    return ResponseEntity.ok(new TokenResponseDTO(tokenJWT));
  }
}

