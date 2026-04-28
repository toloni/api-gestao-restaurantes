package br.com.toloni.gestaorestaurantes.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TipoUsuarioInvalidoException extends IllegalArgumentException {

  public TipoUsuarioInvalidoException(String tipo) {
    super("Tipo de usuário inválido: " + tipo);
    log.warn("Tipo de usuário inválido: {}", tipo);
  }

}
