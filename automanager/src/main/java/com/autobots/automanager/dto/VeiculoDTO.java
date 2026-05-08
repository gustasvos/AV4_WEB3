package com.autobots.automanager.dto;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record VeiculoDTO(
        Long id,

        @NotNull(message = "Tipo é obrigatório")
        TipoVeiculo tipo,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        @NotBlank(message = "Placa é obrigatória")
        String placa,

        Long proprietarioId
) {}