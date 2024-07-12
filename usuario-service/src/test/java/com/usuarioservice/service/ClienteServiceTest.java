package com.usuarioservice.service;

import com.usuarioservice.controller.exception.modal.RegistroJaExisteException;
import com.usuarioservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.model.Role;
import com.usuarioservice.model.Usuario;
import com.usuarioservice.repository.UsuarioRepository;
import com.usuarioservice.util.InstanceGeneratorHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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

class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsuarioService usuarioService;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.usuarioService = new UsuarioService(this.usuarioRepository, this.passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void cadastrar() {
        final Usuario usuario = InstanceGeneratorHelper.getUsuario();
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();

        when(this.usuarioRepository.findByEmailOrCpf(anyString(), anyString())).thenReturn(Optional.empty());
        when(this.usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        final UsuarioDTO result = this.usuarioService.cadastrar(usuarioDTO);


        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), usuarioDTO.nome()),
                () -> assertEquals(result.cep(), usuarioDTO.cep()),
                () -> assertEquals(result.logradouro(), usuarioDTO.logradouro()),
                () -> assertEquals(result.complemento(), usuarioDTO.complemento()),
                () -> assertEquals(result.bairro(), usuarioDTO.bairro()),
                () -> assertEquals(result.numero(), usuarioDTO.numero()),
                () -> assertEquals(result.telefone(), usuarioDTO.telefone()),
                () -> assertEquals(result.email(), usuarioDTO.email()),
                () -> assertEquals(result.dataNascimento(), usuarioDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), usuarioDTO.cpf()),
                () -> verify(this.usuarioRepository, times(1)).findByEmailOrCpf(anyString(), anyString()),
                () -> verify(this.usuarioRepository, times(1)).save(any())

        );
    }

    @Test
    void listarUsuarios() {
        final Usuario usuario = InstanceGeneratorHelper.getUsuario();
        List<Usuario> usuarios = List.of(usuario, usuario);

        when(this.usuarioRepository.findByStatusTrue()).thenReturn(usuarios);

        List<UsuarioDTO> result = this.usuarioService.listarUsuarios();

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.usuarioRepository, times(1)).findByStatusTrue()
        );

    }

    @Test
    void encontrarUsuarioPorId() {
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();
        when(this.usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getUsuario()));

        UsuarioDTO result = this.usuarioService.encontrarUsuarioPorId(UUID.randomUUID());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), usuarioDTO.nome()),
                () -> assertEquals(result.cep(), usuarioDTO.cep()),
                () -> assertEquals(result.logradouro(), usuarioDTO.logradouro()),
                () -> assertEquals(result.complemento(), usuarioDTO.complemento()),
                () -> assertEquals(result.bairro(), usuarioDTO.bairro()),
                () -> assertEquals(result.numero(), usuarioDTO.numero()),
                () -> assertEquals(result.telefone(), usuarioDTO.telefone()),
                () -> assertEquals(result.email(), usuarioDTO.email()),
                () -> assertEquals(result.dataNascimento(), usuarioDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), usuarioDTO.cpf()),
                () -> verify(this.usuarioRepository, times(1)).findById(any(UUID.class))
        );
    }

    @Test
    void atualizarUsuario() {
        final Usuario usuario = InstanceGeneratorHelper.getUsuario();
        final UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                "Valentina Mariane Ramos",
                "01001-000",
                "Praça da Sé",
                "lado ímpar",
                "Sé",
                "138",
                "95999878033",
                "valentina.mariane.ramos@hotmail.com.br",
                LocalDate.now(),
                "58235909626",
                "123456",
                Role.ROLE_ADMIN
        );

        when(this.usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(usuario));
        when(this.usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        final UsuarioDTO result = this.usuarioService.atualizarUsuario(usuario.getId(), usuarioDTO);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), usuarioDTO.nome()),
                () -> assertEquals(result.cep(), usuarioDTO.cep()),
                () -> assertEquals(result.logradouro(), usuarioDTO.logradouro()),
                () -> assertEquals(result.complemento(), usuarioDTO.complemento()),
                () -> assertEquals(result.bairro(), usuarioDTO.bairro()),
                () -> assertEquals(result.numero(), usuarioDTO.numero()),
                () -> assertEquals(result.telefone(), usuarioDTO.telefone()),
                () -> assertEquals(result.email(), usuarioDTO.email()),
                () -> assertEquals(result.dataNascimento(), usuarioDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), usuarioDTO.cpf()),
                () -> verify(this.usuarioRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.usuarioRepository, times(1)).save(any())

        );
    }

    @Test
    void deletarUsuario() {
        when(this.usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getUsuario()));
        this.usuarioService.deletarUsuario(UUID.randomUUID());
        verify(this.usuarioRepository, times(1)).findById(any(UUID.class));
        verify(this.usuarioRepository, times(1)).save(any());
    }


    @Test
    void cadastrarUsuarioAlreadyExistsException() {
        final Usuario usuario = InstanceGeneratorHelper.getUsuario();
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();

        when(this.usuarioRepository.findByEmailOrCpf(anyString(), anyString())).thenReturn(Optional.of(usuario));

        assertAll(
                () -> assertThatThrownBy(() -> this.usuarioService.cadastrar(usuarioDTO)).isInstanceOf(RegistroJaExisteException.class).hasMessage("O registro [Isadora Bianca Maitê Martins] que você está tentando criar já existe na base de dados."),
                () -> verify(this.usuarioRepository, times(1)).findByEmailOrCpf(anyString(), anyString()),
                () -> verify(this.usuarioRepository, times(0)).save(any())
        );
    }

    @Test
    void atualizarUsuarioNotFoundException() {
        final UUID uuid = UUID.fromString("e4815b1c-4a09-48a5-ac18-90baf00afc22");
        final UsuarioDTO usuarioDTO = InstanceGeneratorHelper.getUsuarioDTO();
        when(this.usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> this.usuarioService.atualizarUsuario(uuid, usuarioDTO)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [e4815b1c-4a09-48a5-ac18-90baf00afc22] não foi encontrado encontrado."),
                () -> verify(this.usuarioRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.usuarioRepository, times(0)).save(any())
        );
    }

    @Test
    void deletarUsuarioNotFoundException() {
        final UUID uuid = UUID.fromString("82547121-1019-4e0a-96d2-cd18b4c9e17d");
        when(this.usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertAll(
                () -> assertThatThrownBy(() -> this.usuarioService.deletarUsuario(uuid)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [82547121-1019-4e0a-96d2-cd18b4c9e17d] não foi encontrado encontrado."),
                () -> verify(this.usuarioRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.usuarioRepository, times(0)).save(any())
        );
    }
}