package br.com.toloni.gestaorestaurantes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.toloni.gestaorestaurantes.controller.dto.AlterarSenhaDTO;
import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioCreateDTO;
import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioResponseDTO;
import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioUpdateDTO;
import br.com.toloni.gestaorestaurantes.mapper.UsuarioMapper;
import br.com.toloni.gestaorestaurantes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

  private final UsuarioService usuarioService;
  private final UsuarioMapper usuarioMapper;

  public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
    this.usuarioService = usuarioService;
    this.usuarioMapper = usuarioMapper;
  }

  @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários, com opção de filtro por nome")
  @GetMapping
  public ResponseEntity<List<UsuarioResponseDTO>> getUsuarioString(@RequestParam(required = false) String nome,
      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("Listando usuários com nome: {}, page: {}, size: {}", nome, page, size);
    int pageAdjusted = Math.max(page - 1, 0);
    var usuarios = usuarioService.listar(nome, pageAdjusted, size);
    return ResponseEntity.ok(usuarioMapper.toResponseDTOList(usuarios));
  }

  @Operation(summary = "Consultar usuário por ID", description = "Retorna os detalhes de um usuário específico pelo seu ID")
  @GetMapping("/{id}")
  public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
    log.info("Consultando usuário com ID: {}", id);
    var usuario = usuarioService.consultar(id);
    return ResponseEntity.ok(usuarioMapper.toResponseDTO(usuario));
  }

  @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema. O email deve ser único.")
  @PostMapping
  public ResponseEntity<UsuarioResponseDTO> postUsuario(@RequestBody @Valid UsuarioCreateDTO entity) {
    log.info("Criando novo usuário: {}", entity);
    var savedUsuario = usuarioService.salvar(usuarioMapper.toEntity(entity));
    var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUsuario.getId())
        .toUri();
    return ResponseEntity.created(uri).body(usuarioMapper.toResponseDTO(savedUsuario));
  }

  @Operation(summary = "Alterar usuário", description = "Permite alterar os dados de um usuário existente")
  @PatchMapping("/{id}")
  public ResponseEntity<Void> alterarUsuario(
      @PathVariable Long id,
      @RequestBody @Valid UsuarioUpdateDTO usuarioRequestDTO) {
    log.info("Alterando usuário com ID: {}", id);
    var usuario = usuarioMapper.toEntityUpdate(usuarioRequestDTO);
    usuario.setId(id);
    usuarioService.alterarUsuario(usuario);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Excluir usuário", description = "Permite excluir um usuário existente do sistema")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
    log.info("Excluindo usuário com ID: {}", id);
    usuarioService.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Alterar senha", description = "Permite que um usuário altere sua senha fornecendo a senha atual e a nova senha")
  @PatchMapping("/{id}/alterar-senha")
  public ResponseEntity<Void> alterarSenha(
      @PathVariable Long id,
      @RequestBody @Valid AlterarSenhaDTO alterarSenhaDTO) {
    log.info("Alterando senha para usuário com ID: {}", id);
    usuarioService.alterarSenha(id, alterarSenhaDTO.senhaAtual(), alterarSenhaDTO.novaSenha());
    return ResponseEntity.noContent().build();
  }

}
