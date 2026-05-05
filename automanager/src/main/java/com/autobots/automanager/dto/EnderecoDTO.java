package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record EnderecoDTO(

        Long id,

        @NotBlank(message = "Estado é obrigatório")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ser a sigla com 2 letras maiúsculas (ex: SP)")
        String estado,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
        String cidade,

        @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
        String bairro,

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 2, max = 150, message = "Rua deve ter entre 2 e 150 caracteres")
        String rua,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
        String numero,

        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 12345-678 ou 12345678")
        String codigoPostal,

        @Size(max = 255, message = "Informações adicionais devem ter no máximo 255 caracteres")
        String informacoesAdicionais

) {}