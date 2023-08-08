package br.com.alura.financas.domain.despesa;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DadosDetalharDespesa(String descricao, BigDecimal valor, LocalDateTime data, Categoria categoria) {
    public DadosDetalharDespesa(Despesa despesa) {
        this(
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria()
        );
    }
}
