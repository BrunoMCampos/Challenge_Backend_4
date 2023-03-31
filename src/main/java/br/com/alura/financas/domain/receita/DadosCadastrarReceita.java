package br.com.alura.financas.domain.receita;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastrarReceita(
        @NotBlank
        @Length(min = 1, max = 255)
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data
) {
}
