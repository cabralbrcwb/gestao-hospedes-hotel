package br.com.senior.gestaohospedes.belavista.service.impl;

import br.com.senior.gestaohospedes.belavista.repository.UsuarioRepository;
import br.com.senior.gestaohospedes.belavista.service.AutenticacaoService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

  private final UsuarioRepository usuarioRepository;
  private final MessageSource messageSource;

  public AutenticacaoServiceImpl(UsuarioRepository usuarioRepository, MessageSource messageSource) {
    this.usuarioRepository = usuarioRepository;
    this.messageSource = messageSource;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findByLogin(username).orElseThrow(() -> {
      String errorMessage = messageSource.getMessage("error.user.notfound", new Object[]{username},
          LocaleContextHolder.getLocale());
      return new UsernameNotFoundException(errorMessage);
    });
  }
}
