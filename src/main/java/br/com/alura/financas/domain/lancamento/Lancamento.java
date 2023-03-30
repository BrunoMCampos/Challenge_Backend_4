package br.com.alura.financas.domain.lancamento;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private TipoLancamentoEnum tipo;

    public Lancamento(DadosCadastrarLancamento dados) {
        this.descricao = dados.descricao();
        this.valor = dados.valor();
        this.data = dados.data();
    }
}
