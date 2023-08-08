package br.com.alura.financas.controller;

import br.com.alura.financas.domain.despesa.Categoria;
import br.com.alura.financas.domain.despesa.DadosCadastrarDespesa;
import br.com.alura.financas.domain.despesa.Despesa;
import br.com.alura.financas.domain.receita.DadosCadastrarReceita;
import br.com.alura.financas.domain.receita.Receita;
import br.com.alura.financas.domain.resumo.DadosResumo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@Transactional
@WithMockUser
class ResumoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosResumo> dadosResumoJson;

    @Autowired
    private JacksonTester<DadosCadastrarReceita> dadosCadastrarReceitaJson;

    @Autowired
    private JacksonTester<DadosCadastrarDespesa> dadosCadastrarDespesaJson;

    private final Despesa despesaMoradiaDentroDoMes = new Despesa(null, "Despesa Moradia",new BigDecimal(1200), LocalDateTime.now(), Categoria.MORADIA);
    private final Despesa despesaSaudeDentroDoMes = new Despesa(null, "Despesa Saude",new BigDecimal(600), LocalDateTime.now(), Categoria.SAUDE);
    private final Despesa despesaOutrasForaDoMes = new Despesa(null, "Despesa Outras",new BigDecimal(200), LocalDateTime.now().plus(1L, ChronoUnit.MONTHS), Categoria.OUTRAS);

    private final Receita receitaVendaDentroDoMes = new Receita(null, "Receita Venda", new BigDecimal(1500), LocalDateTime.now());
    private final Receita receitaSalarioDentroDoMes = new Receita(null, "Receita Salario", new BigDecimal(2500), LocalDateTime.now());
    private final Receita receitaAluguelForaDoMes = new Receita(null, "Receita Aluguel", new BigDecimal(1000), LocalDateTime.now().plus(1L,ChronoUnit.MONTHS));

    private void cadastrarReceita(Receita receita) throws Exception {
        mvc.perform(
                        post("/receitas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        dadosCadastrarReceitaJson
                                                .write(
                                                        new DadosCadastrarReceita(
                                                                receita.getDescricao(),
                                                                receita.getValor(),
                                                                receita.getData()
                                                        )
                                                ).getJson()
                                ))
                .andReturn()
                .getResponse();
    }

    private void cadastrarDespesa(Despesa despesa) throws Exception {
        mvc.perform(
                        post("/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        dadosCadastrarDespesaJson
                                                .write(
                                                        new DadosCadastrarDespesa(
                                                                despesa.getDescricao(),
                                                                despesa.getValor(),
                                                                despesa.getData(),
                                                                despesa.getCategoria()
                                                        )
                                                ).getJson()
                                ))
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e json com valores corretos")
    void resumoMensal() throws Exception {
        cadastrarReceita(receitaVendaDentroDoMes);
        cadastrarReceita(receitaSalarioDentroDoMes);
        cadastrarReceita(receitaAluguelForaDoMes);

        cadastrarDespesa(despesaMoradiaDentroDoMes);
        cadastrarDespesa(despesaOutrasForaDoMes);
        cadastrarDespesa(despesaSaudeDentroDoMes);

        String url = "/resumo/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonth().getValue();

        MockHttpServletResponse response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Map<Categoria,BigDecimal> valorGastoPorCategoria = new HashMap<>();

        valorGastoPorCategoria.put(Categoria.MORADIA, new BigDecimal(1200));
        valorGastoPorCategoria.put(Categoria.SAUDE, new BigDecimal(600));

        String jsonEsperado = dadosResumoJson.write(
                new DadosResumo(
                        new BigDecimal(4000),
                        new BigDecimal(1800),
                        new BigDecimal(2200),
                        valorGastoPorCategoria
                )
        ).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }


}