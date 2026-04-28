package br.com.toloni.gestaorestaurantes.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.toloni.gestaorestaurantes.exception.custom.LoginInvalidoException;
import br.com.toloni.gestaorestaurantes.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImp implements LoginService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginServiceImp(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Boolean login(String login, String senha) {

    var usuario = usuarioRepository.findByLogin(login)
        .orElseThrow(() -> new LoginInvalidoException("Usuário não encontrado: " + login));

    if (!passwordEncoder.matches(senha, usuario.getSenha())) {
      throw new LoginInvalidoException("Senha incorreta para o usuário: " + login);
    }

    return true;

  }

}
