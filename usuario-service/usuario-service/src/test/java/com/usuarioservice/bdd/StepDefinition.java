package com.usuarioservice.bdd;

import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.model.Usuario;
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

    private UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.randomUUID(), "João Silva TESTE", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joa@example.com", LocalDate.of(1990, 1, 1), "81667077058");
    ;

    private Usuario usuarioResposta;

    private String ENDPOINT = "http://localhost:8080/usuario";

    @Dado("uma requisição para criar um novo usuario com um payload UsuarioDTO válido")
    public void uma_requisição_para_criar_um_novo_usuario_com_um_payload_usuario_dto_válido() {
        usuarioDTO = usuarioDTO;

    }

    @Quando("o usuário envia uma requisição POST para \\/usuario")
    public Usuario o_usuário_envia_uma_requisição_post_para_usuario() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .post(ENDPOINT);
        usuarioResposta = response.then().extract().as(Usuario.class);
        return response.then().extract().as(Usuario.class);
    }

    @Então("o serviço deve retornar uma resposta com o código de status HTTP Criado")
    public void o_serviço_deve_retornar_uma_resposta_com_o_código_de_status_http_criado() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @Então("o corpo da resposta deve conter o UsuarioDTO criado")
    public void o_corpo_da_resposta_deve_conter_o_usuario_dto_criado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usuario.schema.json"));
    }


    @Quando("o usuario envia uma requisição para \\/usuario")
    public void o_usuario_envia_uma_requisição_para_usuario() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT);
    }

    @Então("o serviço deve retornar a lista de usuarios cadastrados")
    public void o_serviço_deve_retornar_a_lista_de_usuarios_cadastrados() {
        response.then().assertThat()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].nome", equalTo("João Silva"));
    }

    @Dado("que um usuario já foi cadastrado {string}")
    public void que_um_usuario_já_foi_cadastrado(String cpf) {
        UsuarioDTO usuarioDTO1 = new UsuarioDTO(UUID.randomUUID(), "Nome Exemplo", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "exemple@example.com", LocalDate.of(1990, 1, 1), cpf);
        usuarioResposta = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO1)
                .when()
                .post(ENDPOINT).then().extract().as(Usuario.class);
        ;
    }


    @Quando("o usuário envia uma requisição PUT para \\/usuario\\/\\{id}")
    public void o_usuário_envia_uma_requisição_put_para_usuario() {
        String uuid = usuarioResposta.getId().toString();
        usuarioResposta.setBairro("Setando Bairro");
        UsuarioDTO usuarioDTO1 = UsuarioDTO.getInstance(usuarioResposta);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO1)
                .when()
                .put(ENDPOINT + '/' + uuid);
    }

    @Então("o serviço deve atualizar o usuario com os detalhes fornecidos")
    public void o_serviço_deve_atualizar_o_usuario_com_os_detalhes_fornecidos() {
        response.then().assertThat()
                .body("bairro", equalTo("Setando Bairro"));
    }

    @Então("retornar uma resposta com o código de status HTTP OK")
    public void retornar_uma_resposta_com_o_código_de_status_http_ok() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Então("o corpo da resposta deve conter o UsuarioDTO atualizado")
    public void o_corpo_da_resposta_deve_conter_o_usuario_dto_atualizado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usuario.schema.json"));
    }

}
