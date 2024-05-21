package com.pedidos.service.application.controller;

import com.pedidos.service.application.gateway.PedidoGateway;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.exception.CustomException;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.infrastructure.service.ClienteService;
import com.pedidos.service.infrastructure.service.PedidoService;
import com.pedidos.service.infrastructure.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(value = "/pedido", produces = {"application/json"})
@Tag(name = "Pedido")
public class PedidoController {
    private final PedidoGateway pedidoGateway;

    @Autowired
    public PedidoController(ClienteService clienteService, ProdutoService produtoService, PedidoService pedidoService) {
        this.pedidoGateway = new PedidoGateway(clienteService, produtoService, pedidoService);
    }

    @GetMapping("{identificador}")
    @Operation(summary = "Consultar Pedido", description = "Consultar os dados do Pedido", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Pagamento do pedido realizado com sucesso"),})
    public ResponseEntity<PedidoDTO> consultarPeloIdentificador(@PathVariable UUID identificador) throws RegistroNaoEncontradoException {
        PedidoDTO pedidoDTO = this.pedidoGateway.consultarPeloIdentificador(identificador);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary = "Cadastro de Pedido", description = "Esté metodo tem como finalidade permitir o cadastro de pedido no sistema, associando os dados do pedido.", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cadastro do pedido realizado com sucesso"),})
    public ResponseEntity<PedidoDTO> cadastrarProduto(@RequestBody @Valid PedidoDTO pedidoDTO) {
        pedidoDTO = this.pedidoGateway.cadastrar(pedidoDTO);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.CREATED);
    }

    @PutMapping("{identificador}/efetuar-pagamento")
    @Operation(summary = "Pagar de Pedido", description = "Esté metodo tem como finalidade permitir o pagamento do pedido no sistema", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Pagamento do pedido realizado com sucesso"),})
    public ResponseEntity<PedidoDTO> liquidarPedido(@PathVariable UUID identificador) throws CustomException {
        PedidoDTO pedidoDTO = this.pedidoGateway.liquidarPedido(identificador);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
    }

    @GetMapping("relatorio-pedidos-pagos")
    @Operation(summary = "Gerar Relatório", description = "Esté metodo tem como finalidade emitir o relatório dos pedidos já pagos no sistema, e atualiza o status para 'PREPARANDO PARA ENVIO'", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Relatório do pedido realizado com sucesso"),})
    public ResponseEntity<List<PedidoDTO>> gerarRelatorioPedidosPagos() throws CustomException {
        List<PedidoDTO> pedidoDTOS = this.pedidoGateway.gerarRelatorioPedidosPagos();
        return new ResponseEntity<>(pedidoDTOS, HttpStatus.OK);
    }

    @PutMapping("{identificador}/transportar")
    @Operation(summary = "Transportar Pedido", description = "Esté metodo tem como finalidade de atualizar o status do pedido para 'AGUARDANDO ENTREGA', quando for iniciado o transpote do pedido para o cliente", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),})
    public ResponseEntity<PedidoDTO> enviar(@PathVariable UUID identificador) throws CustomException {
        PedidoDTO pedidoDTO = this.pedidoGateway.enviar(identificador);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
    }

    @PutMapping("{identificador}/entregar")
    @Operation(summary = "Entregar Pedido", description = "Esté metodo tem como finalidade de atualizar o status do pedido para 'ENTREGUE', quando o pedido for entregue para o cliente", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),})
    public ResponseEntity<PedidoDTO> entregar(@PathVariable UUID identificador) throws CustomException {
        PedidoDTO pedidoDTO = this.pedidoGateway.entregar(identificador);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
    }
}
