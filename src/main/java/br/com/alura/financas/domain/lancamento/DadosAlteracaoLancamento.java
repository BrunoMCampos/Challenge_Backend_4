package br.com.alura.financas.domain.lancamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAlteracaoLancamento(
        @Length(min = 5, max = 255)
        String descricao,
        BigDecimal valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data) {
}
