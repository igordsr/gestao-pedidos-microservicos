package com.clienteservice.service;

import com.clienteservice.controller.exception.modal.RegistroJaExisteException;
import com.clienteservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.repository.ClienteRepository;
import com.clienteservice.util.InstanceGeneratorHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.clienteService = new ClienteService(this.clienteRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void cadastrar() {
        final Cliente cliente = InstanceGeneratorHelper.getCliente();
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();

        when(this.clienteRepository.findByEmailOrCpf(anyString(), anyString())).thenReturn(Optional.empty());
        when(this.clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        final ClienteDTO result = this.clienteService.cadastrar(clienteDTO);


        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), clienteDTO.nome()),
                () -> assertEquals(result.cep(), clienteDTO.cep()),
                () -> assertEquals(result.logradouro(), clienteDTO.logradouro()),
                () -> assertEquals(result.complemento(), clienteDTO.complemento()),
                () -> assertEquals(result.bairro(), clienteDTO.bairro()),
                () -> assertEquals(result.numero(), clienteDTO.numero()),
                () -> assertEquals(result.telefone(), clienteDTO.telefone()),
                () -> assertEquals(result.email(), clienteDTO.email()),
                () -> assertEquals(result.dataNascimento(), clienteDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), clienteDTO.cpf()),
                () -> verify(this.clienteRepository, times(1)).findByEmailOrCpf(anyString(), anyString()),
                () -> verify(this.clienteRepository, times(1)).save(any())

        );
    }

    @Test
    void listarClientes() {
        final Cliente cliente = InstanceGeneratorHelper.getCliente();
        List<Cliente> clientes = List.of(cliente, cliente);

        when(this.clienteRepository.findByStatusTrue()).thenReturn(clientes);

        List<ClienteDTO> result = this.clienteService.listarClientes();

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().hasSize(2),
                () -> verify(this.clienteRepository, times(1)).findByStatusTrue()
        );

    }

    @Test
    void encontrarClientePorId() {
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();
        when(this.clienteRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getCliente()));

        ClienteDTO result = this.clienteService.encontrarClientePorId(UUID.randomUUID());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), clienteDTO.nome()),
                () -> assertEquals(result.cep(), clienteDTO.cep()),
                () -> assertEquals(result.logradouro(), clienteDTO.logradouro()),
                () -> assertEquals(result.complemento(), clienteDTO.complemento()),
                () -> assertEquals(result.bairro(), clienteDTO.bairro()),
                () -> assertEquals(result.numero(), clienteDTO.numero()),
                () -> assertEquals(result.telefone(), clienteDTO.telefone()),
                () -> assertEquals(result.email(), clienteDTO.email()),
                () -> assertEquals(result.dataNascimento(), clienteDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), clienteDTO.cpf()),
                () -> verify(this.clienteRepository, times(1)).findById(any(UUID.class))
        );
    }

    @Test
    void atualizarCliente() {
        final Cliente cliente = InstanceGeneratorHelper.getCliente();
        final ClienteDTO clienteDTO = new ClienteDTO(
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
                "58235909626"
        );

        when(this.clienteRepository.findById(any(UUID.class))).thenReturn(Optional.of(cliente));
        when(this.clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        final ClienteDTO result = this.clienteService.atualizarCliente(cliente.getId(), clienteDTO);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result.nome(), clienteDTO.nome()),
                () -> assertEquals(result.cep(), clienteDTO.cep()),
                () -> assertEquals(result.logradouro(), clienteDTO.logradouro()),
                () -> assertEquals(result.complemento(), clienteDTO.complemento()),
                () -> assertEquals(result.bairro(), clienteDTO.bairro()),
                () -> assertEquals(result.numero(), clienteDTO.numero()),
                () -> assertEquals(result.telefone(), clienteDTO.telefone()),
                () -> assertEquals(result.email(), clienteDTO.email()),
                () -> assertEquals(result.dataNascimento(), clienteDTO.dataNascimento()),
                () -> assertEquals(result.cpf(), clienteDTO.cpf()),
                () -> verify(this.clienteRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.clienteRepository, times(1)).save(any())

        );
    }

    @Test
    void deletarCliente() {
        when(this.clienteRepository.findById(any(UUID.class))).thenReturn(Optional.of(InstanceGeneratorHelper.getCliente()));
        this.clienteService.deletarCliente(UUID.randomUUID());
        verify(this.clienteRepository, times(1)).findById(any(UUID.class));
        verify(this.clienteRepository, times(1)).save(any());
    }


    @Test
    void cadastrarClienteAlreadyExistsException() {
        final Cliente cliente = InstanceGeneratorHelper.getCliente();
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();

        when(this.clienteRepository.findByEmailOrCpf(anyString(), anyString())).thenReturn(Optional.of(cliente));

        assertAll(
                () -> assertThatThrownBy(() -> this.clienteService.cadastrar(clienteDTO)).isInstanceOf(RegistroJaExisteException.class).hasMessage("O registro [Isadora Bianca Maitê Martins] que você está tentando criar já existe na base de dados."),
                () -> verify(this.clienteRepository, times(1)).findByEmailOrCpf(anyString(), anyString()),
                () -> verify(this.clienteRepository, times(0)).save(any())
        );
    }

    @Test
    void atualizarClienteNotFoundException() {
        final UUID uuid = UUID.fromString("e4815b1c-4a09-48a5-ac18-90baf00afc22");
        final ClienteDTO clienteDTO = InstanceGeneratorHelper.getClienteDTO();
        when(this.clienteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> this.clienteService.atualizarCliente(uuid, clienteDTO)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [e4815b1c-4a09-48a5-ac18-90baf00afc22] não foi encontrado encontrado."),
                () -> verify(this.clienteRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.clienteRepository, times(0)).save(any())
        );
    }

    @Test
    void deletarClienteNotFoundException() {
        final UUID uuid = UUID.fromString("82547121-1019-4e0a-96d2-cd18b4c9e17d");
        when(this.clienteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertAll(
                () -> assertThatThrownBy(() -> this.clienteService.deletarCliente(uuid)).isInstanceOf(RegistroNaoEncontradoException.class).hasMessage("O registro [82547121-1019-4e0a-96d2-cd18b4c9e17d] não foi encontrado encontrado."),
                () -> verify(this.clienteRepository, times(1)).findById(any(UUID.class)),
                () -> verify(this.clienteRepository, times(0)).save(any())
        );
    }
}