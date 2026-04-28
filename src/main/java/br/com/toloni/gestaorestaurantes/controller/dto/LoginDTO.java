package br.com.toloni.gestaorestaurantes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.ToString;

public record LoginDTO(
        @Schema(description = "Login do usuário (único)", example = "joao.silva", minLength = 3, maxLength = 50) @NotBlank(message = "Login é obrigatório") @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres") @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Login pode conter apenas letras, números, pontos, underscores e hífens") String login,

        @Schema(description = "Senha do usuário", example = "senha123", minLength = 6) @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") @ToString.Exclude String senha) {

}
