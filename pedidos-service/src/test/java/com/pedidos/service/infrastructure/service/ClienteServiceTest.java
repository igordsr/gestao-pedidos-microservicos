package com.pedidos.service.infrastructure.service;

import com.pedidos.service.infrastructure.feign.Cliente;
import com.pedidos.service.infrastructure.feign.ClienteServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteServiceClient clienteServiceClient;

    private UUID clienteId;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        cliente = mock(Cliente.class);
    }

    @Test
    void verificarExistencia_ClienteExiste_NoExceptionThrown() {
        when(clienteServiceClient.getClienteById(clienteId)).thenReturn(cliente);

        clienteService.verificarExistencia(clienteId);

        verify(clienteServiceClient).getClienteById(clienteId);
    }

    @Test
    void verificarExistencia_ClienteNaoExiste_ThrowRuntimeException() {
        when(clienteServiceClient.getClienteById(any(UUID.class))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> clienteService.verificarExistencia(clienteId));
    }
}
