package com.catalogoprodutosservice.service;


import com.catalogoprodutosservice.controller.exception.model.ProdutoAlreadyExistsException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoInsuficienteException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoNotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @InjectMocks
    private ProdutoService produtoService;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.produtoService = new ProdutoService(this.produtoRepository);
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
                () -> assertThatThrownBy(() -> this.produtoService.cadastrar(produtoDTO)).isInstanceOf(ProdutoAlreadyExistsException.class).hasMessage("Produto CAMISETA já está cadastrado no sistema."),
                () -> verify(this.produtoRepository, times(1)).findByNomeIgnoreCase(anyString()),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }

    @Test
    void atualizarProdutoNotFoundException() {
        final UUID uuid = UUID.randomUUID();
        final ProdutoDTO produtoDTO = InstanceGeneratorHelper.getProdutoDTO();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.atualizarProduto(uuid, produtoDTO)).isInstanceOf(ProdutoNotFoundException.class).hasMessage("Produto não foi encontrado"),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }

    @Test
    void deletarProdutoNotFoundException() {
        final UUID uuid = UUID.randomUUID();
        when(this.produtoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertAll(
                () -> assertThatThrownBy(() -> this.produtoService.deletarProduto(uuid)).isInstanceOf(ProdutoNotFoundException.class).hasMessage("Produto não foi encontrado"),
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
                () -> assertThatThrownBy(() -> this.produtoService.decrementarEstoque(produto.getId(), quantidade)).isInstanceOf(ProdutoInsuficienteException.class).hasMessage("Não há unidades suficientes do produto CAMISETA para a demanda solicitada"),
                () -> verify(this.produtoRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.produtoRepository, times(0)).save(any())
        );
    }
}