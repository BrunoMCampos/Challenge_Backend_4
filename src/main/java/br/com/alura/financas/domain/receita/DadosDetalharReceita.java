package br.com.alura.financas.domain.receita;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalharReceita(String descricao, BigDecimal valor, @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data) {
    public DadosDetalharReceita(Receita receita) {
        this(
                receita.getDescricao(),
                receita.getValor(),
                receita.getData()
        );
    }
}
