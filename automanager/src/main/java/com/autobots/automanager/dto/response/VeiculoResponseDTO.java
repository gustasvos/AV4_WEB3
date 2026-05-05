package com.autobots.automanager.dto.response;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

public record VeiculoResponseDTO(
        Long id,
        TipoVeiculo tipo,
        String modelo,
        String placa,
        Long proprietarioId
) {}