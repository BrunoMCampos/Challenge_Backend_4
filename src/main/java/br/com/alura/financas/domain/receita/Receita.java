package br.com.alura.financas.domain.receita;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Receita")
@Table(name = "receitas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    public Receita(DadosCadastrarReceita dados) {
        this.descricao = dados.descricao();
        this.data = dados.data();
        this.valor = dados.valor();
    }
}
