package br.com.toloni.gestaorestaurantes.service.validador;

import br.com.toloni.gestaorestaurantes.domain.Usuario;

/**
 * Contrato para validadores de usuário.
 * Implementa o padrão Strategy para validações reutilizáveis e testáveis.
 */
public interface ValidadorUsuario {
  void validar(Usuario usuario);
}
