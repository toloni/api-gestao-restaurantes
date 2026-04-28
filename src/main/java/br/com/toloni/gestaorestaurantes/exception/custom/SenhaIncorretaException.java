package br.com.toloni.gestaorestaurantes.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SenhaIncorretaException extends RuntimeException {

  public SenhaIncorretaException(String message) {
    super("Senha incorreta");
    log.warn("Senha incorreta: {}", message);
  }

}
