package br.com.alura.financas.domain.receita;

import br.com.alura.financas.domain.lancamento.*;
import br.com.alura.financas.validacao.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento cadastrar(DadosCadastrarLancamento dados) {
        Optional<Lancamento> optionalLancamento = lancamentoRepository.findReceitaComMesmaDescricaoNoMesmoMesEAno(dados.descricao(), dados.data());
        if (optionalLancamento.isPresent()) {
            throw new ValidacaoException("Existem receitas com a mesma descrição já cadastradas neste mês.");
        } else {
            Lancamento lancamento = new Lancamento(dados);
            lancamento.setTipo(TipoLancamentoEnum.RECEITA);
            return lancamentoRepository.save(lancamento);
        }
    }

    public DadosDetalharLancamento atualizar(Lancamento lancamento, DadosAlteracaoLancamento dados) {
        if (dados.descricao() != null) {
            lancamento.setDescricao(dados.descricao());
        }
        if(dados.valor() != null){
            lancamento.setValor(dados.valor());
        }
        if(dados.data() != null){
            lancamento.setData(dados.data());
        }
        return new DadosDetalharLancamento(lancamento);
    }
}
