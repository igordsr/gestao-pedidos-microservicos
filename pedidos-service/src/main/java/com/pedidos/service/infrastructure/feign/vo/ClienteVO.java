package com.pedidos.service.infrastructure.feign.vo;

import java.time.LocalDate;
import java.util.UUID;

public record ClienteVO(
        UUID id,
        String nome,
        String endereco,
        String telefone,
        String email,
        LocalDate dataNascimento,
        String cpf
) {
}