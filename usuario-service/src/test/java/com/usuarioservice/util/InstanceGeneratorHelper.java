package com.usuarioservice.util;

import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.model.Role;
import com.usuarioservice.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public abstract class InstanceGeneratorHelper {

    public static Usuario getUsuario() {
        final Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Isadora Bianca Maitê Martins");
        usuario.setCep("01001-000");
        usuario.setLogradouro("Praça da Sé");
        usuario.setComplemento("lado ímpar");
        usuario.setBairro("Sé");
        usuario.setNumero("138");
        usuario.setTelefone("2737183089");
        usuario.setEmail("isadora_bianca_martins@hotmail.it");
        usuario.setDataNascimento(LocalDate.now());
        usuario.setCpf("91019677031");
        return usuario;
    }

    public static UsuarioDTO getUsuarioDTO() {
        Usuario usuario = getUsuario();
        final UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCep(),
                usuario.getLogradouro(),
                usuario.getComplemento(),
                usuario.getBairro(),
                usuario.getNumero(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getDataNascimento(),
                usuario.getCpf(),
                "123456",
                Role.ROLE_ADMIN
        );
        return usuarioDTO;
    }

    public static List<UsuarioDTO> getUsuariosDTO() {
        return List.of(
                new UsuarioDTO(UUID.fromString("e7fa5b83-42a4-11ef-aff1-d05099ff5204"), "João Silva", "01001-000", "Praça da Sé", "lado ímpar", "Sé", "138", "1234567890", "joao@gmail.com", LocalDate.of(1990, 1, 1), "12345678901", "$2a$10$jAmXjR3FPHj4hyES0Qh3VuG9I6f7g9dNQ2Rtd0WRcQVFJVdno3SJ6", Role.ROLE_ADMIN),
                new UsuarioDTO(UUID.fromString("e7fa5ecf-42a4-11ef-aff1-d05099ff5204"), "Maria Oliveira", "01002-000", "Praça da República", "lado par", "República", "138", "9876543210", "maria@gmail.com", LocalDate.of(1995, 2, 2), "09876543210", "$2a$10$jAmXjR3FPHj4hyES0Qh3VuG9I6f7g9dNQ2Rtd0WRcQVFJVdno3SJ6", Role.ROLE_ADMIN),
                new UsuarioDTO(UUID.fromString("e7fa5f37-42a4-11ef-aff1-d05099ff5204"), "Pedro Souza", "01003-000", "Avenida Paulista", "esquina", "Bela Vista", "138", "1111111111", "pedro@gmail.com", LocalDate.of(2000, 3, 3), "11122233344", "$2a$10$jAmXjR3FPHj4hyES0Qh3VuG9I6f7g9dNQ2Rtd0WRcQVFJVdno3SJ6", Role.ROLE_ADMIN),
                new UsuarioDTO(UUID.fromString("e7fa5f70-42a4-11ef-aff1-d05099ff5204"), "Ana Santos", "01004-000", "Rua Augusta", "próximo ao metrô", "Consolação", "138", "2222222222", "ana@gmail.com", LocalDate.of(1985, 4, 4), "55566677788", "$2a$10$jAmXjR3FPHj4hyES0Qh3VuG9I6f7g9dNQ2Rtd0WRcQVFJVdno3SJ6", Role.ROLE_ADMIN),
                new UsuarioDTO(UUID.fromString("e7fa5fae-42a4-11ef-aff1-d05099ff5204"), "Carlos Pereira", "01005-000", "Rua Oscar Freire", "loja 10", "Pinheiros", "138", "3333333333", "carlos@gmail.com", LocalDate.of(1975, 5, 5), "99988877766", "$2a$10$jAmXjR3FPHj4hyES0Qh3VuG9I6f7g9dNQ2Rtd0WRcQVFJVdno3SJ6", Role.ROLE_ADMIN)
        );
    }
}
