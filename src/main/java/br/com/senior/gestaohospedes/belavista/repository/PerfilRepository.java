package br.com.senior.gestaohospedes.belavista.repository;

import br.com.senior.gestaohospedes.belavista.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(String nome);
}
