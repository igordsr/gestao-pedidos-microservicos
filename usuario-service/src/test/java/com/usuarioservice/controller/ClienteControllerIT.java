package com.usuarioservice.controller;

import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.util.InstanceGeneratorHelper;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioControllerIT {
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
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .post("/usuario")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo(usuarioDTO.nome()))
                .body("cep", equalTo(usuarioDTO.cep()))
                .body("logradouro", equalTo(usuarioDTO.logradouro()))
                .body("complemento", equalTo(usuarioDTO.complemento()))
                .body("bairro", equalTo(usuarioDTO.bairro()))
                .body("numero", equalTo(usuarioDTO.numero()))
                .body("telefone", equalTo(usuarioDTO.telefone()))
                .body("email", equalTo(usuarioDTO.email()))
                .body("dataNascimento", equalTo(usuarioDTO.dataNascimento().toString()))
                .body("cpf", equalTo(usuarioDTO.cpf()));
    }

    @Test
    void atualizar() {
        final UUID id = UUID.fromString("fabb6ed4-eb55-4913-b1a9-607e3b3567bd");
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .put("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(usuarioDTO.nome()))
                .body("cep", equalTo(usuarioDTO.cep()))
                .body("logradouro", equalTo(usuarioDTO.logradouro()))
                .body("complemento", equalTo(usuarioDTO.complemento()))
                .body("bairro", equalTo(usuarioDTO.bairro()))
                .body("numero", equalTo(usuarioDTO.numero()))
                .body("telefone", equalTo(usuarioDTO.telefone()))
                .body("email", equalTo(usuarioDTO.email()))
                .body("dataNascimento", equalTo(usuarioDTO.dataNascimento().toString()))
                .body("cpf", equalTo(usuarioDTO.cpf()));
    }

    @Test
    void deletar() {
        final UUID id = UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void encontrarUsuarioPorId() {
        final UUID id = UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5");
        final UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5"), "João Silva", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joao@example.com", LocalDate.of(1990, 1, 1), "12345678901");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .get("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(usuarioDTO.nome()))
                .body("cep", equalTo(usuarioDTO.cep()))
                .body("logradouro", equalTo(usuarioDTO.logradouro()))
                .body("complemento", equalTo(usuarioDTO.complemento()))
                .body("bairro", equalTo(usuarioDTO.bairro()))
                .body("numero", equalTo(usuarioDTO.numero()))
                .body("telefone", equalTo(usuarioDTO.telefone()))
                .body("email", equalTo(usuarioDTO.email()))
                .body("dataNascimento", equalTo(usuarioDTO.dataNascimento().toString()))
                .body("cpf", equalTo(usuarioDTO.cpf()));
    }

    @Test
    void listarTodosOsUsuarios() throws JsonProcessingException {
        List<UsuarioDTO> usuariosDTO = InstanceGeneratorHelper.getUsuariosDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/usuario")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(usuariosDTO)));
    }


    @Test
    void cadastrarUsuarioAlreadyExistsException() {
        final UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5"), "João Silva", "01001-000",
                "Praça da Sé", "lado ímpar", "Sé", "138", "2737183089", "joao@example.com", LocalDate.of(1990, 1, 1), "91019677031");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .post("/usuario")
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo("O registro [João Silva] que você está tentando criar já existe na base de dados."))
                .body("path", equalTo("/usuario"));
    }

    @Test
    void atualizarUsuarioNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .put("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O registro [66342f0e-24fb-4cea-812f-dffbe915f180] não foi encontrado encontrado."))
                .body("path", equalTo(String.format("/usuario/%s", id)));
    }

    @Test
    void deletarUsuarioNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O registro [66342f0e-24fb-4cea-812f-dffbe915f180] não foi encontrado encontrado."))
                .body("path", equalTo(String.format("/usuario/%s", id)));
    }
}