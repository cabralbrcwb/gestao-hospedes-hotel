package br.com.senior.gestaohospedes.belavista.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservaRequestDTO {

    @NotNull(message = "O ID do hóspede é obrigatório.")
    private Long idHospede;

    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaidaPrevista;
    private boolean adicionalVeiculo;
}
