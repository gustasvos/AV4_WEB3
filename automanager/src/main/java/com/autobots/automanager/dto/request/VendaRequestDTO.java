package com.autobots.automanager.dto.request;

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.dto.ServicoDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

public record VendaRequestDTO(
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