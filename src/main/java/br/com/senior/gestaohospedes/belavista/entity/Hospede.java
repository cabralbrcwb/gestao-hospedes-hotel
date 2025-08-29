package br.com.senior.gestaohospedes.belavista.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "{validation.field.notBlank}")
    @Column(unique = true)
    private String documento;

    @NotBlank(message = "{validation.field.notBlank}")
    private String telefone;

    @OneToMany(mappedBy = "hospede")
    private List<Reserva> reservas;
}
