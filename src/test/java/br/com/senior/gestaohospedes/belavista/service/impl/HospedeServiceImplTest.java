package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.exception.DocumentoDuplicadoException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeNaoEncontradoException;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospedeServiceImplTest {

    @Mock
    private HospedeRepository hospedeRepository;

    @InjectMocks
    private HospedeServiceImpl hospedeService;

    @Test
    void salvarHospede_ComDocumentoUnico_DeveSalvarComSucesso() {
        Hospede hospede = new Hospede();
        hospede.setDocumento("123456789");
        when(hospedeRepository.findByDocumento("123456789")).thenReturn(Optional.empty());
        when(hospedeRepository.save(hospede)).thenReturn(hospede);

        Hospede hospedeSalvo = hospedeService.salvarHospede(hospede);

        assertNotNull(hospedeSalvo);
        verify(hospedeRepository).save(hospede);
    }

    @Test
    void salvarHospede_ComDocumentoDuplicado_DeveLancarExcecao() {
        Hospede hospede = new Hospede();
        hospede.setDocumento("123456789");
        when(hospedeRepository.findByDocumento("123456789")).thenReturn(Optional.of(new Hospede()));

        assertThrows(DocumentoDuplicadoException.class, () -> {
            hospedeService.salvarHospede(hospede);
        });

        verify(hospedeRepository, never()).save(hospede);
    }

    @Test
    void buscarHospedePorId_ComIdExistente_DeveRetornarHospede() {
        when(hospedeRepository.findById(1L)).thenReturn(Optional.of(new Hospede()));

        Hospede hospede = hospedeService.buscarHospedePorId(1L);

        assertNotNull(hospede);
    }

    @Test
    void buscarHospedePorId_ComIdInexistente_DeveLancarExcecao() {
        when(hospedeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HospedeNaoEncontradoException.class, () -> {
            hospedeService.buscarHospedePorId(1L);
        });
    }
}
