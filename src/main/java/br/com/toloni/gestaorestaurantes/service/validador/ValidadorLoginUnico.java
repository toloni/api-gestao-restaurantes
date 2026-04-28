package br.com.toloni.gestaorestaurantes.service.validador;

import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.UsuarioJaCadastradoException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;

/**
 * Validador responsável por verificar se o login é único.
 */
@Component
public class ValidadorLoginUnico implements ValidadorUsuario {

  private final UsuarioRepository usuarioRepository;

  public ValidadorLoginUnico(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public void validar(Usuario usuario) {
    if (usuarioRepository.existsByLogin(usuario.getLogin())) {
      throw new UsuarioJaCadastradoException("login", usuario.getLogin());
    }
  }
}
