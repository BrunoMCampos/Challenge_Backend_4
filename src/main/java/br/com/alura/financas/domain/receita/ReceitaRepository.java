package br.com.alura.financas.domain.receita;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query("select r from Receita r where r.descricao = :descricao and month(r.data) = month(:data) and year(r.data) = year(:data)")
    Optional<Receita> findReceitaComMesmaDescricaoNoMesmoMesEAno(String descricao, LocalDate data);

    Page<Receita> findByDescricaoContaining(Pageable pageable, String descricao);

    @Query("select r from Receita r where month(r.data) = :mes and year(r.data) = :ano")
    Page<Receita> findAllByMesEAno(Pageable pageable, Integer ano, Integer mes);

    Optional<Receita> findByDescricao(String descricao);
}
