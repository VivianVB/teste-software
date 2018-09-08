package br.ucdb.pos.engenhariasoftware.testesoftware.controller;

import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToMoneyConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertEquals;

public class LancamentoControllerTest {

    /*
        Configuração do endereço
    */
    @BeforeTest
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    /*
        Inserção de 1 lançamento na lista
    */
    @BeforeMethod(groups = "Teste 1")
    public void salvarTest(){
        Response response = given().when()
                .formParam("descricao", "Assured Rest VVB 1")
                .formParam("valor", "50,00")
                .formParam("dataLancamento", "03/04/2018")
                .formParam("tipoLancamento", "SAIDA")
                .formParam("categoria", "LAZER")
                .header("Content-Type", "application/x-www-form-urlencoded") // opcional
                .post("/salvar");

        assertEquals(response.getStatusCode(), 302, "Erro ao salvar lançamento Assured Rest VVB 1");
    }

    /*
        Busca de lançamento, tamanho 1
    */
    @Test(groups = "Teste 1")
    public void buscarTamanhoUm() {
        Response response = given()
                .when()
                .body("Assured")
                .post("/buscaLancamentos");

        assertEquals(response.getStatusCode(), 200, "Erro ao buscar lançamento Assured Rest VVB 1");

        InputStream in = response.asInputStream();
        List<Lancamento> list = JsonPath.with(in).getList("lancamentos", Lancamento.class);
        assertEquals(list.size(), 1);
    }

    /*
        Remoção de lançamento, tamanho 1
    */
    @AfterMethod(groups = "Teste 1")
    public void removerTest(){

        Response response1 = given()
                .when()
                .body("Assured")
                .post("/buscaLancamentos");
        InputStream in1 = response1.asInputStream();
        String meuid = JsonPath.with(in1).getString("lancamentos.id");
        meuid = meuid.replace("[","").replace("]","");

        Response response = given()/*.pathParam("id", b)*/
                .when().get("/remover/"+meuid);
        //assertEquals(response.getStatusCode(), 200);
        //assertEquals("Lançamentos",response.body().htmlPath()
        //   .getString("html.body.div.div.div.div.div.h4").toString());

    }

}


