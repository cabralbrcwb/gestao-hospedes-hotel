package br.com.senior.gestaohospedes.belavista.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.TipoDocumento;
import br.com.senior.gestaohospedes.belavista.exception.DocumentoDuplicadoException;
import br.com.senior.gestaohospedes.belavista.exception.HospedeNaoEncontradoException;
import br.com.senior.gestaohospedes.belavista.repository.HospedeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    hospede.setTipoDocumento(TipoDocumento.RG);
    when(hospedeRepository.findByDocumento("123456789")).thenReturn(Optional.empty());
    when(hospedeRepository.save(hospede)).thenReturn(hospede);

    Hospede hospedeSalvo = hospedeService.salvarHospede(hospede);

    assertNotNull(hospedeSalvo);
    verify(hospedeRepository).save(hospede);
  }

  @Test
  void salvarHospede_ComDocumentoDuplicado_DeveLancarExcecao() {
    Hospede hospede = new Hospede();
    hospede.setTipoDocumento(TipoDocumento.CPF);
    hospede.setDocumento("02514525011");
    when(hospedeRepository.findByDocumento("02514525011")).thenReturn(Optional.of(new Hospede()));

    assertThrows(DocumentoDuplicadoException.class, () -> hospedeService.salvarHospede(hospede));

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

    assertThrows(HospedeNaoEncontradoException.class, () -> hospedeService.buscarHospedePorId(1L));
  }
}
