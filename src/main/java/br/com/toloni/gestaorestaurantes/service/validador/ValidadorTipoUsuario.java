package br.com.toloni.gestaorestaurantes.service.validador;

import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.TipoUsuarioInvalidoException;

/**
 * Validador responsável por verificar se o tipo de usuário é válido.
 */
@Component
public class ValidadorTipoUsuario implements ValidadorUsuario {

  @Override
  public void validar(Usuario usuario) {
    if (usuario.getTipo() == null) {
      throw new TipoUsuarioInvalidoException("null");
    }
  }
}
