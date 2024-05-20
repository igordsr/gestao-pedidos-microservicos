package com.catalogoprodutosservice.controller;

import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {
    @InjectMocks
    private ProdutoController produtoController;
    @Mock
    private ProdutoService produtoService;
    private ProdutoDTO produtoDTOMock = mock(ProdutoDTO.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.produtoController = new ProdutoController(produtoService);
    }

    @Test
    void cadastrar() {
        when(this.produtoService.cadastrar(any(ProdutoDTO.class))).thenReturn(produtoDTOMock);
        ProdutoDTO result = this.produtoController.cadastrarProduto(produtoDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(produtoDTOMock),
                () -> verify(this.produtoService, times(1)).cadastrar(any(ProdutoDTO.class))
        );
    }

    @Test
    void atualizar() {
        when(this.produtoService.atualizarProduto(any(UUID.class), any(ProdutoDTO.class))).thenReturn(produtoDTOMock);
        ProdutoDTO result = this.produtoController.atualizarProduto(UUID.randomUUID(), produtoDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(produtoDTOMock),
                () -> verify(this.produtoService, times(1)).atualizarProduto(any(UUID.class), any(ProdutoDTO.class))
        );
    }

    @Test
    void deletar() {
        doNothing().when(this.produtoService).deletarProduto(any(UUID.class));
        this.produtoController.deletar(UUID.randomUUID());
        verify(this.produtoService, times(1)).deletarProduto(any(UUID.class));
    }

    @Test
    void encontrarProdutoPorId() {
        when(this.produtoService.encontrarProdutoPorId(any(UUID.class))).thenReturn(this.produtoDTOMock);
        ProdutoDTO result = this.produtoController.encontrarProdutoPorId(UUID.randomUUID()).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(produtoDTOMock),
                () -> verify(this.produtoService, times(1)).encontrarProdutoPorId(any(UUID.class))
        );
    }

    @Test
    void listarTodosOsProdutos() {
        when(this.produtoService.listarProdutos()).thenReturn(List.of(this.produtoDTOMock, this.produtoDTOMock));
        List<ProdutoDTO> result = this.produtoController.listarTodosOsProdutos().getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.produtoService, times(1)).listarProdutos()
        );
    }

    @Test
    void decrementarEstoque() {
        when(this.produtoService.decrementarEstoque(any(UUID.class), any(Integer.class))).thenReturn(this.produtoDTOMock);
        ProdutoDTO result = this.produtoController.decrementarEstoque(UUID.randomUUID(), 5).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(produtoDTOMock),
                () -> verify(this.produtoService, times(1)).decrementarEstoque(any(UUID.class), any(Integer.class))
        );
    }

    @Test
    void incrementarEstoque() {
        when(this.produtoService.incrementarEstoque(any(UUID.class), any(Integer.class))).thenReturn(this.produtoDTOMock);
        ProdutoDTO result = this.produtoController.incrementarEstoque(UUID.randomUUID(), 5).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(produtoDTOMock),
                () -> verify(this.produtoService, times(1)).incrementarEstoque(any(UUID.class), any(Integer.class))
        );
    }
}