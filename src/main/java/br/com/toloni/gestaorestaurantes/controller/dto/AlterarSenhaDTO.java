package br.com.toloni.gestaorestaurantes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaDTO(
        @Schema(description = "Senha Atual do usuário", example = "senha123", minLength = 6) @NotBlank(message = "Senha Atual é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senhaAtual,
        @Schema(description = "Nova Senha do usuário", example = "novaSenha123", minLength = 6) @NotBlank(message = "Nova Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String novaSenha) {

}
