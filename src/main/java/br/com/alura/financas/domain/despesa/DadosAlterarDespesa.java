package br.com.alura.financas.domain.despesa;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAlterarDespesa(
        @Length(min = 1, max = 255)
        String descricao,
        BigDecimal valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,
        Categoria categoria) {
}
