package com.clienteservice.util;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public abstract class InstanceGeneratorHelper {

    public static Cliente getCliente() {
        final Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome("Isadora Bianca Maitê Martins");
        cliente.setCep("01001-000");
        cliente.setLogradouro("Praça da Sé");
        cliente.setComplemento("lado ímpar");
        cliente.setBairro("Sé");
        cliente.setNumero("138");
        cliente.setTelefone("2737183089");
        cliente.setEmail("isadora_bianca_martins@hotmail.it");
        cliente.setDataNascimento(LocalDate.now());
        cliente.setCpf("91019677031");
        return cliente;
    }

    public static ClienteDTO getClienteDTO() {
        Cliente cliente = getCliente();
        final ClienteDTO clienteDTO = new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCep(),
                cliente.getLogradouro(),
                cliente.getComplemento(),
                cliente.getBairro(),
                cliente.getNumero(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getDataNascimento(),
                cliente.getCpf()
        );
        return clienteDTO;
    }

    public static List<ClienteDTO> getClientesDTO() {
        return List.of(
                new ClienteDTO(UUID.fromString("1f7b366b-b51a-47c1-8b5e-8e9c8a1055f5"), "João Silva", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joao@example.com", LocalDate.of(1990, 1, 1), "12345678901"),
                new ClienteDTO(UUID.fromString("fabb6ed4-eb55-4913-b1a9-607e3b3567bd"), "Maria Oliveira", "01002-000", "Praça da República", "lado par", "República", "245", "9876543210", "maria@example.com", LocalDate.of(1995, 2, 2), "09876543210"),
                new ClienteDTO(UUID.fromString("e583d69e-0263-45fb-bf4a-780e42d155b7"), "Pedro Souza", "01003-000", "Avenida Paulista", "esquina", "Bela Vista", "37", "1111111111", "pedro@example.com", LocalDate.of(2000, 3, 3), "11122233344"),
                new ClienteDTO(UUID.fromString("2e3b1c45-1e9e-4a41-ae8e-3a6e14cfb91c"), "Ana Santos", "01004-000", "Rua Augusta", "próximo ao metrô", "Consolação", "189", "2222222222", "ana@example.com", LocalDate.of(1985, 4, 4), "55566677788"),
                new ClienteDTO(UUID.fromString("b964f3d3-9c8f-4775-a0e1-f92d8f42106e"), "Carlos Pereira", "01005-000", "Rua Oscar Freire", "loja 10", "Pinheiros", "502", "3333333333", "carlos@example.com", LocalDate.of(1975, 5, 5), "99988877766")
        );
    }
}
