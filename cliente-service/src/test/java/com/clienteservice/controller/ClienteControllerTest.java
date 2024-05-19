package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class ClienteControllerTest {
    @InjectMocks
    private ClienteController clienteController;
    @Mock
    private ClienteService clienteService;
    private ClienteDTO clienteDTOMock = mock(ClienteDTO.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.clienteController = new ClienteController(clienteService);
    }

    @Test
    void cadastrar() {
        when(this.clienteService.cadastrar(any(ClienteDTO.class))).thenReturn(clienteDTOMock);
        ClienteDTO result = this.clienteController.cadastrarCliente(clienteDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(clienteDTOMock),
                () -> verify(this.clienteService, times(1)).cadastrar(any(ClienteDTO.class))
        );
    }

    @Test
    void atualizar() {
        when(this.clienteService.atualizarCliente(any(UUID.class), any(ClienteDTO.class))).thenReturn(clienteDTOMock);
        ClienteDTO result = this.clienteController.atualizarCliente(UUID.randomUUID(), clienteDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(clienteDTOMock),
                () -> verify(this.clienteService, times(1)).atualizarCliente(any(UUID.class), any(ClienteDTO.class))
        );
    }

    @Test
    void deletar() {
        doNothing().when(this.clienteService).deletarCliente(any(UUID.class));
        this.clienteController.deletar(UUID.randomUUID());
        verify(this.clienteService, times(1)).deletarCliente(any(UUID.class));
    }

    @Test
    void encontrarClientePorId() {
        when(this.clienteService.encontrarClientePorId(any(UUID.class))).thenReturn(this.clienteDTOMock);
        ClienteDTO result = this.clienteController.encontrarClientePorId(UUID.randomUUID()).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(clienteDTOMock),
                () -> verify(this.clienteService, times(1)).encontrarClientePorId(any(UUID.class))
        );
    }

    @Test
    void listarTodosOsClientes() {
        when(this.clienteService.listarClientes()).thenReturn(List.of(this.clienteDTOMock, this.clienteDTOMock));
        List<ClienteDTO> result = this.clienteController.listarTodosOsClientes().getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.clienteService, times(1)).listarClientes()
        );
    }
}