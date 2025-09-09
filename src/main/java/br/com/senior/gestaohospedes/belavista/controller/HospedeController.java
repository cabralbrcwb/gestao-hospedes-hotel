package br.com.senior.gestaohospedes.belavista.controller;

import br.com.senior.gestaohospedes.belavista.entity.Hospede;
import br.com.senior.gestaohospedes.belavista.entity.TipoDocumento;
import br.com.senior.gestaohospedes.belavista.service.HospedeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {

  private final HospedeService hospedeService;
  @Autowired
  public HospedeController(HospedeService hospedeService) {
    this.hospedeService = hospedeService;
  }

  @PostMapping
  public ResponseEntity<Hospede> criarHospede(@Valid @RequestBody Hospede hospede) {
    log.info("Requisição para criar novo hóspede: {}", hospede);
    Hospede novoHospede = hospedeService.salvarHospede(hospede);
    log.info("Hóspede criado com sucesso: {}", novoHospede);
    return new ResponseEntity<>(novoHospede, HttpStatus.CREATED);
  }

  @GetMapping
  public List<Hospede> listarHospedes(@RequestParam(required = false) String nome,
      @RequestParam(required = false) String documento,
      @RequestParam(required = false) String telefone,
      @RequestParam(required = false) TipoDocumento tipoDocumento) {
    log.info("Requisição para buscar hóspedes com filtros - Nome: {}, Documento: {}, Telefone: {}, TipoDocumento: {}",
        nome, documento, telefone, tipoDocumento);
    List<Hospede> hospedes = hospedeService.buscarHospedes(nome, documento, telefone, tipoDocumento);
    log.debug("Total de hóspedes encontrados: {}", hospedes.size());
    return hospedes;
  }

  @GetMapping("/{id}")
  public Hospede buscarHospedePorId(@PathVariable Long id) {
    log.info("Requisição para buscar hóspede por ID: {}", id);
    return hospedeService.buscarHospedePorId(id);
  }

  @PutMapping("/{id}")
  public Hospede atualizarHospede(@PathVariable Long id, @Valid @RequestBody Hospede hospede) {
    log.info("Requisição para atualizar hóspede com ID {}: {}", id, hospede);
    return hospedeService.atualizarHospede(id, hospede);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarHospede(@PathVariable Long id) {
    log.info("Requisição para deletar hóspede com ID: {}", id);
    hospedeService.deletarHospede(id);
    log.info("Hóspede com ID {} deletado com sucesso.", id);
  }
}
