package com.pedidos.service.infrastructure.service;

import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.infrastructure.persistence.entity.PedidoEntity;
import com.pedidos.service.infrastructure.persistence.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private UUID identificador;
    private PedidoEntity pedidoEntity;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        identificador = UUID.randomUUID();
        pedidoEntity = mock(PedidoEntity.class);
        pedido = mock(Pedido.class);
    }

    @Test
    void consultarPeloIdentificadorTest() throws RegistroNaoEncontradoException {
        when(pedidoRepository.findById(identificador)).thenReturn(Optional.of(pedidoEntity));
        when(pedidoEntity.toModal()).thenReturn(pedido);

        Pedido result = pedidoService.consultarPeloIdentificador(identificador);

        assertNotNull(result);
        assertEquals(pedido, result);
    }

    @Test
    void consultarPeloIdentificadorTestNotFound() {
        when(pedidoRepository.findById(identificador)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> pedidoService.consultarPeloIdentificador(identificador));
    }
}
