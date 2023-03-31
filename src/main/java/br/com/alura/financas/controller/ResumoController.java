package br.com.alura.financas.controller;

import br.com.alura.financas.domain.despesa.Despesa;
import br.com.alura.financas.domain.despesa.DespesaRepository;
import br.com.alura.financas.domain.receita.Receita;
import br.com.alura.financas.domain.receita.ReceitaRepository;
import br.com.alura.financas.domain.resumo.DadosResumo;
import br.com.alura.financas.domain.resumo.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResumoController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ResumoService resumo;

    @GetMapping("resumo/{ano}/{mes}")
    public ResponseEntity<DadosResumo> resumoMensal(Pageable pageable, @PathVariable Integer ano, @PathVariable Integer mes) {
        Page<Receita> receitas = receitaRepository.findAllByMesEAno(pageable, ano, mes);
        Page<Despesa> despesas = despesaRepository.findAllByMesEAno(pageable, ano, mes);

        DadosResumo dadosResumo = resumo.gerarResumo(receitas, despesas);

        return ResponseEntity.ok().body(dadosResumo);
    }
}
