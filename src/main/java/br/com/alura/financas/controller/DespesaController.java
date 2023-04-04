package br.com.alura.financas.controller;

import br.com.alura.financas.domain.despesa.*;
import br.com.alura.financas.infra.exception.DadosErro;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearer-key")
public class DespesaController {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastrarDespesa dados, UriComponentsBuilder uriBuilder) {
        Despesa despesa = this.despesaService.cadastrar(dados);
        if(despesa == null){
            return ResponseEntity.badRequest().body(new DadosErro("Descrição", "Descrição já cadastrada dentro deste mês"));
        }
        URI uri = uriBuilder.path("despesas/{idDespesa}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalharDespesa(despesa));
    }

    @GetMapping("{idDespesa}")
    public ResponseEntity<DadosDetalharDespesa> detalhar(@PathVariable Long idDespesa) {
        Optional<Despesa> optionalDespesa = despesaRepository.findById(idDespesa);
        if(optionalDespesa.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(new DadosDetalharDespesa(optionalDespesa.get()));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemDespesas>> listar(Pageable pageable, @RequestParam(required = false) String descricao) {
        Page<DadosListagemDespesas> despesas;
        if (descricao != null) {
            despesas = despesaRepository.findByDescricaoContaining(pageable, descricao).map(DadosListagemDespesas::new);
        } else {
            despesas = despesaRepository.findAll(pageable).map(DadosListagemDespesas::new);
        }
        return ResponseEntity.ok().body(despesas);
    }

    @GetMapping("{ano}/{mes}")
    public ResponseEntity<Page<DadosListagemDespesas>> listarPorMesEAno(Pageable pageable, @PathVariable Integer ano, @PathVariable Integer mes) {
        Page<DadosListagemDespesas> despesas = despesaRepository.findAllByMesEAno(pageable, ano, mes).map(DadosListagemDespesas::new);
        return ResponseEntity.ok().body(despesas);
    }

    @DeleteMapping("{idDespesa}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long idDespesa) {
        Optional<Despesa> optionalDespesa = despesaRepository.findById(idDespesa);
        if(optionalDespesa.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        despesaRepository.delete(optionalDespesa.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{idDespesa}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long idDespesa, @RequestBody @Valid DadosAlterarDespesa dados) {
        Despesa despesa = despesaRepository.getReferenceById(idDespesa);
        DadosDetalharDespesa despesaAtualizada = despesaService.atualizar(despesa, dados);
        if(despesaAtualizada == null){
            return ResponseEntity.badRequest().body(new DadosErro("Descrição", "Descrição já cadastrada dentro deste mês"));
        }
        return ResponseEntity.ok().body(despesaAtualizada);
    }

}
