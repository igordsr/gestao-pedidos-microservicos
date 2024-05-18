package com.catalogoprodutosservice.controller;

import com.catalogoprodutosservice.controller.exception.modal.CustomException;
import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(value = "/produto", produces = {"application/json"})
@Tag(name = "Produto")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public ProdutoController(ProdutoService produtoService, JobLauncher jobLauncher, Job job) {
        this.produtoService = produtoService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cadastro de Produto", description = "Esté metodo tem como finalidade permitir o cadastro de produtos no sistema, associando os dados do produto.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        produtoDTO = this.produtoService.cadastrar(produtoDTO);
        return new ResponseEntity<>(produtoDTO, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar de Produto", description = "Esté metodo tem como finalidade permitir atualizar os dados cadastrais do produtos no sistema", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização dos dados cadastrais do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable UUID id, @RequestBody @Valid ProdutoDTO produtoDTO) {
        produtoDTO = this.produtoService.atualizarProduto(id, produtoDTO);
        return new ResponseEntity<>(produtoDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar produto pelo id", description = "Esté metodo tem como finalidade permitir deletar de forma logica as informações cadastrais do produto.", method = "Delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exclusão do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        this.produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Consultar dados do produto pelo id", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do produto.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<ProdutoDTO> encontrarProdutoPorId(@PathVariable UUID id) {
        ProdutoDTO produtoDTO = this.produtoService.encontrarProdutoPorId(id);
        return new ResponseEntity<>(produtoDTO, HttpStatus.OK);
    }

    @GetMapping("filterById={id}")
    @Operation(summary = "Consultar dados do produto pelo id", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais do produto.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<List<ProdutoDTO>> encontrarProdutosPorIds(@PathVariable @NotEmpty List<UUID> id) {
        List<ProdutoDTO> produtoDTO = this.produtoService.encontrarProdutosPorIds(id);
        return new ResponseEntity<>(produtoDTO, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Listar todos os produtos", description = "Esté metodo tem como finalidade permitir consultar as informações cadastrais de todos o produtos.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta do produto realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = {@Content(schema = @Schema(implementation = CustomException.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca pelo produto", content = {@Content(schema = @Schema(implementation = CustomException.class))})
    })
    public ResponseEntity<List<ProdutoDTO>> listarTodosOsProdutos() {
        List<ProdutoDTO> produtoDTOList = this.produtoService.listarProdutos();
        return new ResponseEntity<>(produtoDTOList, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/decrementarEstoque/{quantidade}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Decrementar estoque", description = "Este método permite diminuir uma quantidade específica de um produto.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda do produto realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "422", description = "Estoque insuficiente", content = @Content)
    })
    public ResponseEntity<ProdutoDTO> decrementarEstoque(@PathVariable UUID id, @PathVariable int quantidade) {
        final ProdutoDTO produtoDTO = produtoService.decrementarEstoque(id, quantidade);
        return new ResponseEntity<>(produtoDTO, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/incrementarEstoque/{quantidade}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Incrementar estoque", description = "Este método permite incrementar o estoque de um produto existente.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque incrementado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<ProdutoDTO> incrementarEstoque(@PathVariable UUID id, @PathVariable int quantidade) {
        final ProdutoDTO produtoDTO = produtoService.incrementarEstoque(id, quantidade);
        return new ResponseEntity<>(produtoDTO, HttpStatus.OK);
    }

    @GetMapping("/batch")
    public BatchStatus batch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", Calendar.getInstance().getTime())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        while (jobExecution.isRunning()) {
            System.out.println("..................");
        }
        return jobExecution.getStatus();
    }

}
