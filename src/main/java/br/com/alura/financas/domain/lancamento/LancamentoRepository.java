package br.com.alura.financas.domain.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query("select l from Lancamento l where l.descricao = :descricao and month(l.data) = month(:data) and year(l.data) = year(:data)")
    Optional<Lancamento> findReceitaComMesmaDescricaoNoMesmoMesEAno(String descricao, LocalDate data);

    Page<Lancamento> findAllByTipo(Pageable pageable, TipoLancamentoEnum tipo);

    Optional<Lancamento> findByIdAndTipo(Long id, TipoLancamentoEnum tipo);

    void deleteByIdAndTipo(Long id, TipoLancamentoEnum tipo);
}
