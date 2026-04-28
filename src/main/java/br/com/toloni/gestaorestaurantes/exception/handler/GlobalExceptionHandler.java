package br.com.toloni.gestaorestaurantes.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.toloni.gestaorestaurantes.exception.custom.LoginInvalidoException;
import br.com.toloni.gestaorestaurantes.exception.custom.RecursoNaoEncontradoException;
import br.com.toloni.gestaorestaurantes.exception.custom.SenhaIncorretaException;
import br.com.toloni.gestaorestaurantes.exception.custom.TipoUsuarioInvalidoException;
import br.com.toloni.gestaorestaurantes.exception.custom.UsuarioJaCadastradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problem.setTitle("Erro de validação");
    problem.setDetail("Um ou mais campos estão inválidos");

    var erros = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .toList();

    problem.setProperty("errors", erros);

    return problem;
  }

  @ExceptionHandler(UsuarioJaCadastradoException.class)
  public ProblemDetail handleConflict(UsuarioJaCadastradoException ex) {

    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

    problem.setTitle("Usuário já cadastrado");
    problem.setDetail(ex.getMessage());

    return problem;
  }

  @ExceptionHandler(TipoUsuarioInvalidoException.class)
  public ProblemDetail handleTipoUsuarioInvalido(TipoUsuarioInvalidoException ex) {

    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problem.setTitle("Tipo de usuário inválido");
    problem.setDetail(ex.getMessage());

    return problem;
  }

  @ExceptionHandler(LoginInvalidoException.class)
  public ProblemDetail handleLoginInvalido(LoginInvalidoException ex) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

    problem.setTitle("Login inválido");
    problem.setDetail(ex.getMessage());

    return problem;
  }

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ProblemDetail handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

    problem.setTitle("Recurso não encontrado");
    problem.setDetail(ex.getMessage());

    return problem;
  }

  @ExceptionHandler(SenhaIncorretaException.class)
  public ProblemDetail handleSenhaIncorreta(SenhaIncorretaException ex) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problem.setTitle("Senha incorreta");
    problem.setDetail(ex.getMessage());

    return problem;
  }

}
