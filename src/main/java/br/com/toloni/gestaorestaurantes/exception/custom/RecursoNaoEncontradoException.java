package br.com.toloni.gestaorestaurantes.exception.custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecursoNaoEncontradoException extends RuntimeException {

  public RecursoNaoEncontradoException(String recurso, String valor) {
    super(String.format("%s com valor '%s' não encontrado", recurso, valor));
    log.warn("{} com valor '{}' não encontrado", recurso, valor);
  }

}
