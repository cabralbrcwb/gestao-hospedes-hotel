package br.com.senior.gestaohospedes.belavista.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutResponseDTO {

    private List<String> detalhesDiarias;
    private BigDecimal valorTotalDiarias;
    private BigDecimal valorTotalEstacionamento;
    private BigDecimal valorMultaAtraso;
    private BigDecimal valorTotalGeral;
}
