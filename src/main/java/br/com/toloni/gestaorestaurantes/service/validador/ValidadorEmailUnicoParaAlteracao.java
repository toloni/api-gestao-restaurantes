package br.com.toloni.gestaorestaurantes.service.validador;

import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.UsuarioJaCadastradoException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;

/**
 * Validador responsável por verificar se o email é único durante a alteração de
 * usuário,
 * excluindo o próprio usuário da verificação.
 */
@Component
public class ValidadorEmailUnicoParaAlteracao implements ValidadorUsuario {

  private final UsuarioRepository usuarioRepository;

  public ValidadorEmailUnicoParaAlteracao(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public void validar(Usuario usuario) {
    if (usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), usuario.getId())) {
      throw new UsuarioJaCadastradoException("email", usuario.getEmail());
    }
  }
}
