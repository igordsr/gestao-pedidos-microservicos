package com.pedidos.service.infrastructure.feign;

import java.time.LocalDate;
import java.util.UUID;

public record Cliente(
        UUID id,
        String nome,
        String endereco,
        String telefone,
        String email,
        LocalDate dataNascimento,
        String cpf
) {
}