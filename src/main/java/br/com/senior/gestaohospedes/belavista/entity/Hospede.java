package br.com.senior.gestaohospedes.belavista.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Hospede {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{validation.field.notBlank}")
  @Size(max = 500, message = "{validation.field.size}")
  private String nome;

  @Enumerated(EnumType.STRING)
  @Column
  @NotNull(message = "{validation.field.notBlank}")
  private TipoDocumento tipoDocumento;


  @NotBlank(message = "{validation.field.notBlank}")
  @Column(unique = true)
  private String documento;

  @NotBlank(message = "{validation.field.notBlank}")
  private String telefone;

  @Email(message = "{validation.field.email}")
  private String email;

  @OneToMany(mappedBy = "hospede")
  @JsonIgnore // Evita serialização para prevenir problemas de recursão infinita
  private List<Reserva> reservas;
}
