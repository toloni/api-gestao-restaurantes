package br.com.toloni.gestaorestaurantes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta de um usuário")
public record UsuarioResponseDTO(
        @Schema(description = "ID do usuário", example = "1") Long id,
        @Schema(description = "Nome completo do usuário", example = "João Silva") String nome,

        @Schema(description = "Email do usuário", example = "joao.silva@example.com") String email,

        @Schema(description = "Login do usuário", example = "joao.silva") String login,

        @Schema(description = "Tipo do usuário", example = "CLIENTE", allowableValues = {
                "CLIENTE", "DONO_RESTAURANTE" }) String tipo,

        @Schema(description = "Endereço completo do usuário", example = "Rua Exemplo, 123 - Bairro, Cidade - UF") String endereco) {

}
