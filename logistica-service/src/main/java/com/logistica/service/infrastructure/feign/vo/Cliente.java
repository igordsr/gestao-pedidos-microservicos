package com.logistica.service.infrastructure.feign.vo;

import java.time.LocalDate;
import java.util.UUID;

public record Cliente(
        UUID id,
        String nome,
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String numero,
        String telefone,
        String email,
        LocalDate dataNascimento,
        String cpf
){

}