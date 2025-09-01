package br.com.senior.gestaohospedes.belavista.repository;

import br.com.senior.gestaohospedes.belavista.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}
