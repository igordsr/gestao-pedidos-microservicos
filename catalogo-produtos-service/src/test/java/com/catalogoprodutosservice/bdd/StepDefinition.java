package com.catalogoprodutosservice.bdd;

import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.model.Produto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class StepDefinition {


    private Response response;

    private ProdutoDTO produtoDTO;

    private Produto produtoResposta;

    private String ENDPOINT = "http://localhost:8082/produto";

    @Dado("uma requisição para criar um novo produto com um payload ProdutoDTO válido")
    public void uma_requisição_para_criar_um_novo_produto_com_um_payload_produto_dto_válido() {
        produtoDTO = new ProdutoDTO(UUID.randomUUID(), "Sapato" + UUID.randomUUID(), "Sapato descrição", 526.75, 150);
    }

    @Quando("o cliente envia uma requisição POST para \\/produto")
    public Produto o_cliente_envia_uma_requisição_post_para_produto() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO)
                .when()
                .post(ENDPOINT);
        return response.then().extract().as(Produto.class);
    }


    @Então("o serviço deve retornar uma resposta com o código de status HTTP Criado")
    public void o_serviço_deve_retornar_uma_resposta_com_o_código_de_status_http_criado() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @E("o corpo da resposta deve conter o ProdutoDTO criado")
    public void retornar_uma_resposta_com_o_código_de_status_http_criado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/produto.schema.json"));
    }

    @Dado("que um produto já foi cadastrado")
    public void que_um_produto_ja_foi_cadastrado() {
        uma_requisição_para_criar_um_novo_produto_com_um_payload_produto_dto_válido();
        produtoResposta = o_cliente_envia_uma_requisição_post_para_produto();
    }

    @Quando("o cliente envia uma requisição PUT para \\/produto\\/\\{id}")
    public void o_cliente_envia_uma_requisição_put_para_produto() {
        String uuid = produtoResposta.getId().toString();
        ProdutoDTO produtoDTO1 = new ProdutoDTO(UUID.fromString(uuid), "Sapato Update" + uuid, "Sapato descrição update", 526.75, 150);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(produtoDTO1)
                .when()
                .put(ENDPOINT + '/' + uuid);
    }

    @Então("o serviço deve atualizar o produto com os detalhes fornecidos")
    public void o_serviço_deve_atualizar_o_produto_com_os_detalhes_fornecidos() {
        String uuid = produtoResposta.getId().toString();
        response.then().assertThat()
                .body("nome", equalTo("Sapato Update" + uuid))
                .body("descricao", equalTo("Sapato descrição update"));
    }

    @Então("retornar uma resposta com o código de status HTTP OK")
    public void retornar_uma_resposta_com_o_código_de_status_http_ok() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Então("o corpo da resposta deve conter o ProdutoDTO atualizado")
    public void o_corpo_da_resposta_deve_conter_o_produto_dto_atualizado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/produto.schema.json"));
    }


    @Dado("que um produto esta cadastrado")
    public void que_um_produto_esta_cadastrado() {
        uma_requisição_para_criar_um_novo_produto_com_um_payload_produto_dto_válido();
        produtoResposta = o_cliente_envia_uma_requisição_post_para_produto();
    }

    @Quando("o cliente envia uma requisição DELETE para \\/produto\\/\\{id}")
    public void o_cliente_envia_uma_requisição_delete_para_produto() {
        String uuid = produtoResposta.getId().toString();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT + '/' + uuid);
    }

    @Então("o serviço deve retornar uma resposta com o código de status HTTP Sem Conteúdo")
    public void o_serviço_deve_retornar_uma_resposta_com_o_código_de_status_http_sem_conteúdo() {
        response.then().assertThat().statusCode(204);

    }
}
