package br.com.toloni.gestaorestaurantes.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginInvalidoException extends RuntimeException {

  public LoginInvalidoException(String message) {
    super("Login ou senha inválidos");
    log.warn("Login ou senha inválidos: {}", message);
  }

}
