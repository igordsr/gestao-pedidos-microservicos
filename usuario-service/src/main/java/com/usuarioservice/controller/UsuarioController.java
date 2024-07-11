package com.usuarioservice.controller;

import com.usuarioservice.controller.exception.modal.CustomException;
import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.service.UsuarioService;
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
@RequestMapping(value = "/usuario", produces = {"application/json"})
@Tag(name = "Usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cadastro de Usuario", description = "Os usuarios podem se registrar no sistema, associando seus dados pessoais.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro do usuario realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo usuario", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        usuarioDTO = this.usuarioService.cadastrar(usuarioDTO);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar de Usuario", description = "Os usuarios podem atualizar os dados cadastrarei no sistema", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização dos dados cadastrais do usuario realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo usuario", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        usuarioDTO = this.usuarioService.atualizarUsuario(id, usuarioDTO);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar usuario pelo id", description = "Esté metodo tem como finalidade permitir deletar de forma logica as informações cadastrais do usuario.", method = "Delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exclusão do usuario realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo usuario", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        this.usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Consultar dados do usuario pelo id", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do usuario.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do usuario realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo usuario", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<UsuarioDTO> encontrarUsuarioPorId(@PathVariable UUID id) {
        UsuarioDTO usuarioDTO = this.usuarioService.encontrarUsuarioPorId(id);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Listar todos os usuarios", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do usuario de todos o usuarios.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do usuario realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo usuario", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<List<UsuarioDTO>> listarTodosOsUsuarios() {
        List<UsuarioDTO> usuarioDTOList = this.usuarioService.listarUsuarios();
        return new ResponseEntity<>(usuarioDTOList, HttpStatus.OK);
    }
}
