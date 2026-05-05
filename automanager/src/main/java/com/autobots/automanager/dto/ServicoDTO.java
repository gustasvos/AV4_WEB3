package com.autobots.automanager.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ServicoDTO(

        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
        String nome,

        @NotNull(message = "Necessário inserir um valor")
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
        Double valor,

        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        String descricao

) {}