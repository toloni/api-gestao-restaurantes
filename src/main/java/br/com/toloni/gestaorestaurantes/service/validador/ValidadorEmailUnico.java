package br.com.toloni.gestaorestaurantes.service.validador;

import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.UsuarioJaCadastradoException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;

/**
 * Validador responsável por verificar se o email é único.
 */
@Component
public class ValidadorEmailUnico implements ValidadorUsuario {

  private final UsuarioRepository usuarioRepository;

  public ValidadorEmailUnico(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public void validar(Usuario usuario) {
    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new UsuarioJaCadastradoException("email", usuario.getEmail());
    }
  }
}
