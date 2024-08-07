package com.usuarioservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.model.Role;
import com.usuarioservice.util.InstanceGeneratorHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    private String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        jwtToken = obtainJwtToken("joao@gmail.com", "string"); // Mudar conforme seu setup de autenticação
    }

    private String obtainJwtToken(String username, String password) {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"email\":\"" + username + "\", \"password\":\"" + password + "\"}")
                .when()
                .post("/auth"); // Mudar conforme o endpoint de autenticação
        return response.jsonPath().getString("token");
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
        final UUID id = UUID.fromString("e7fa5ecf-42a4-11ef-aff1-d05099ff5204");
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
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
        final UUID id = UUID.fromString("e7fa5f37-42a4-11ef-aff1-d05099ff5204");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void encontrarUsuarioPorId() {
        final UUID id = UUID.fromString("e7fa5b83-42a4-11ef-aff1-d05099ff5204");
        final UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.fromString("e7fa5b83-42a4-11ef-aff1-d05099ff5204"), "João Silva", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joao@gmail.com", LocalDate.of(1990, 1, 1), "12345678901", "123456", Role.ROLE_ADMIN);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
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
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/usuario")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(usuariosDTO)));
    }


    @Test
    void cadastrarUsuarioAlreadyExistsException() {
        final UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.fromString("e7fa5b83-42a4-11ef-aff1-d05099ff5204"), "João Silva", "01001-000",
                "Praça da Sé", "lado ímpar", "Sé", "138", "2737183089", "joao@gmail.com", LocalDate.of(1990, 1, 1), "91019677031", "123456", Role.ROLE_ADMIN);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
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
                .header("Authorization", "Bearer " + jwtToken)
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
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/usuario/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O registro [66342f0e-24fb-4cea-812f-dffbe915f180] não foi encontrado encontrado."))
                .body("path", equalTo(String.format("/usuario/%s", id)));
    }
}