package br.com.alura.financas.domain.receita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @SuppressWarnings("unused")
    public Receita cadastrar(DadosCadastrarReceita dados) {
        Optional<Receita> optionalReceita = receitaRepository.findReceitaComMesmaDescricaoNoMesmoMesEAno(dados.descricao(), dados.data());
        if (optionalReceita.isPresent()) {
            return null;
        } else {
            Receita receita = new Receita(dados);
            return receitaRepository.save(receita);
        }
    }

    @SuppressWarnings("unused")
    public DadosDetalharReceita atualizar(Receita receita, DadosAlterarReceita dados) {
        if (dados.descricao() != null && !dados.descricao().equals(receita.getDescricao())) {
            Optional<Receita> optionalReceita = receitaRepository.findByDescricao(dados.descricao());
            if(optionalReceita.isPresent()){
                return null;
            }
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
