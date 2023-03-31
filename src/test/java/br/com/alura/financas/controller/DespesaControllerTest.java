package br.com.alura.financas.controller;

import br.com.alura.financas.domain.despesa.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@Transactional
class DespesaControllerTest {

    private final Despesa despesaEducacao = new Despesa(null, "Despesa Educacao", new BigDecimal(100), LocalDate.now(), Categoria.EDUCACAO);
    private final Despesa despesaSemCategoria = new Despesa(null, "Despesa sem Categoria", new BigDecimal(100), LocalDate.now(), null);
    private final Despesa despesaAlimentacao = new Despesa(null, "Despesa Alimentacao", new BigDecimal(200), LocalDate.now(), Categoria.ALIMENTACAO);

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosCadastrarDespesa> dadosCadastrarDespesaJson;
    @Autowired
    private JacksonTester<DadosDetalharDespesa> dadosDetalharDespesaJson;
    @Autowired
    private JacksonTester<DadosListagemDespesas> dadosListarDespesaJson;
    @Autowired
    private JacksonTester<DadosAlterarDespesa> dadosAlterarDespesaJson;

    @Autowired
    private DespesaRepository despesaRepository;

    private MockHttpServletResponse cadastrarDespesa(Despesa despesa) throws Exception {
        return mvc.perform(post("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                dadosCadastrarDespesaJson.write(
                                                new DadosCadastrarDespesa(
                                                        despesa.getDescricao(),
                                                        despesa.getValor(),
                                                        despesa.getData(),
                                                        despesa.getCategoria()))
                                        .getJson()))
                .andReturn()
                .getResponse();
    }

    private MockHttpServletResponse alterarDespesa(String url, Despesa despesa) throws Exception {
        return mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAlterarDespesaJson.write(
                                new DadosAlterarDespesa(
                                        despesa.getDescricao(),
                                        despesa.getValor(),
                                        despesa.getData(),
                                        despesa.getCategoria()
                                )).getJson()
                        )).andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando não recebendo json")
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/despesas"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem data")
    void cadastrarCenario2() throws Exception {
        despesaEducacao.setData(null);
        MockHttpServletResponse response = cadastrarDespesa(despesaEducacao);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem descrição")
    void cadastrarCenario3() throws Exception {
        despesaEducacao.setDescricao(null);
        MockHttpServletResponse response = cadastrarDespesa(despesaEducacao);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem valor")
    void cadastrarCenario4() throws Exception {
        despesaEducacao.setValor(null);
        MockHttpServletResponse response = cadastrarDespesa(despesaEducacao);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando recebendo informações válidas")
    void cadastrarCenario5() throws Exception {
        MockHttpServletResponse response = cadastrarDespesa(despesaEducacao);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaEducacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando recebendo informações válidas e sem categoria, cadastrando a categoria como outras")
    void cadastrarCenario6() throws Exception {
        MockHttpServletResponse response = cadastrarDespesa(despesaSemCategoria);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        despesaSemCategoria.setCategoria(Categoria.OUTRAS);

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaSemCategoria)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo informações válidas, porém com descrição já cadastrada no mesmo mês")
    void cadastrarCenario7() throws Exception {
        MockHttpServletResponse response = cadastrarDespesa(despesaSemCategoria);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        despesaSemCategoria.setCategoria(Categoria.OUTRAS);

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaSemCategoria)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

        despesaSemCategoria.setCategoria(null);

        response = cadastrarDespesa(despesaSemCategoria);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inexistente")
    void detalharCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/despesas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com os dados quando recebendo um id existente")
    void detalharCenario2() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesas.get(0))).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json  com os 3 registros cadastrados")
    void listarCenario1() throws Exception {
        cadastrarDespesa(despesaEducacao);
        cadastrarDespesa(despesaSemCategoria);
        cadastrarDespesa(despesaAlimentacao);

        MockHttpServletResponse response = mvc.perform(get("/despesas")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaEducacao)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        despesaSemCategoria.setCategoria(Categoria.OUTRAS);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaSemCategoria)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json apenas com os registros correspondentes a descricao informada")
    void listarCenario2() throws Exception {
        cadastrarDespesa(despesaEducacao);
        cadastrarDespesa(despesaSemCategoria);
        cadastrarDespesa(despesaAlimentacao);

        MockHttpServletResponse response = mvc.perform(get("/despesas?descricao=sem")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaEducacao)).getJson();
        assertThat(response.getContentAsString()).doesNotContain(jsonEsperado);

        despesaSemCategoria.setCategoria(Categoria.OUTRAS);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaSemCategoria)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).doesNotContain(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json apenas com as despesas daquele mês")
    void listarPorMesEAnoCenario1() throws Exception {
        despesaEducacao.setData(LocalDate.now().plus(1L, ChronoUnit.MONTHS));
        cadastrarDespesa(despesaEducacao);

        cadastrarDespesa(despesaSemCategoria);
        cadastrarDespesa(despesaAlimentacao);

        String url = "/despesas/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue();

        MockHttpServletResponse response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaEducacao)).getJson();
        assertThat(response.getContentAsString()).doesNotContain(jsonEsperado);

        despesaSemCategoria.setCategoria(Categoria.OUTRAS);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaSemCategoria)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarDespesaJson.write(new DadosListagemDespesas(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inválido")
    void deletarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/despesas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json quando recebendo um id válido")
    void deletarCenario2() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(delete(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inválido")
    void atualizarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/despesas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com todas as informações alteradas")
    void atualizarCenario2() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        MockHttpServletResponse response = alterarDespesa(url, despesaEducacao);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaEducacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a descrição alterada")
    void atualizarCenario3() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        despesaAlimentacao.setDescricao("Teste");

        MockHttpServletResponse response = alterarDespesa(url, despesaAlimentacao);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a valor alterada")
    void atualizarCenario4() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        despesaAlimentacao.setValor(new BigDecimal(333));

        MockHttpServletResponse response = alterarDespesa(url, despesaAlimentacao);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a data alterada")
    void atualizarCenario5() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        despesaAlimentacao.setData(LocalDate.now().plus(1L,ChronoUnit.MONTHS));

        MockHttpServletResponse response = alterarDespesa(url, despesaAlimentacao);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a categoria alterada")
    void atualizarCenario6() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        despesaAlimentacao.setCategoria(Categoria.SAUDE);

        MockHttpServletResponse response = alterarDespesa(url, despesaAlimentacao);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharDespesaJson.write(new DadosDetalharDespesa(despesaAlimentacao)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id válido, porém sem o json")
    void atualizarCenario7() throws Exception {
        cadastrarDespesa(despesaAlimentacao);

        List<Despesa> despesas = despesaRepository.findAll();
        String url = "/despesas/" + despesas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(put(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}