package com.usuarioservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioLoginDTO {
    @Schema(example = "ayla_barros@gmail.com")
    @Email(message = "O e-mail deve ser válido")
    @NotNull(message = "O e-mail do usuario não pode estar em nulo.")
    @NotBlank(message = "O e-mail do usuario não pode estar em Branco.")
    private String email;

    @Size(min = 6)
    @NotNull(message = "A senha do usuário não pode ser null")
    private String password;
}
