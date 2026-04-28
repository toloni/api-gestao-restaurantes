package br.com.toloni.gestaorestaurantes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.toloni.gestaorestaurantes.controller.dto.LoginDTO;
import br.com.toloni.gestaorestaurantes.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/login")
@Tag(name = "Login", description = "Autenticação de usuários no sistema")
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @Operation(summary = "Realizar login", description = "Autentica um usuário com base no login e senha fornecidos")
  @PostMapping
  public ResponseEntity<String> postMethodName(@RequestBody LoginDTO loginDTO) {
    log.info("Tentativa de login para o usuário: {}", loginDTO.login());
    loginService.login(loginDTO.login(), loginDTO.senha());
    return ResponseEntity.ok("Login realizado com sucesso");
  }

}
