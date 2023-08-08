package br.com.alura.financas.domain.receita;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DadosAlterarReceita(
        @Length(min = 1, max = 255)
        String descricao,
        BigDecimal valor,
        LocalDateTime data) {
}
