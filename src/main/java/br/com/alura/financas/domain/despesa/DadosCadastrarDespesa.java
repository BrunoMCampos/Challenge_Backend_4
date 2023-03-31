package br.com.alura.financas.domain.despesa;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastrarDespesa(
        @NotBlank
        @Length(min = 1, max = 255)
        String descricao,
        @NotNull
        BigDecimal valor,
        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,
        Categoria categoria) {
}
