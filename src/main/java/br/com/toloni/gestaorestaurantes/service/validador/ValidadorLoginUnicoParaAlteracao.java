package br.com.toloni.gestaorestaurantes.service.validador;

import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.UsuarioJaCadastradoException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;

/**
 * Validador responsável por verificar se o login é único durante a alteração de
 * usuário,
 * excluindo o próprio usuário da verificação.
 */
@Component
public class ValidadorLoginUnicoParaAlteracao implements ValidadorUsuario {

  private final UsuarioRepository usuarioRepository;

  public ValidadorLoginUnicoParaAlteracao(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public void validar(Usuario usuario) {
    if (usuarioRepository.existsByLoginAndIdNot(usuario.getLogin(), usuario.getId())) {
      throw new UsuarioJaCadastradoException("login", usuario.getLogin());
    }
  }
}
