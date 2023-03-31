package br.com.alura.financas.domain.resumo;

import br.com.alura.financas.domain.despesa.Categoria;

import java.math.BigDecimal;
import java.util.Map;

public record DadosResumo(
        BigDecimal valorTotalReceitas,
        BigDecimal valorTotalDespesas,
        BigDecimal saldoFinal,
        Map<Categoria, BigDecimal> valorGastoPorCategoria
) {
}
