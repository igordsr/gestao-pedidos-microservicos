package com.pedidos.service.bdd;

import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.infrastructure.feign.Cliente;
import com.pedidos.service.infrastructure.feign.ClienteServiceClient;
import com.pedidos.service.infrastructure.service.PedidoService;
import com.pedidos.service.util.InstanceGeneratorHelper;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class StepDefinition {

    @Mock
    private ClienteServiceClient clienteServiceClient;

    @Autowired
    private PedidoService pedidoService;

    private Response response;

    private Pedido pedidoResposta;

    private Cliente clienteValido;

    private String ENDPOINT = "http://localhost:8083/pedido";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Dado("um cliente válido")
    public void um_cliente_válido() {
        UUID clienteId = UUID.randomUUID();
        clienteValido = new Cliente(clienteId, "Cliente Teste", "Teste", "1195865555", "teste@teste.com", LocalDate.now(), "22222222222");
        when(clienteServiceClient.getClienteById(any(UUID.class)))
                .thenReturn(clienteValido);
    }

    @Quando("o cliente envia uma requisição POST para \\/pedido")
    public Pedido o_cliente_envia_uma_requisição_post_para_pedido() {
        PedidoDTO pedidoDTO = new PedidoDTO(UUID.randomUUID(), clienteValido.id(), List.of(InstanceGeneratorHelper.getItemDTO()), StatusPedido.PREPARANDO_PARA_ENVIO.getDescricao());
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pedidoDTO)
                .when()
                .post(ENDPOINT);
        return response.then().extract().as(Pedido.class);
    }

    @Então("o serviço deve retornar uma resposta com o código de status HTTP Criado")
    public void o_serviço_deve_retornar_uma_resposta_com_o_código_de_status_http_criado() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @Então("o corpo da resposta deve conter o PedidoDTO criado")
    public void o_corpo_da_resposta_deve_conter_o_pedido_dto_criado() {
        response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/pedido.schema.json"));
    }

}
