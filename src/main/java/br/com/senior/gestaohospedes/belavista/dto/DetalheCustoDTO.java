package br.com.senior.gestaohospedes.belavista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalheCustoDTO {

    private String descricao;
    private LocalDate data;
    private BigDecimal valor;

}
