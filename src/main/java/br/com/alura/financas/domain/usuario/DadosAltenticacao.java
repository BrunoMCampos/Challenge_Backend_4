package br.com.alura.financas.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAltenticacao(@NotBlank String login,@NotBlank String senha) {
}
