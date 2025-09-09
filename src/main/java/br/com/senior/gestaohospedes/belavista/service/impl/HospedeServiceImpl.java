package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.entity.TipoDocumento;
import br.com.senior.gestaohospedes.belavista.exception.DocumentoDuplicadoException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeComReservaAtivaException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeNaoEncontradoException;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import br.com.senior.gestaohospedes.belavista.repository.ReservaRepository;
import br.com.senior.gestaohospedes.belavista.service.HospedeService;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HospedeServiceImpl implements HospedeService {

  private final HospedeRepository hospedeRepository;
  private final ReservaRepository reservaRepository;

  public HospedeServiceImpl(HospedeRepository hospedeRepository,
      ReservaRepository reservaRepository) {
    this.hospedeRepository = hospedeRepository;
    this.reservaRepository = reservaRepository;
  }

  @Override
  public Hospede salvarHospede(Hospede hospede) {
    log.debug("Iniciando processo para salvar hóspede.");
    hospedeRepository.findByDocumento(hospede.getDocumento()).ifPresent(h -> {
      log.warn("Tentativa de cadastro com documento duplicado: {}", hospede.getDocumento());
      throw new DocumentoDuplicadoException(hospede.getDocumento());
    });
    log.debug("Documento verificado. Salvando novo hóspede no banco de dados.");
    return hospedeRepository.save(hospede);
  }

  @Override
  public List<Hospede> buscarHospedes(String nome, String documento, String telefone,
      TipoDocumento tipoDocumento) {
    log.debug(
        "Iniciando busca de hóspedes com filtros: nome={}, documento={}, telefone={}, tipoDocumento={}",
        nome, documento, telefone, tipoDocumento);

    if (tipoDocumento != null && StringUtils.hasText(documento)) {
      log.debug("Buscando por tipo de documento {} e documento {}", tipoDocumento, documento);
      return hospedeRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento);
    }

    if (tipoDocumento != null) {
      log.debug("Buscando por tipo de documento: {}", tipoDocumento);
      return hospedeRepository.findByTipoDocumento(tipoDocumento);
    }

    if (StringUtils.hasText(nome)) {
      log.debug("Buscando por nome: {}", nome);
      return hospedeRepository.findByNomeContainingIgnoreCase(nome);
    }

    if (StringUtils.hasText(documento)) {
      log.debug("Buscando por documento: {}", documento);
      return hospedeRepository.findByDocumento(documento)
          .map(Collections::singletonList)
          .orElse(Collections.emptyList());
    }

    if (StringUtils.hasText(telefone)) {
      log.debug("Buscando por telefone: {}", telefone);
      return hospedeRepository.findByTelefone(telefone);
    }

    log.debug("Nenhum filtro específico fornecido. Buscando todos os hóspedes.");
    return hospedeRepository.findAll();
  }

  @Override
  public Hospede buscarHospedePorId(Long id) {
    log.debug("Buscando hóspede por ID: {}", id);
    return hospedeRepository.findById(id).orElseThrow(() -> {
      log.warn("Hóspede não encontrado para o ID: {}", id);
      return new HospedeNaoEncontradoException(id);
    });
  }

  @Override
  public Hospede atualizarHospede(Long id, Hospede hospedeAtualizado) {
    log.debug("Iniciando atualização para o hóspede de ID: {}", id);
    Hospede hospedeExistente = buscarHospedePorId(id);

    // Verifica se o documento foi alterado e se o novo já existe para outro hóspede
    if (!hospedeExistente.getDocumento().equals(hospedeAtualizado.getDocumento())) {
      Optional<Hospede> hospedeComNovoDocumento = hospedeRepository.findByDocumento(
          hospedeAtualizado.getDocumento());
      if (hospedeComNovoDocumento.isPresent() && !hospedeComNovoDocumento.get().getId()
          .equals(id)) {
        log.warn("Tentativa de atualizar para um documento que já pertence a outro hóspede: {}",
            hospedeAtualizado.getDocumento());
        throw new DocumentoDuplicadoException(hospedeAtualizado.getDocumento());
      }
      hospedeExistente.setDocumento(hospedeAtualizado.getDocumento());
    }

    hospedeExistente.setNome(hospedeAtualizado.getNome());
    hospedeExistente.setTelefone(hospedeAtualizado.getTelefone());

    log.debug("Salvando dados atualizados para o hóspede de ID: {}", id);
    return hospedeRepository.save(hospedeExistente);
  }


  @Override
  public void deletarHospede(Long id) {
    log.debug("Iniciando exclusão do hóspede de ID: {}", id);
    Hospede hospede = buscarHospedePorId(id);

    boolean temReservasAtivas = reservaRepository.existsByHospedeIdAndStatusIn(id,
        List.of(StatusReserva.PENDENTE, StatusReserva.CHECK_IN, StatusReserva.CONFIRMADA));
    if (temReservasAtivas) {
      log.warn("Tentativa de exclusão de hóspede com reservas ativas. ID do Hóspede: {}", id);
      throw new HospedeComReservaAtivaException();

    }
  }
}