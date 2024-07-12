package com.usuarioservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.usuarioservice.model.Role;
import com.usuarioservice.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.UUID;

public record UsuarioDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID id,
        @Schema(example = "Yago Thomas Aparício")
        @NotNull(message = "Nome do usuario não pode estar em nulo.")
        @NotBlank(message = "Nome do usuario não pode estar em Branco.")
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
        @NotNull(message = "O telefone do usuario não pode estar em nulo.")
        @NotBlank(message = "O telefone do usuario não pode estar em Branco.")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos numéricos")
        String telefone,
        @Schema(example = "ayla_barros@gmail.com")
        @Email(message = "O e-mail deve ser válido")
        @NotNull(message = "O telefone do usuario não pode estar em nulo.")
        @NotBlank(message = "O telefone do usuario não pode estar em Branco.")
        String email,
        @Schema(example = "1991-02-15")
        @NotNull(message = "A data de nascimento não pode estar em branco")
        LocalDate dataNascimento,
        @CPF
        @Schema(example = "59694668247")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ser válido")
        String cpf,

        @Size(min = 6)
        @NotNull(message = "A senha do usuário não pode ser null")
        String password,
        @NotNull(message = "A Permissão do usuário não pode ser null")
        Role role
) {

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCep(cep);
        usuario.setLogradouro(logradouro);
        usuario.setComplemento(complemento);
        usuario.setBairro(bairro);
        usuario.setNumero(numero);
        usuario.setTelefone(telefone);
        usuario.setEmail(email);
        usuario.setDataNascimento(dataNascimento);
        usuario.setCpf(cpf);
        usuario.setPassword(password);
        usuario.setRole(role);
        return usuario;
    }

    public static UsuarioDTO getInstance(final Usuario usuario) {
        Assert.notNull(usuario, "Objeto não pode ser nulo");
        return new UsuarioDTO(
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
                usuario.getPassword(),
                usuario.getRole()
        );
    }
}
