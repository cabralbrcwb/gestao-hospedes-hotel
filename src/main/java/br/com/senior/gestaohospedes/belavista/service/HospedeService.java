package br.com.senior.gestaohospedes.belavista.service;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.TipoDocumento;

import java.util.List;

public interface HospedeService {

  Hospede salvarHospede(Hospede hospede);

  List<Hospede> buscarHospedes(String nome, String documento, String telefone, TipoDocumento tipoDocumento);

  Hospede buscarHospedePorId(Long id);

  Hospede atualizarHospede(Long id, Hospede hospedeAtualizado);

  void deletarHospede(Long id);
}
