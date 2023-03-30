package br.com.alura.financas.domain.lancamento;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosListagemLancamentos(String descricao, BigDecimal valor,
                                       @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data) {
    public DadosListagemLancamentos(Lancamento lancamento) {
        this(lancamento.getDescricao(), lancamento.getValor(), lancamento.getData());
    }
}
