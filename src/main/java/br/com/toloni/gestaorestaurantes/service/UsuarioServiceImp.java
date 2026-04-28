package br.com.toloni.gestaorestaurantes.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.toloni.gestaorestaurantes.domain.Usuario;
import br.com.toloni.gestaorestaurantes.exception.custom.RecursoNaoEncontradoException;
import br.com.toloni.gestaorestaurantes.exception.custom.SenhaIncorretaException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;
import br.com.toloni.gestaorestaurantes.service.validador.ValidadorFactory;

@Service
public class UsuarioServiceImp implements UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final ValidadorFactory validadorFactory;
  private final PasswordEncoder passwordEncoder;

  public UsuarioServiceImp(UsuarioRepository usuarioRepository, ValidadorFactory validadorFactory,
      PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.validadorFactory = validadorFactory;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Usuario salvar(Usuario usuario) {
    validadorFactory.getValidadoresParaCriacao().forEach(validador -> validador.validar(usuario));
    usuario.setUltimaAlteracao(LocalDate.now());
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    return usuarioRepository.save(usuario);
  }

  @Override
  public Page<Usuario> listar(String nome, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    if (nome != null) {
      return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    return usuarioRepository.findAll(pageable);
  }

  @Override
  public Usuario consultar(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id.toString()));
  }

  @Override
  public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id.toString()));

    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
      throw new SenhaIncorretaException("Senha atual incorreta para o usuário ID: " + id);
    }

    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);
  }

  @Override
  public void alterarUsuario(Usuario usuario) {

    Usuario usuarioEntity = usuarioRepository.findById(usuario.getId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuario.getId().toString()));

    validadorFactory.getValidadoresParaAlteracao().forEach(validador -> validador.validar(usuario));
    usuario.setSenha(usuarioEntity.getSenha());
    usuario.setUltimaAlteracao(LocalDate.now());
    usuarioRepository.save(usuario);
  }

  @Override
  public void excluir(Long id) {
    if (!usuarioRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Usuário", id.toString());
    }

    usuarioRepository.deleteById(id);
  }
}