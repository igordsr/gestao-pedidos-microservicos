package com.clienteservice.dto;

import com.clienteservice.model.Cliente;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.UUID;

public record ClienteDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID id,
        @Schema(example = "Yago Thomas Aparício")
        @NotNull(message = "Nome do cliente não pode estar em nulo.")
        @NotBlank(message = "Nome do cliente não pode estar em Branco.")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @Schema(example = "Rua Anéo Costamilan, 355")
        @NotNull(message = "O endereço do cliente não pode estar em nulo.")
        @NotBlank(message = "O endereço do cliente não pode estar em Branco.")
        String endereco,
        @Schema(example = "5435008794")
        @NotNull(message = "O telefone do cliente não pode estar em nulo.")
        @NotBlank(message = "O telefone do cliente não pode estar em Branco.")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos numéricos")
        String telefone,
        @Schema(example = "ayla_barros@gmail.com")
        @Email(message = "O e-mail deve ser válido")
        @NotNull(message = "O telefone do cliente não pode estar em nulo.")
        @NotBlank(message = "O telefone do cliente não pode estar em Branco.")
        String email,
        @Schema(example = "1991-02-15")
        @NotNull(message = "A data de nascimento não pode estar em branco")
        LocalDate dataNascimento,
        @CPF
        @Schema(example = "59694668247")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ser válido")
        String cpf
) {

    public Cliente toCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setDataNascimento(dataNascimento);
        cliente.setCpf(cpf);
        return cliente;
    }

    public static ClienteDTO getInstance(final Cliente cliente) {
        Assert.notNull(cliente, "Objeto não pode ser nulo");
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEndereco(), cliente.getTelefone(), cliente.getEmail(), cliente.getDataNascimento(), cliente.getCpf());
    }
}
