package com.catalogoprodutosservice.service;

import com.catalogoprodutosservice.controller.exception.modal.EntidadeNaoProcessavelException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroJaExisteException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import com.catalogoprodutosservice.util.InstanceGeneratorHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private FileUploadService fileUploadService;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job job;

    @Value("${inputFile}")
    private Resource inputFile;
    @InjectMocks
    private ProdutoService produtoService;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.produtoService = new ProdutoService(this.produtoRepository, fileUploadService, jobLauncher, job, inputFile);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void cadastrar() {
        final Produto produto = InstanceGeneratorHelper.getProduto();
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();

        when(this.produtoRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(this.produtoRepository.save(any(Produto.class))).thenReturn(produto);

        final ProdutoDTO result = this.produtoService.cadastrar(produtoDTO);


        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), produtoDTO.nome()),
                () -> assertEquals(result.descricao(), produtoDTO.descricao()),
                () -> assertEquals(result.preco(), produtoDTO.preco()),
                () -> assertEquals(result.qtdEstoque(), produtoDTO.qtdEstoque()),
                () -> verify(this.produtoRepository, times(1)).findByNomeIgnoreCase(anyString()),
                () -> verify(this.produtoRepository, times(1)).save(any())

        );
    }

    @Test
    void listarProdutos() {
        final Produto produto = InstanceGeneratorHelper.getProduto();
        List<Produto> produtos = List.of(produto, produto);

        when(this.produtoRepository.findByStatusTrue()).thenReturn(produtos);

        List<ProdutoDTO> result = this.produtoService.listarProdutos();

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.produtoRepository, times(1)).findByStatusTrue()
        );

    }

    @Test
    void encontrarProdutoPorId() {
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getProduto()));

        ProdutoDTO result = this.produtoService.encontrarProdutoPorId(UUID.randomUUID());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), produtoDTO.nome()),
                () -> assertEquals(result.descricao(), produtoDTO.descricao()),
                () -> assertEquals(result.preco(), produtoDTO.preco()),
                () -> assertEquals(result.qtdEstoque(), produtoDTO.qtdEstoque()),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class))
        );
    }

    @Test
    void encontrarProdutosPorIds() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Produto> produtos = List.of(InstanceGeneratorHelper.getProduto(), InstanceGeneratorHelper.getProduto());
        List<ProdutoDTO> expectedProdutosDTOs = produtos.stream()
                .map(ProdutoDTO::getInstance)
                .collect(Collectors.toList());

        when(this.produtoRepository.findAllById(ids)).thenReturn(produtos);

        List<ProdutoDTO> result = this.produtoService.encontrarProdutosPorIds(ids);

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(produtos.size()),
                () -> assertThat(result).containsExactlyInAnyOrderElementsOf(expectedProdutosDTOs),
                () -> verify(this.produtoRepository, times(1)).findAllById(ids)
        );
    }

    @Test
    void atualizarProduto() {
        final Produto produto = InstanceGeneratorHelper.getProduto();
        final ProdutoDTO produtoDTO = new ProdutoDTO(null, "CAMISETA ALTERADA", "Camiseta de algodão com estampa", 30.0, 2);

        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(produto));
        when(this.produtoRepository.save(any(Produto.class))).thenReturn(produto);

        final ProdutoDTO result = this.produtoService.atualizarProduto(produto.getId(), produtoDTO);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), produtoDTO.nome()),
                () -> assertEquals(result.descricao(), produtoDTO.descricao()),
                () -> assertEquals(result.preco(), produtoDTO.preco()),
                () -> assertEquals(result.qtdEstoque(), produtoDTO.qtdEstoque()),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(1)).save(any())

        );
    }

    @Test
    void deletarProduto() {
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getProduto()));
        this.produtoService.deletarProduto(UUID.randomUUID());
        verify(this.produtoRepository, times(1)).findById(any(UUID.class));
        verify(this.produtoRepository, times(1)).save(any());
    }

    @Test
    void decrementarEstoque() {
        final Integer quantidade = 2;
        final Produto produto = InstanceGeneratorHelper.getProduto();
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(produto));
        when(this.produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO result = this.produtoService.decrementarEstoque(produto.getId(), quantidade);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), produtoDTO.nome()),
                () -> assertEquals(result.descricao(), produtoDTO.descricao()),
                () -> assertEquals(result.preco(), produtoDTO.preco()),
                () -> assertEquals(result.qtdEstoque(), (produtoDTO.qtdEstoque() - quantidade)),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(1)).save(any())
        );
    }

    @Test
    void incrementarEstoque() {
        final Integer quantidade = 10;
        final Produto produto = InstanceGeneratorHelper.getProduto();
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(produto));
        when(this.produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO result = this.produtoService.incrementarEstoque(produto.getId(), quantidade);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), produtoDTO.nome()),
                () -> assertEquals(result.descricao(), produtoDTO.descricao()),
                () -> assertEquals(result.preco(), produtoDTO.preco()),
                () -> assertEquals(result.qtdEstoque(), (produtoDTO.qtdEstoque() + quantidade)),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(1)).save(any())
        );
    }

    @Test
    void cadastrarProdutoAlreadyExistsException() {
        final Produto produto = InstanceGeneratorHelper.getProduto();
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();

        when(this.produtoRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.of(produto));

        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.cadastrar(produtoDTO)).isInstanceOf(RegistroJaExisteException.class).hasMessage("O registro [Camiseta] que você está tentando criar já existe na base de dados."),
                () -> verify(this.produtoRepository, times(1)).findByNomeIgnoreCase(anyString()),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }

    @Test
    void atualizarRegistroNaoEncontradoExceptionxception() {
        final UUID uuid = UUID.fromString("5160e5ca-5912-4592-912c-3cfe6f73aa41");
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.atualizarProduto(uuid, produtoDTO)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [5160e5ca-5912-4592-912c-3cfe6f73aa41] não foi encontrado encontrado."),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }

    @Test
    void deletarRegistroNaoEncontradoExceptionxception() {
        final UUID uuid = UUID.fromString("b6803068-eea8-4fd0-91c7-af343f27d4d0");
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.deletarProduto(uuid)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [b6803068-eea8-4fd0-91c7-af343f27d4d0] não foi encontrado encontrado."),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }

    @Test
    void decrementarEstoqueProdutoInsuficienteException() {
        final Integer quantidade = 12;
        final Produto produto = InstanceGeneratorHelper.getProduto();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(produto));

        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.decrementarEstoque(produto.getId(), quantidade)).isInstanceOf(EntidadeNaoProcessavelException.class).hasMessage("A solicitação não pôde ser processada devido a dados inválidos ou à violação das regras de negócio."),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }
}