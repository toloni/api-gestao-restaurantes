package br.com.toloni.gestaorestaurantes.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsuarioJaCadastradoException extends RuntimeException {

  public UsuarioJaCadastradoException(String campo, String valor) {
    super("Já existe usuário com " + campo + ": " + valor);
    log.warn("Já existe usuário com {}: {}", campo, valor);
  }
}
