package br.com.alura.financas.domain.despesa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SuppressWarnings("unused")
public interface DespesaRepository extends JpaRepository<Despesa,Long> {

    @Query("select d from Despesa d where d.descricao = :descricao and month(d.data) = month(:data) and year(d.data) = year(:data)")
    Optional<Despesa> findDespesaComMesmaDescricaoNoMesmoMesEAno(String descricao, LocalDateTime data);

    Page<Despesa> findByDescricaoContaining(Pageable pageable, String descricao);

    @Query("select d from Despesa d where month(d.data) = :mes and year(d.data) = :ano")
    Page<Despesa> findAllByMesEAno(Pageable pageable, Integer ano, Integer mes);

    Optional<Despesa> findByDescricao(String descricao);
}
