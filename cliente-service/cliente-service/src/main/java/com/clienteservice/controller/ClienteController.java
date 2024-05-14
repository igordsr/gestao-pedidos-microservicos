package com.clienteservice.controller;

import com.clienteservice.controller.exception.ClienteServiceApplicationError;
import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(value = "/cliente", produces = {"application/json"})
@Tag(name = "Cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cadastro de Cliente", description = "Os clientes podem se registrar no sistema, associando seus dados pessoais.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo cliente", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))})
    })
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody @Valid ClienteDTO clienteDTO) {
        clienteDTO = this.clienteService.cadastrar(clienteDTO);
        return new ResponseEntity<>(clienteDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar de Cliente", description = "Os clientes podem atualizar os dados cadastrarei no sistema", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização dos dados cadastrais do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo cliente", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))})
    })
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable UUID id, @RequestBody @Valid ClienteDTO clienteDTO) {
        clienteDTO = this.clienteService.atualizarCliente(id, clienteDTO);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar cliente pelo id", description = "Esté metodo tem como finalidade permitir deletar de forma logica as informações cadastrais do cliente.", method = "Delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exclusão do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo cliente", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))})
    })
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        this.clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Consultar dados do cliente pelo id", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do cliente.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo cliente", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))})
    })
    public ResponseEntity<ClienteDTO> encontrarClientePorId(@PathVariable UUID id) {
        ClienteDTO clienteDTO = this.clienteService.encontrarClientePorId(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Listar todos os clientes", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do cliente de todos o clientes.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo cliente", content = {@Content(schema = @Schema(implementation = ClienteServiceApplicationError.class))})
    })
    public ResponseEntity<List<ClienteDTO>> listarTodosOsClientes() {
        List<ClienteDTO> clienteDTOList = this.clienteService.listarClientes();
        return new ResponseEntity<>(clienteDTOList, HttpStatus.OK);
    }
}
