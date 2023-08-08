package br.com.alura.financas.domain.receita;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DadosDetalharReceita(String descricao, BigDecimal valor, LocalDateTime data) {
    public DadosDetalharReceita(Receita receita) {
        this(
                receita.getDescricao(),
                receita.getValor(),
                receita.getData()
        );
    }
}
