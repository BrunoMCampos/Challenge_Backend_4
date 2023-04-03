package br.com.alura.financas.domain.resumo;

import br.com.alura.financas.domain.despesa.Categoria;
import br.com.alura.financas.domain.despesa.Despesa;
import br.com.alura.financas.domain.receita.Receita;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
public class ResumoService {

    @SuppressWarnings("unused")
    public DadosResumo gerarResumo(Page<Receita> receitas, Page<Despesa> despesas) {
        BigDecimal valorTotalReceitas = receitas.stream().map(Receita::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorTotalDespesas = despesas.stream().map(Despesa::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoFinal = valorTotalReceitas.subtract(valorTotalDespesas);

        Map<Categoria, List<Despesa>> despesasPorCategoria = despesas.stream().collect(Collectors.groupingBy(Despesa::getCategoria));

        Map<Categoria, BigDecimal> valorGastoPorCategoria = new HashMap<>();
        despesasPorCategoria
                .keySet()
                .forEach(
                        categoria ->
                                valorGastoPorCategoria
                                        .put(
                                                categoria,
                                                despesasPorCategoria
                                                        .get(categoria)
                                                        .stream()
                                                        .map(Despesa::getValor)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add)));

        return new DadosResumo(valorTotalReceitas, valorTotalDespesas, saldoFinal, valorGastoPorCategoria);
    }

}
