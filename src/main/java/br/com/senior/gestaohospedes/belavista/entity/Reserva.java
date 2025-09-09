package br.com.senior.gestaohospedes.belavista.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime dataEntrada;

  private LocalDateTime dataSaida;

  private boolean adicionalVeiculo;

  private BigDecimal valorTotal;

  @Enumerated(EnumType.STRING)
  private StatusReserva status;

  @ManyToOne
  @JoinColumn(name = "hospede_id")
  private Hospede hospede;
}
