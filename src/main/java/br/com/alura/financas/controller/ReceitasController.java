package br.com.alura.financas.controller;

import br.com.alura.financas.domain.receita.*;
import br.com.alura.financas.infra.exception.ValidacaoException;
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
    private ReceitaRepository receitaRepository;

    @Autowired
    private ReceitaService receita;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalharReceita> cadastrar(@RequestBody @Valid DadosCadastrarReceita dados, UriComponentsBuilder uriBuilder) {
        Receita receita = this.receita.cadastrar(dados);
        URI uri = uriBuilder.path("receitas/{idReceita}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalharReceita(receita));
    }

    @GetMapping("{idReceita}")
    public ResponseEntity<DadosDetalharReceita> detalhar(@PathVariable Long idReceita) {
        Optional<Receita> optionalReceita = receitaRepository.findById(idReceita);
        if (optionalReceita.isEmpty()) {
            throw new ValidacaoException("Receita não encontrada.");
        } else {
            return ResponseEntity.ok().body(new DadosDetalharReceita(optionalReceita.get()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemReceitas>> listar(Pageable pageable, @RequestParam(required = false) String descricao) {
        Page<DadosListagemReceitas> receitas;
        if (descricao != null) {
            receitas = receitaRepository.findByDescricaoContaining(pageable, descricao).map(DadosListagemReceitas::new);
        } else {
            receitas = receitaRepository.findAll(pageable).map(DadosListagemReceitas::new);
        }
        return ResponseEntity.ok().body(receitas);
    }

    @GetMapping("{ano}/{mes}")
    public ResponseEntity<Page<DadosListagemReceitas>> listarPorMesEAno(Pageable pageable,@PathVariable Integer ano, @PathVariable Integer mes){
        Page<DadosListagemReceitas> receitas = receitaRepository.findAllByMesEAno(pageable, ano, mes).map(DadosListagemReceitas::new);
        return ResponseEntity.ok().body(receitas);
    }

    @DeleteMapping("{idReceita}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long idReceita) {
        Optional<Receita> optionalReceita = receitaRepository.findById(idReceita);
        if (optionalReceita.isEmpty()) {
            throw new ValidacaoException("Receita não encontrada");
        }
        receitaRepository.delete(optionalReceita.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{idReceita}")
    @Transactional
    public ResponseEntity<DadosDetalharReceita> atualizar(@PathVariable Long idReceita, @RequestBody @Valid DadosAlterarReceita dados) {
        Optional<Receita> optionalReceita = receitaRepository.findById(idReceita);
        if (optionalReceita.isEmpty()) {
            throw new ValidacaoException("Receita não encontrada");
        }
        DadosDetalharReceita lancamentoAtualizado = receita.atualizar(optionalReceita.get(), dados);
        return ResponseEntity.ok().body(lancamentoAtualizado);
    }

}
