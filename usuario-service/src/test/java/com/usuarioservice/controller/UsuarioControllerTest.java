package com.usuarioservice.controller;

import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.service.UsuarioService;
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

class UsuarioControllerTest {
    @InjectMocks
    private UsuarioController usuarioController;
    @Mock
    private UsuarioService usuarioService;
    private UsuarioDTO usuarioDTOMock = mock(UsuarioDTO.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.usuarioController = new UsuarioController(usuarioService);
    }

    @Test
    void cadastrar() {
        when(this.usuarioService.cadastrar(any(UsuarioDTO.class))).thenReturn(usuarioDTOMock);
        UsuarioDTO result = this.usuarioController.cadastrarUsuario(usuarioDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(usuarioDTOMock),
                () -> verify(this.usuarioService, times(1)).cadastrar(any(UsuarioDTO.class))
        );
    }

    @Test
    void atualizar() {
        when(this.usuarioService.atualizarUsuario(any(UUID.class), any(UsuarioDTO.class))).thenReturn(usuarioDTOMock);
        UsuarioDTO result = this.usuarioController.atualizarUsuario(UUID.randomUUID(), usuarioDTOMock).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(usuarioDTOMock),
                () -> verify(this.usuarioService, times(1)).atualizarUsuario(any(UUID.class), any(UsuarioDTO.class))
        );
    }

    @Test
    void deletar() {
        doNothing().when(this.usuarioService).deletarUsuario(any(UUID.class));
        this.usuarioController.deletar(UUID.randomUUID());
        verify(this.usuarioService, times(1)).deletarUsuario(any(UUID.class));
    }

    @Test
    void encontrarUsuarioPorId() {
        when(this.usuarioService.encontrarUsuarioPorId(any(UUID.class))).thenReturn(this.usuarioDTOMock);
        UsuarioDTO result = this.usuarioController.encontrarUsuarioPorId(UUID.randomUUID()).getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(usuarioDTOMock),
                () -> verify(this.usuarioService, times(1)).encontrarUsuarioPorId(any(UUID.class))
        );
    }

    @Test
    void listarTodosOsUsuarios() {
        when(this.usuarioService.listarUsuarios()).thenReturn(List.of(this.usuarioDTOMock, this.usuarioDTOMock));
        List<UsuarioDTO> result = this.usuarioController.listarTodosOsUsuarios().getBody();
        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.usuarioService, times(1)).listarUsuarios()
        );
    }
}