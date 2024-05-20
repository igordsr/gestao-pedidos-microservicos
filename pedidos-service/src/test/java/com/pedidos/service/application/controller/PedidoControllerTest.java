package com.pedidos.service.application.controller;

import com.pedidos.service.application.gateway.PedidoGateway;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.exception.RegistroNaoEncontradoException;
import com.pedidos.service.infrastructure.persistence.entity.PedidoEntity;
import com.pedidos.service.infrastructure.persistence.repository.PedidoRepository;
import com.pedidos.service.infrastructure.service.ClienteService;
import com.pedidos.service.infrastructure.service.PedidoService;
import com.pedidos.service.infrastructure.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoGateway pedidoGateway;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    ClienteService clienteService;

    @Mock
    ProdutoService produtoService;
    @Mock
    PedidoService pedidoService;

    private PedidoDTO pedidoDTOMock;

    private PedidoEntity pedidoEntity;

    private UUID identificador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pedidoDTOMock = mock(PedidoDTO.class);
        pedidoEntity = mock(PedidoEntity.class);

        identificador = UUID.randomUUID();

        this.pedidoController = new PedidoController(clienteService, produtoService, pedidoService);
    }

    @Test
    void consultarPeloIdentificadorTest() throws RegistroNaoEncontradoException {
        when(pedidoRepository.findById(UUID.randomUUID())).thenReturn(Optional.of(pedidoEntity));

        ResponseEntity<PedidoDTO> response = pedidoController.consultarPeloIdentificador(identificador);

        assertThat(response.getBody()).isEqualTo(pedidoDTOMock);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        verify(pedidoGateway).consultarPeloIdentificador(identificador);
    }

}