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

        @Schema(example = "01001-000")
        @NotNull(message = "O CEP não pode estar em nulo.")
        @NotBlank(message = "O CEP não pode estar em Branco.")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 00000-000")
        String cep,

        @Schema(example = "Praça da Sé")
        @NotNull(message = "O logradouro não pode estar em nulo.")
        @NotBlank(message = "O logradouro não pode estar em Branco.")
        String logradouro,

        @Schema(example = "lado ímpar")
        String complemento,

        @Schema(example = "Sé")
        @NotNull(message = "O bairro não pode estar em nulo.")
        @NotBlank(message = "O bairro não pode estar em Branco.")
        String bairro,

        @Schema(example = "138")
        @NotNull(message = "O número não pode estar em nulo.")
        @NotBlank(message = "O número não pode estar em Branco.")
        String numero,
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
        cliente.setCep(cep);
        cliente.setLogradouro(logradouro);
        cliente.setComplemento(complemento);
        cliente.setBairro(bairro);
        cliente.setNumero(numero);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setDataNascimento(dataNascimento);
        cliente.setCpf(cpf);
        return cliente;
    }

    public static ClienteDTO getInstance(final Cliente cliente) {
        Assert.notNull(cliente, "Objeto não pode ser nulo");
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getCep(),
                cliente.getLogradouro(),
                cliente.getComplemento(),
                cliente.getBairro(),
                cliente.getNumero(), cliente.getTelefone(), cliente.getEmail(), cliente.getDataNascimento(), cliente.getCpf());
    }
}
