package com.logistica.service.controller;

import com.logistica.service.dto.EntregaDTO;
import com.logistica.service.service.EntregaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EntregaControllerTest {
    @Mock
    private EntregaService entregaService;

    @InjectMocks
    private EntregaController entregaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRelatorioDeEntregas() {
        Map<String, List<EntregaDTO>> mockRelatorio = new HashMap<>();
        mockRelatorio.put("12345", Arrays.asList(new EntregaDTO(UUID.randomUUID(), UUID.randomUUID(), "12345-678", LocalDate.now(), null, "EM_TRANSITO")));

        when(entregaService.processarPedidosPagosEAgruparPorCep()).thenReturn(mockRelatorio);

        ResponseEntity<Map<String, List<EntregaDTO>>> response = entregaController.getRelatorioDeEntregas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRelatorio, response.getBody());

        verify(entregaService, times(1)).processarPedidosPagosEAgruparPorCep();
    }

    @Test
    public void testConfirmaEntrega_Success() {
        UUID idEntrega = UUID.randomUUID();

        doNothing().when(entregaService).atualizarEntrega(idEntrega);

        ResponseEntity<Void> response = entregaController.confirmaEntrega(idEntrega);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(entregaService, times(1)).atualizarEntrega(idEntrega);
    }

    @Test
    public void testConfirmaEntrega_NotFound() {
        UUID idEntrega = UUID.randomUUID();

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrega não encontrada")).when(entregaService).atualizarEntrega(idEntrega);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            entregaController.confirmaEntrega(idEntrega);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Entrega não encontrada\"", exception.getMessage());

        verify(entregaService, times(1)).atualizarEntrega(idEntrega);
    }
}