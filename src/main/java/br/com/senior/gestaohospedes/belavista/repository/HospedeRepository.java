package br.com.senior.gestaohospedes.belavista.repository;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {

  Optional<Hospede> findByDocumento(String documento);

  List<Hospede> findByTipoDocumento(TipoDocumento tipoDocumento);

  List<Hospede> findByNomeContainingIgnoreCase(String nome);

  List<Hospede> findByTelefone(String telefone);

  List<Hospede> findByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);
}
