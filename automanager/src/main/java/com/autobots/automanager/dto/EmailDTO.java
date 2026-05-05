package com.autobots.automanager.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record EmailDTO(

        Long id,

        @NotBlank(message = "Necessário inserir um endereço de email")
        @Email(message = "O email provido não é válido")
        String endereco
) {
}
