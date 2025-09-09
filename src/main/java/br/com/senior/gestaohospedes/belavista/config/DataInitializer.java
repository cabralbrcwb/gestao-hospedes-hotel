package br.com.senior.gestaohospedes.belavista.config;

import br.com.senior.gestaohospedes.belavista.entity.Perfil;
import br.com.senior.gestaohospedes.belavista.entity.Usuario;
import br.com.senior.gestaohospedes.belavista.repository.PerfilRepository;
import br.com.senior.gestaohospedes.belavista.repository.UsuarioRepository;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private final UsuarioRepository usuarioRepository;
  private final PerfilRepository perfilRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
      PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.perfilRepository = perfilRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    log.info("Iniciando verificação de dados iniciais...");

    // 1. Cria o perfil de ATENDENTE se ele não existir
    Perfil perfilAtendente = perfilRepository.findByNome("ATENDENTE").orElseGet(() -> {
      log.info("Perfil ATENDENTE não encontrado. Criando...");
      Perfil novoPerfil = new Perfil();
      novoPerfil.setNome("ATENDENTE");
      return perfilRepository.save(novoPerfil);
    });

    // 2. Cria o usuário atendente padrão se ele não existir
    if (usuarioRepository.findByLogin("atendente").isEmpty()) {
      log.info("Usuário 'atendente' não encontrado. Criando usuário padrão...");
      Usuario atendente = new Usuario();
      atendente.setLogin("atendente");
      atendente.setSenha(passwordEncoder.encode("123456")); // Senha padrão
      atendente.setPerfis(Set.of(perfilAtendente));
      usuarioRepository.save(atendente);
      log.info("Usuário 'atendente' criado com sucesso. Senha padrão: 123456");
    } else {
      log.info("Usuário 'atendente' já existe. Nenhuma ação necessária.");
    }

    log.info("Verificação de dados iniciais concluída.");
  }
}
