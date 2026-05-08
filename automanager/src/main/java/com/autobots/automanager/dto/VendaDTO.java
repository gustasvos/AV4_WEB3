package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record VendaDTO(
        Long id,

        @NotBlank(message = "Identificação é obrigatória")
        String identificacao,

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "Funcionário é obrigatório")
        Long funcionarioId,

        Long veiculoId,

        Set<Long> mercadoriaIds,

        Set<Long> servicoIds
) {}