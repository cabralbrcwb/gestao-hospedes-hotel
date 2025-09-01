package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import br.com.senior.gestaohospedes.belavista.exception.DocumentoDuplicadoException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeComReservaAtivaException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeNaoEncontradoException;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import br.com.senior.gestaohospedes.belavista.repository.ReservaRepository;
import br.com.senior.gestaohospedes.belavista.service.HospedeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class HospedeServiceImpl implements HospedeService {

    private final HospedeRepository hospedeRepository;
    private final ReservaRepository reservaRepository;

    public HospedeServiceImpl(HospedeRepository hospedeRepository, ReservaRepository reservaRepository) {
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
    public List<Hospede> buscarHospedes(String nome, String documento, String telefone) {
        log.debug("Iniciando busca de hóspedes com filtros.");
        if (nome != null) {
            log.debug("Buscando por nome: {}", nome);
            return hospedeRepository.findByNomeContainingIgnoreCase(nome);
        }
        if (documento != null) {
            log.debug("Buscando por documento: {}", documento);
            return hospedeRepository.findByDocumento(documento)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }
        if (telefone != null) {
            log.debug("Buscando por telefone: {}", telefone);
            return hospedeRepository.findByTelefone(telefone);
        }
        log.debug("Nenhum filtro específico fornecido. Buscando todos os hóspedes.");
        return hospedeRepository.findAll();
    }

    @Override
    public Hospede buscarHospedePorId(Long id) {
        log.debug("Buscando hóspede por ID: {}", id);
        return hospedeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Hóspede não encontrado para o ID: {}", id);
                    return new HospedeNaoEncontradoException(id);
                });
    }

    @Override
    public Hospede atualizarHospede(Long id, Hospede hospedeAtualizado) {
        log.debug("Iniciando atualização para o hóspede de ID: {}", id);
        Hospede hospedeExistente = buscarHospedePorId(id);
        hospedeExistente.setNome(hospedeAtualizado.getNome());
        hospedeExistente.setTelefone(hospedeAtualizado.getTelefone());
        log.debug("Salvando dados atualizados para o hóspede de ID: {}", id);
        return hospedeRepository.save(hospedeExistente);
    }

    @Override
    public void deletarHospede(Long id) {
        log.debug("Iniciando exclusão do hóspede de ID: {}", id);
        Hospede hospede = buscarHospedePorId(id);

        // CORREÇÃO: Alterado de PENDENTE para CONFIRMADA para alinhar com a nova regra de negócio.
        boolean temReservasAtivas = reservaRepository.existsByHospedeIdAndStatusIn(id, List.of(StatusReserva.CONFIRMADA, StatusReserva.CHECK_IN));
        if (temReservasAtivas) {
            log.warn("Tentativa de exclusão de hóspede com reservas ativas. ID do Hóspede: {}", id);
            throw new HospedeComReservaAtivaException();
        }

        hospedeRepository.delete(hospede);
        log.info("Hóspede de ID {} excluído com sucesso.", id);
    }
}
