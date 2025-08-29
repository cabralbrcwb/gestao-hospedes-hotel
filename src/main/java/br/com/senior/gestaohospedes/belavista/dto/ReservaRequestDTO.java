package br.com.senior.gestaohospedes.belavista.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaRequestDTO {

    private Long idHospede;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaidaPrevista;
    private boolean adicionalVeiculo;
}
