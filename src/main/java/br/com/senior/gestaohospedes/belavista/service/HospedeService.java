package br.com.senior.gestaohospedes.belavista.service;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;

import java.util.List;

public interface HospedeService {

    Hospede salvarHospede(Hospede hospede);

    List<Hospede> buscarHospedes(String nome, String documento, String telefone);

    Hospede buscarHospedePorId(Long id);

    Hospede atualizarHospede(Long id, Hospede hospedeAtualizado);

    void deletarHospede(Long id);
}
