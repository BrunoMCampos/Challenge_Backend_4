package br.com.alura.financas.controller;

import br.com.alura.financas.domain.receita.*;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@WithMockUser
class ReceitaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastrarReceita> dadosCadastrarReceitaJson;
    @Autowired
    private JacksonTester<DadosDetalharReceita> dadosDetalharReceitaJson;
    @Autowired
    private JacksonTester<DadosListagemReceitas> dadosListarReceitaJson;
    @Autowired
    private JacksonTester<DadosAlterarReceita> dadosAlterarReceitaJson;

    @Autowired
    private ReceitaRepository receitaRepository;

    private final Receita receitaCompleta = new Receita(null, "Receita", new BigDecimal(100), LocalDate.now());
    private final Receita receitaCompletaVenda = new Receita(null, "Venda", new BigDecimal(220), LocalDate.now());
    private final Receita receitaCompletaEntrada = new Receita(null, "Entrada", new BigDecimal(330), LocalDate.now());

    private MockHttpServletResponse cadastrarReceita(Receita receita) throws Exception {
        return mvc.perform(
                        post("/receitas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        dadosCadastrarReceitaJson.write(
                                                new DadosCadastrarReceita(
                                                        receita.getDescricao(),
                                                        receita.getValor(),
                                                        receita.getData())).getJson()
                                )).andReturn()
                .getResponse();
    }

    private MockHttpServletResponse alterarReceita(String url, Receita receita) throws Exception {
        return mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAlterarReceitaJson.write(
                                new DadosAlterarReceita(
                                        receita.getDescricao(),
                                        receita.getValor(),
                                        receita.getData()
                                )).getJson()
                        )).andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando não receber json")
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/receitas")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem data")
    void cadastrarCenario2() throws Exception {
        receitaCompleta.setData(null);
        MockHttpServletResponse response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem descrição")
    void cadastrarCenario3() throws Exception {
        receitaCompleta.setDescricao(null);
        MockHttpServletResponse response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json sem valor")
    void cadastrarCenario4() throws Exception {
        receitaCompleta.setValor(null);
        MockHttpServletResponse response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando recebendo json válido")
    void cadastrarCenario5() throws Exception {
        MockHttpServletResponse response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo json válido, porém com a mesma descrição já cadastrada no mês")
    void cadastrarCenario6() throws Exception {
        MockHttpServletResponse response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        receitaCompleta.setValor(new BigDecimal(4521));

        response = cadastrarReceita(receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inexistente")
    void detalharCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/receitas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com os dados quando recebendo um id existente")
    void detalharCenario2() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitas.get(0))).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }


    @Test
    @DisplayName("Deveria devolver código http 200 e o json  com os 3 registros cadastrados")
    void listarCenario1() throws Exception {
        cadastrarReceita(receitaCompleta);
        cadastrarReceita(receitaCompletaEntrada);
        cadastrarReceita(receitaCompletaVenda);

        MockHttpServletResponse response = mvc.perform(get("/receitas")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaEntrada)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaVenda)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json apenas com os registros correspondentes a descricao informada")
    void listarCenario2() throws Exception {
        cadastrarReceita(receitaCompleta);
        cadastrarReceita(receitaCompletaEntrada);
        cadastrarReceita(receitaCompletaVenda);

        MockHttpServletResponse response = mvc.perform(get("/receitas?descricao=da")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).doesNotContain(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaEntrada)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaVenda)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json apenas com as despesas daquele mês")
    void listarPorMesEAnoCenario1() throws Exception {
        receitaCompletaEntrada.setData(LocalDate.now().plus(1L, ChronoUnit.MONTHS));
        cadastrarReceita(receitaCompletaEntrada);

        cadastrarReceita(receitaCompleta);
        cadastrarReceita(receitaCompletaVenda);

        String url = "/receitas/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue();

        MockHttpServletResponse response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaEntrada)).getJson();
        assertThat(response.getContentAsString()).doesNotContain(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);

        jsonEsperado = dadosListarReceitaJson.write(new DadosListagemReceitas(receitaCompletaVenda)).getJson();
        assertThat(response.getContentAsString()).contains(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inválido")
    void deletarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/receitas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json quando recebendo um id válido")
    void deletarCenario2() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(delete(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        response = mvc.perform(get(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id inválido")
    void atualizarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/receitas/99999")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com todas as informações alteradas")
    void atualizarCenario2() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        MockHttpServletResponse response = alterarReceita(url, receitaCompletaEntrada);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitaCompletaEntrada)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a descrição alterada")
    void atualizarCenario3() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        receitaCompleta.setDescricao("Receita Alterada");

        MockHttpServletResponse response = alterarReceita(url, receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a valor alterada")
    void atualizarCenario4() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        receitaCompleta.setValor(new BigDecimal(1000));

        MockHttpServletResponse response = alterarReceita(url, receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e o json com a apenas a data alterada")
    void atualizarCenario5() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        receitaCompleta.setData(LocalDate.now().plus(1L,ChronoUnit.MONTHS));

        MockHttpServletResponse response = alterarReceita(url, receitaCompleta);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalharReceitaJson.write(new DadosDetalharReceita(receitaCompleta)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando recebendo um id válido, porém sem o json")
    void atualizarCenario6() throws Exception {
        cadastrarReceita(receitaCompleta);

        List<Receita> receitas = receitaRepository.findAll();
        String url = "/receitas/" + receitas.get(0).getId();

        MockHttpServletResponse response = mvc.perform(put(url)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}