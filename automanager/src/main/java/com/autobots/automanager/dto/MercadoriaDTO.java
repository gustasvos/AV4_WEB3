package com.autobots.automanager.dto;

import javax.validation.constraints.*;
import java.time.LocalDate;

public record MercadoriaDTO(

        Long id,

        @NotNull(message = "Data de validade é obrigatória")
        @Future(message = "A validade deve ser uma data futura")
        LocalDate dataValidade,

        @NotNull(message = "Data de fabricação é obrigatória")
        @PastOrPresent(message = "A data de fabricação não pode ser futura")
        LocalDate dataFabricacao,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
        String nome,

        @NotNull(message = "Necessário inserir uma quantidade")
        @Min(value = 0, message = "Quantidade não pode ser negativa")
        Long quantidade,

        @NotNull(message = "Necessário inserir um valor")
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
        Double valor,

        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        String descricao

) {}