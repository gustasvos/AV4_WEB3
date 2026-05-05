package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record TelefoneDTO(

        Long id,

        @NotBlank(message = "DDD é obrigatório")
        @Pattern(regexp = "\\d{2}", message = "DDD deve conter exatamente 2 dígitos numéricos")
        String ddd,

        @NotBlank(message = "Número é obrigatório")
        @Pattern(regexp = "\\d{8,9}", message = "Número deve conter 8 ou 9 dígitos numéricos")
        String numero

) {}