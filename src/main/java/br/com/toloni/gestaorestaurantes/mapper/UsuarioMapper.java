package br.com.toloni.gestaorestaurantes.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioCreateDTO;
import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioResponseDTO;
import br.com.toloni.gestaorestaurantes.controller.dto.UsuarioUpdateDTO;
import br.com.toloni.gestaorestaurantes.domain.TipoUsuario;
import br.com.toloni.gestaorestaurantes.domain.Usuario;

@Component
public class UsuarioMapper {

  public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
    return new UsuarioResponseDTO(
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getLogin(),
        usuario.getTipo().name(),
        usuario.getEndereco());
  }

  public Usuario toEntity(UsuarioCreateDTO dto) {
    return Usuario.builder()
        .nome(dto.nome())
        .email(dto.email())
        .login(dto.login())
        .senha(dto.senha())
        .tipo(TipoUsuario.valueOf(dto.tipo().toUpperCase()))
        .endereco(dto.endereco())
        .build();
  }

  public Usuario toEntityUpdate(UsuarioUpdateDTO dto) {
    return Usuario.builder()
        .nome(dto.nome())
        .email(dto.email())
        .login(dto.login())
        .tipo(TipoUsuario.valueOf(dto.tipo().toUpperCase()))
        .endereco(dto.endereco())
        .build();
  }

  public List<UsuarioResponseDTO> toResponseDTOList(Page<Usuario> usuarios) {
    return usuarios.stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }
}
