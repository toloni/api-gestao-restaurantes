package br.com.toloni.gestaorestaurantes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.ToString;

@Schema(description = "Dados para criação de um novo usuário")
public record UsuarioCreateDTO(
        @Schema(description = "Nome completo do usuário", example = "João Silva", minLength = 1, maxLength = 100) @NotBlank(message = "Nome é obrigatório") @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres") String nome,

        @Schema(description = "Email do usuário (único)", example = "joao.silva@example.com") @NotBlank(message = "Email é obrigatório") @Email(message = "Email deve ter formato válido") String email,

        @Schema(description = "Login do usuário (único)", example = "joao.silva", minLength = 3, maxLength = 50) @NotBlank(message = "Login é obrigatório") @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres") @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Login pode conter apenas letras, números, pontos, underscores e hífens") String login,

        @Schema(description = "Senha do usuário", example = "senha123", minLength = 6) @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") @ToString.Exclude String senha,

        @Schema(description = "Tipo do usuário", example = "CLIENTE", allowableValues = {
                "CLIENTE",
                "DONO_RESTAURANTE" }) @NotBlank(message = "Tipo é obrigatório") @Pattern(regexp = "^(CLIENTE|DONO_RESTAURANTE)$", message = "Tipo deve ser CLIENTE ou DONO_RESTAURANTE") String tipo,

        @Schema(description = "Endereço completo do usuário", example = "Rua Exemplo, 123 - Bairro, Cidade - UF", minLength = 10) @NotBlank(message = "Endereço é obrigatório") @Size(min = 10, max = 200, message = "Endereço deve ter entre 10 e 200 caracteres") String endereco) {
}
