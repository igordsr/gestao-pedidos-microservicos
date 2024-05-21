package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.model.Item;
import com.pedidos.service.infrastructure.feign.vo.ProdutoVO;
import com.pedidos.service.infrastructure.feign.ProdutoServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoVOServiceTest {

    @Mock
    private ProdutoServiceClient produtoServiceClient;

    @InjectMocks
    private ProdutoService produtoService;

    private UUID produtoId;
    private ProdutoVO produtoVO;
    private Item item;
    private int quantidade;

    @BeforeEach
    void setUp() {
        produtoId = UUID.randomUUID();
        quantidade = 5;
        produtoVO = new ProdutoVO(produtoId, "Teste Nome", "Teste Desc", 10.7, 10);
        item = new Item(produtoId, 10);
    }

    @Test
    void consultarProdutosTest() {
        List<UUID> ids = List.of(produtoId);
        when(produtoServiceClient.getProdutoById(ids)).thenReturn(List.of(produtoVO));

        List<Item> result = produtoService.consultarProdutos(ids);

        assertEquals(1, result.size());
        assertEquals(item.getQuantidade(), result.get(0).getQuantidade());
    }

}