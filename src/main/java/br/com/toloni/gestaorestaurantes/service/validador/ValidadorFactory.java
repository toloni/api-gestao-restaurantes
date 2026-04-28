package br.com.toloni.gestaorestaurantes.service.validador;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Factory responsável por fornecer os validadores apropriados conforme o
 * contexto de operação.
 */
@Component
public class ValidadorFactory {

  private final ValidadorEmailUnico validadorEmailUnico;
  private final ValidadorLoginUnico validadorLoginUnico;
  private final ValidadorTipoUsuario validadorTipoUsuario;
  private final ValidadorEmailUnicoParaAlteracao validadorEmailUnicoParaAlteracao;
  private final ValidadorLoginUnicoParaAlteracao validadorLoginUnicoParaAlteracao;

  public ValidadorFactory(
      ValidadorEmailUnico validadorEmailUnico,
      ValidadorLoginUnico validadorLoginUnico,
      ValidadorTipoUsuario validadorTipoUsuario,
      ValidadorEmailUnicoParaAlteracao validadorEmailUnicoParaAlteracao,
      ValidadorLoginUnicoParaAlteracao validadorLoginUnicoParaAlteracao) {
    this.validadorEmailUnico = validadorEmailUnico;
    this.validadorLoginUnico = validadorLoginUnico;
    this.validadorTipoUsuario = validadorTipoUsuario;
    this.validadorEmailUnicoParaAlteracao = validadorEmailUnicoParaAlteracao;
    this.validadorLoginUnicoParaAlteracao = validadorLoginUnicoParaAlteracao;
  }

  /**
   * Retorna os validadores para criação de novo usuário.
   */
  public List<ValidadorUsuario> getValidadoresParaCriacao() {
    return List.of(
        validadorEmailUnico,
        validadorLoginUnico,
        validadorTipoUsuario);
  }

  /**
   * Retorna os validadores para alteração de usuário existente.
   */
  public List<ValidadorUsuario> getValidadoresParaAlteracao() {
    return List.of(
        validadorEmailUnicoParaAlteracao,
        validadorLoginUnicoParaAlteracao,
        validadorTipoUsuario);
  }
}
