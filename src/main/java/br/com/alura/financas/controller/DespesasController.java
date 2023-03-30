package br.com.alura.financas.controller;

import br.com.alura.financas.domain.despesa.DespesaService;
import br.com.alura.financas.domain.lancamento.*;
import br.com.alura.financas.validacao.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("despesas")
public class DespesasController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private DespesaService despesa;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalharLancamento> cadastrar(@RequestBody @Valid DadosCadastrarLancamento dados, UriComponentsBuilder uriBuilder){
        Lancamento despesa = this.despesa.cadastrar(dados);
        URI uri = uriBuilder.path("despesas/{idDespesa}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalharLancamento(despesa));
    }

    @GetMapping("{idDespesa}")
    public ResponseEntity<DadosDetalharLancamento> detalhar(@PathVariable Long idDespesa){
        Optional<Lancamento> optionalLancamento = lancamentoRepository.findByIdAndTipo(idDespesa, TipoLancamentoEnum.DESPESA);
        if(optionalLancamento.isEmpty()){
            throw new ValidacaoException("Despesa não encontrada.");
        } else {
            return ResponseEntity.ok().body(new DadosDetalharLancamento(optionalLancamento.get()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemLancamentos>> listar(Pageable pageable){
        Page<DadosListagemLancamentos> page = lancamentoRepository.findAllByTipo(pageable, TipoLancamentoEnum.DESPESA).map(DadosListagemLancamentos::new);
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("{idDespesa}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long idDespesa){
        Optional<Lancamento> optionalLancamento = lancamentoRepository.findByIdAndTipo(idDespesa, TipoLancamentoEnum.DESPESA);
        if(optionalLancamento.isEmpty()){
            throw new ValidacaoException("Despesa não encontrada.");
        }
        lancamentoRepository.delete(optionalLancamento.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{idDespesa}")
    @Transactional
    public ResponseEntity<DadosDetalharLancamento> atualizar(@PathVariable Long idDespesa, @RequestBody @Valid DadosAlteracaoLancamento dados){
        Optional<Lancamento> optionalLancamento = lancamentoRepository.findByIdAndTipo(idDespesa, TipoLancamentoEnum.DESPESA);
        if(optionalLancamento.isEmpty()){
            throw new ValidacaoException("Despesa não encontrada");
        }
        DadosDetalharLancamento lancamentoAtualizado = despesa.atualizar(optionalLancamento.get(), dados);
        return ResponseEntity.ok().body(lancamentoAtualizado);
    }

}
