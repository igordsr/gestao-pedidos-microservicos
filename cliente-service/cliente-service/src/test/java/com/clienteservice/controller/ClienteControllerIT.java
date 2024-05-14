package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.util.InstanceGeneratorHelper;
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
class ClienteControllerIT {
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
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .post("/cliente")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo(clienteDTO.nome()))
                .body("endereco", equalTo(clienteDTO.endereco()))
                .body("telefone", equalTo(clienteDTO.telefone()))
                .body("email", equalTo(clienteDTO.email()))
                .body("dataNascimento", equalTo(clienteDTO.dataNascimento().toString()))
                .body("cpf", equalTo(clienteDTO.cpf()));
    }

    @Test
    void atualizar() {
        final UUID id = UUID.fromString("fabb6ed4-eb55-4913-b1a9-607e3b3567bd");
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .put("/cliente/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(clienteDTO.nome()))
                .body("endereco", equalTo(clienteDTO.endereco()))
                .body("telefone", equalTo(clienteDTO.telefone()))
                .body("email", equalTo(clienteDTO.email()))
                .body("dataNascimento", equalTo(clienteDTO.dataNascimento().toString()))
                .body("cpf", equalTo(clienteDTO.cpf()));
    }

    @Test
    void deletar() {
        final UUID id = UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/cliente/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void encontrarClientePorId() {
        final UUID id = UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5");
        final ClienteDTO clienteDTO = new ClienteDTO(UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5"), "João Silva", "Rua A, 123", "1234567890", "joao@example.com", LocalDate.of(1990, 1, 1), "12345678901");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .get("/cliente/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(clienteDTO.nome()))
                .body("endereco", equalTo(clienteDTO.endereco()))
                .body("telefone", equalTo(clienteDTO.telefone()))
                .body("email", equalTo(clienteDTO.email()))
                .body("dataNascimento", equalTo(clienteDTO.dataNascimento().toString()))
                .body("cpf", equalTo(clienteDTO.cpf()));
    }

    @Test
    void listarTodosOsClientes() throws JsonProcessingException {
        List<ClienteDTO> clientesDTO = InstanceGeneratorHelper.getClientesDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cliente")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(clientesDTO)));
    }


    @Test
    void cadastrarClienteAlreadyExistsException() {
        final ClienteDTO clienteDTO = new ClienteDTO(UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5"), "João Silva", "Rua A, 123", "2737183089", "joao@example.com", LocalDate.of(1990, 1, 1), "91019677031");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .post("/cliente")
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo("Cliente JOÃO SILVA já está cadastrado no sistema."))
                .body("path", equalTo("/cliente"));
    }

    @Test
    void atualizarClienteNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .put("/cliente/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Cliente não foi encontrado"))
                .body("path", equalTo(String.format("/cliente/%s", id)));
    }

    @Test
    void deletarClienteNotFoundException() {
        final UUID id = UUID.fromString("66342f0e-24fb-4cea-812f-dffbe915f180");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/cliente/{id}", id)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Cliente não foi encontrado"))
                .body("path", equalTo(String.format("/cliente/%s", id)));
    }
}