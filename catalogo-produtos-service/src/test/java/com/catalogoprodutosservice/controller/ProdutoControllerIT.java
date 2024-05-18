package com.catalogoprodutosservice.controller;

import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.util.InstanceGeneratorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProdutoControllerIT {
    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void cadastrar() throws Exception {
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO)
                .when()
                .post("/produto")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo(produtoDTO.nome()))
                .body("descricao", equalTo(produtoDTO.descricao()))
                .body("preco", equalTo(Float.parseFloat(produtoDTO.preco().toString())))
                .body("qtdEstoque", equalTo(produtoDTO.qtdEstoque()));
    }

    @Test
    void atualizar() {
        final UUID id = UUID.fromString("7fa6c49e-0ad2-4a1d-95a6-956e1534e3c6");
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO)
                .when()
                .put("/produto/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(produtoDTO.nome()))
                .body("nome", equalTo(produtoDTO.nome()))
                .body("descricao", equalTo(produtoDTO.descricao()))
                .body("preco", equalTo(Float.parseFloat(produtoDTO.preco().toString())))
                .body("qtdEstoque", equalTo(produtoDTO.qtdEstoque()));
    }

    @Test
    void deletar() {
        final UUID id = UUID.fromString("9a1f4dd7-151e-4b33-8a60-30cf5e5f0dd0");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/produto/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void encontrarProdutoPorId() {
        final UUID id = UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c");
        final ProdutoDTO produtoDTO = new ProdutoDTO(UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c"), "Produto 2", "Descrição do Produto 2", 20.75, 150);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produto/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(produtoDTO.nome()))
                .body("nome", equalTo(produtoDTO.nome()))
                .body("descricao", equalTo(produtoDTO.descricao()))
                .body("preco", equalTo(Float.parseFloat(produtoDTO.preco().toString())))
                .body("qtdEstoque", equalTo(produtoDTO.qtdEstoque()));
    }

    @Test
    void listarTodosOsProdutos() throws JsonProcessingException {
        List<ProdutoDTO> produtosDTO = InstanceGeneratorHelper.getProdutosDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produto")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(produtosDTO)));
    }

    @Test
    void decrementarEstoque() {
        final UUID id = UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c");
        final ProdutoDTO produtoDTO = new ProdutoDTO(UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c"), "Produto 2", "Descrição do Produto 2", 20.75, 50);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/produto/{id}/decrementarEstoque/{quantidade}", id, 100)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(produtoDTO.nome()))
                .body("nome", equalTo(produtoDTO.nome()))
                .body("descricao", equalTo(produtoDTO.descricao()))
                .body("preco", equalTo(Float.parseFloat(produtoDTO.preco().toString())))
                .body("qtdEstoque", equalTo(produtoDTO.qtdEstoque()));
    }

    @Test
    void incrementarEstoque() {
        final UUID id = UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c");
        final ProdutoDTO produtoDTO = new ProdutoDTO(UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c"), "Produto 2", "Descrição do Produto 2", 20.75, 250);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/produto/{id}/incrementarEstoque/{quantidade}", id, 100)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(produtoDTO.nome()))
                .body("nome", equalTo(produtoDTO.nome()))
                .body("descricao", equalTo(produtoDTO.descricao()))
                .body("preco", equalTo(Float.parseFloat(produtoDTO.preco().toString())))
                .body("qtdEstoque", equalTo(produtoDTO.qtdEstoque()));
    }
    @Test
    void cadastrarProdutoAlreadyExistsException() {
        final ProdutoDTO produtoDTO = new ProdutoDTO(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "Produto 1", "Descrição do Produto 1", 10.50, 100);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO)
                .when()
                .post("/produto")
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo("O registro [Produto 1] que você está tentando criar já existe na base de dados."))
                .body("path", equalTo("/produto"));
    }

    @Test
    void atualizarProdutoNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO)
                .when()
                .put("/produto/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O registro [66342f0e-24fb-4cea-812f-dffbe915f180] não foi encontrado encontrado."))
                .body("path", equalTo(String.format("/produto/%s", id)));
    }

    @Test
    void deletarProdutoNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/produto/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O registro [66342f0e-24fb-4cea-812f-dffbe915f180] não foi encontrado encontrado."))
                .body("path", equalTo(String.format("/produto/%s", id)));
    }

    @Test
    void decrementarEstoqueProdutoInsuficienteException() {
        final UUID id = UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c");
        final ProdutoDTO produtoDTO = new ProdutoDTO(UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c"), "Produto 2", "Descrição do Produto 2", 20.75, 350);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/produto/{id}/decrementarEstoque/{quantidade}", id, 450)
                .then()
                .log().all()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("message", equalTo("A solicitação não pôde ser processada devido a dados inválidos ou à violação das regras de negócio."))
                .body("path", equalTo(String.format("/produto/%s/decrementarEstoque/%s", id, "450")));
    }
}