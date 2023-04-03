package br.com.alura.financas.controller;

import br.com.alura.financas.domain.receita.*;
import br.com.alura.financas.infra.exception.DadosErro;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("receitas")
public class ReceitaController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastrarReceita dados, UriComponentsBuilder uriBuilder) {
        Receita receita = this.receitaService.cadastrar(dados);
        if(receita == null){
            return ResponseEntity.badRequest().body(new DadosErro("Descrição", "Descrição já cadastrada dentro deste mês"));
        }
        URI uri = uriBuilder.path("receitas/{idReceita}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalharReceita(receita));
    }

    @GetMapping("{idReceita}")
    public ResponseEntity<DadosDetalharReceita> detalhar(@PathVariable Long idReceita) {
        Receita receita = receitaRepository.getReferenceById(idReceita);
        return ResponseEntity.ok().body(new DadosDetalharReceita(receita));
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
        Receita receita = receitaRepository.getReferenceById(idReceita);
        receitaRepository.delete(receita);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{idReceita}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long idReceita, @RequestBody @Valid DadosAlterarReceita dados) {
        Receita receita = receitaRepository.getReferenceById(idReceita);
        DadosDetalharReceita receitaAtualizada = receitaService.atualizar(receita, dados);
        if(receitaAtualizada == null){
            return ResponseEntity.badRequest().body(new DadosErro("Descrição", "Descrição já cadastrada dentro deste mês"));
        }
        return ResponseEntity.ok().body(receitaAtualizada);
    }

}
