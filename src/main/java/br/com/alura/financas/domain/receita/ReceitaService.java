package br.com.alura.financas.domain.receita;

import br.com.alura.financas.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    public Receita cadastrar(DadosCadastrarReceita dados) {
        Optional<Receita> optionalReceita = receitaRepository.findReceitaComMesmaDescricaoNoMesmoMesEAno(dados.descricao(), dados.data());
        if (optionalReceita.isPresent()) {
            throw new ValidacaoException("Já existe uma receita com a mesma descrição cadastrada neste mês.");
        } else {
            Receita receita = new Receita(dados);
            return receitaRepository.save(receita);
        }
    }

    public DadosDetalharReceita atualizar(Receita receita, DadosAlterarReceita dados) {
        if (dados.descricao() != null) {
            receita.setDescricao(dados.descricao());
        }
        if(dados.valor() != null){
            receita.setValor(dados.valor());
        }
        if(dados.data() != null){
            receita.setData(dados.data());
        }
        return new DadosDetalharReceita(receita);
    }
}
