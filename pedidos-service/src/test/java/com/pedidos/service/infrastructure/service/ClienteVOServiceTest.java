package com.pedidos.service.infrastructure.service;

import com.pedidos.service.infrastructure.feign.vo.UsuarioVO;
import com.pedidos.service.infrastructure.feign.UsuarioServiceClient;
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
    private UsuarioServiceClient usuarioServiceClient;

    private UUID clienteId;
    private UsuarioVO usuarioVO;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        usuarioVO = mock(UsuarioVO.class);
    }

    @Test
    void verificarExistencia_ClienteExiste_NoExceptionThrown() {
        when(usuarioServiceClient.getUsuarioById(clienteId)).thenReturn(usuarioVO);

        clienteService.verificarExistencia(clienteId);

        verify(usuarioServiceClient).getUsuarioById(clienteId);
    }

    @Test
    void verificarExistencia_ClienteNaoExiste_ThrowRuntimeException() {
        when(usuarioServiceClient.getUsuarioById(any(UUID.class))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> clienteService.verificarExistencia(clienteId));
    }
}
