package br.com.alura.financas.domain.despesa;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosListagemDespesas(
        String descricao,
        BigDecimal valor,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,

        Categoria categoria) {

    public DadosListagemDespesas(Despesa despesa){
        this(despesa.getDescricao(),despesa.getValor(),despesa.getData(),despesa.getCategoria());
    }
}
