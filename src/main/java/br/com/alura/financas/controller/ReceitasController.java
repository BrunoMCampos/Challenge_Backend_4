package br.com.alura.financas.controller;

import br.com.alura.financas.domain.lancamento.*;
import br.com.alura.financas.domain.receita.ReceitaService;
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
@RequestMapping("receitas")
public class ReceitasController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private ReceitaService receita;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalharLancamento> cadastrar(@RequestBody @Valid DadosCadastrarLancamento dados, UriComponentsBuilder uriBuilder){
        Lancamento receita = this.receita.cadastrar(dados);
        URI uri = uriBuilder.path("receitas/{idReceita}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalharLancamento(receita));
    }

    @GetMapping("{idReceita}")
    public ResponseEntity<DadosDetalharLancamento> detalhar(@PathVariable Long idReceita){
        Optional<Lancamento> optionalReceita = lancamentoRepository.findByIdAndTipo(idReceita, TipoLancamentoEnum.RECEITA);
        if(optionalReceita.isEmpty()){
            throw new ValidacaoException("Receita não encontrada.");
        } else {
            return ResponseEntity.ok().body(new DadosDetalharLancamento(optionalReceita.get()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemLancamentos>> listar(Pageable pageable){
        Page<DadosListagemLancamentos> page = lancamentoRepository.findAllByTipo(pageable, TipoLancamentoEnum.RECEITA).map(DadosListagemLancamentos::new);
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("{idReceita}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long idReceita){
        Optional<Lancamento> optionalReceita = lancamentoRepository.findByIdAndTipo(idReceita, TipoLancamentoEnum.RECEITA);
        if(optionalReceita.isEmpty()){
            throw new ValidacaoException("Receita não encontrada");
        }
        lancamentoRepository.delete(optionalReceita.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{idReceita}")
    @Transactional
    public ResponseEntity<DadosDetalharLancamento> atualizar(@PathVariable Long idReceita, @RequestBody @Valid DadosAlteracaoLancamento dados){
        Optional<Lancamento> optionalReceita = lancamentoRepository.findByIdAndTipo(idReceita, TipoLancamentoEnum.RECEITA);
        if(optionalReceita.isEmpty()){
            throw new ValidacaoException("Receita não encontrada");
        }
        DadosDetalharLancamento lancamentoAtualizado = receita.atualizar(optionalReceita.get(), dados);
        return ResponseEntity.ok().body(lancamentoAtualizado);
    }

}
