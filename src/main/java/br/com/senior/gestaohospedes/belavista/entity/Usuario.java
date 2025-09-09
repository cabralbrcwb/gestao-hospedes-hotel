package br.com.senior.gestaohospedes.belavista.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidade que representa um usuário para o Spring Security.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String login;

  @Column(nullable = false)
  private String senha;
  /**
   * Relação muitos-para-muitos entre o usuário e seus perfis. Usa carregamento EAGER para compor as
   * authorities no Spring Security. Mapeada pela tabela de junção `usuario_perfis` (`usuario_id`,
   * `perfil_id`).
   */
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "usuario_perfis", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
  private Set<Perfil> perfis;

  /**
   * O método converte os perfis do usuário em authorities do Spring Security, retornando a lista
   * usada na autorização. Cada Perfil vira um SimpleGrantedAuthority com o nome do perfil.
   *
   * @return authorities do Spring Security
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return perfis.stream().map(perfil -> new SimpleGrantedAuthority(perfil.getNome()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return senha;
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}