package br.com.toloni.gestaorestaurantes.service;

import org.springframework.data.domain.Page;

import br.com.toloni.gestaorestaurantes.domain.Usuario;

public interface UsuarioService {

  public Usuario salvar(Usuario usuario);

  public Page<Usuario> listar(String nome, int page, int size);

  public Usuario consultar(Long id);

  public void alterarSenha(Long id, String senhaAtual, String novaSenha);

  public void alterarUsuario(Usuario usuario);

  public void excluir(Long id);
}
