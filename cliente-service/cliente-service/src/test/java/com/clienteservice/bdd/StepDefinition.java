package com.clienteservice.bdd;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

public class StepDefinition {

    private Response response;

    private ClienteDTO clienteDTO = new ClienteDTO(UUID.randomUUID(), "João Silva TESTE", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joa@example.com", LocalDate.of(1990, 1, 1), "81667077058");
    ;

    private Cliente clienteResposta;

    private String ENDPOINT = "http://localhost:8080/cliente";

    @Dado("uma requisição para criar um novo cliente com um payload ClienteDTO válido")
    public void uma_requisição_para_criar_um_novo_cliente_com_um_payload_cliente_dto_válido() {
        clienteDTO = clienteDTO;

    }

    @Quando("o usuário envia uma requisição POST para \\/cliente")
    public Cliente o_usuário_envia_uma_requisição_post_para_cliente() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO)
                .when()
                .post(ENDPOINT);
        clienteResposta = response.then().extract().as(Cliente.class);
        return response.then().extract().as(Cliente.class);
    }

    @Então("o serviço deve retornar uma resposta com o código de status HTTP Criado")
    public void o_serviço_deve_retornar_uma_resposta_com_o_código_de_status_http_criado() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @Então("o corpo da resposta deve conter o ClienteDTO criado")
    public void o_corpo_da_resposta_deve_conter_o_cliente_dto_criado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
    }


    @Quando("o cliente envia uma requisição para \\/cliente")
    public void o_cliente_envia_uma_requisição_para_cliente() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT);
    }

    @Então("o serviço deve retornar a lista de clientes cadastrados")
    public void o_serviço_deve_retornar_a_lista_de_clientes_cadastrados() {
        response.then().assertThat()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].nome", equalTo("João Silva"));
    }

    @Dado("que um cliente já foi cadastrado {string}")
    public void que_um_cliente_já_foi_cadastrado(String cpf) {
        ClienteDTO clienteDTO1 = new ClienteDTO(UUID.randomUUID(), "Nome Exemplo", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "exemple@example.com", LocalDate.of(1990, 1, 1), cpf);
        clienteResposta = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO1)
                .when()
                .post(ENDPOINT).then().extract().as(Cliente.class);
        ;
    }


    @Quando("o usuário envia uma requisição PUT para \\/cliente\\/\\{id}")
    public void o_usuário_envia_uma_requisição_put_para_cliente() {
        String uuid = clienteResposta.getId().toString();
        clienteResposta.setBairro("Setando Bairro");
        ClienteDTO clienteDTO1 = ClienteDTO.getInstance(clienteResposta);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteDTO1)
                .when()
                .put(ENDPOINT + '/' + uuid);
    }

    @Então("o serviço deve atualizar o cliente com os detalhes fornecidos")
    public void o_serviço_deve_atualizar_o_cliente_com_os_detalhes_fornecidos() {
        response.then().assertThat()
                .body("bairro", equalTo("Setando Bairro"));
    }

    @Então("retornar uma resposta com o código de status HTTP OK")
    public void retornar_uma_resposta_com_o_código_de_status_http_ok() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Então("o corpo da resposta deve conter o ClienteDTO atualizado")
    public void o_corpo_da_resposta_deve_conter_o_cliente_dto_atualizado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
    }

}
