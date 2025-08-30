package br.com.senior.gestaohospedes.belavista.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um perfil de usuário para controle de acesso.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "perfis")
public class Perfil {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String nome;
}