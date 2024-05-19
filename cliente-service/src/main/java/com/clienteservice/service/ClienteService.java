package com.clienteservice.service;

import com.clienteservice.controller.exception.modal.RegistroJaExisteException;
import com.clienteservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(final ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO cadastrar(final ClienteDTO clienteDTO) {
        Assert.notNull(clienteDTO, "O objeto clienteDTO não pode ser null");
        Optional<Cliente> clienteOptional = this.clienteRepository.findByEmailOrCpf(clienteDTO.email(), clienteDTO.cpf());
        if (clienteOptional.isPresent()) {
            throw new RegistroJaExisteException(clienteOptional.get().getNome());
        }
        final Cliente cliente = clienteRepository.save(clienteDTO.toCliente());
        return ClienteDTO.getInstance(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findByStatusTrue().stream().map(ClienteDTO::getInstance).toList();
    }

    public ClienteDTO encontrarClientePorId(UUID id) {
        final Cliente cliente = this.findById(id);
        return ClienteDTO.getInstance(cliente);
    }

    public ClienteDTO atualizarCliente(final UUID id, final ClienteDTO clienteDTO) {
        Assert.notNull(clienteDTO, "O objeto clienteDTO não pode ser null");
        Cliente cliente = this.findById(id);
        cliente.setNome(clienteDTO.nome());
        cliente.setCep(clienteDTO.cep());
        cliente.setLogradouro(clienteDTO.logradouro());
        cliente.setComplemento(clienteDTO.complemento());
        cliente.setBairro(clienteDTO.bairro());
        cliente.setNumero(clienteDTO.numero());
        cliente.setTelefone(clienteDTO.telefone());
        cliente.setEmail(clienteDTO.email());
        cliente.setDataNascimento(clienteDTO.dataNascimento());
        cliente.setCpf(clienteDTO.cpf());
        this.clienteRepository.save(cliente);
        return ClienteDTO.getInstance(cliente);
    }

    public void deletarCliente(UUID id) {
        Assert.notNull(id, "O objeto id não pode ser null");
        Cliente cliente = this.findById(id);
        cliente.setStatus(Boolean.FALSE);
        this.clienteRepository.save(cliente);
    }

    private Cliente findById(UUID id) {
        Assert.notNull(id, "O objeto id não pode ser null");
        return this.clienteRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id.toString()));
    }
}
