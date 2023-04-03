package br.com.alura.financas.domain.despesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public Despesa cadastrar(DadosCadastrarDespesa dados) {
        Optional<Despesa> optionalDespesa = despesaRepository.findDespesaComMesmaDescricaoNoMesmoMesEAno(dados.descricao(), dados.data());
        if (optionalDespesa.isPresent()) {
            return null;
        } else {
            Despesa despesa = new Despesa(dados);
            return despesaRepository.save(despesa);
        }
    }

    public DadosDetalharDespesa atualizar(Despesa despesa, DadosAlterarDespesa dados) {
        if (dados.descricao() != null && !dados.descricao().equals(despesa.getDescricao())) {
            Optional<Despesa> optionalDespesa = despesaRepository.findById(despesa.getId());
            if (optionalDespesa.isPresent()) {
                return null;
            } else {
                despesa.setDescricao(dados.descricao());
            }
        }
        if (dados.valor() != null) {
            despesa.setValor(dados.valor());
        }
        if (dados.data() != null) {
            despesa.setData(dados.data());
        }
        if (dados.categoria() != null) {
            despesa.setCategoria(dados.categoria());
        }
        return new DadosDetalharDespesa(despesa);
    }
}
