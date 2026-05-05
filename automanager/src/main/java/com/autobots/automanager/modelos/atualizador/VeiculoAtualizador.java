package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.request.VeiculoRequestDTO;
import com.autobots.automanager.entidades.Veiculo;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class VeiculoAtualizador {

    public void atualizar(Veiculo veiculo, VeiculoRequestDTO dto) {
        if (dto == null) return;

        if (dto.tipo() != null) {
            veiculo.setTipo(dto.tipo());
        }
        if (dto.modelo() != null && !dto.modelo().isBlank()) {
            veiculo.setModelo(dto.modelo());
        }
        if (dto.placa() != null && !dto.placa().isBlank()) {
            veiculo.setPlaca(dto.placa());
        }
    }

    public void atualizar(Collection<Veiculo> veiculos, Collection<VeiculoRequestDTO> atualizacoes) {
        if (atualizacoes == null) return;
        for (VeiculoRequestDTO dto : atualizacoes) {
            if (dto.id() == null) continue;
            veiculos.stream()
                    .filter(v -> v.getId().equals(dto.id()))
                    .findFirst()
                    .ifPresent(v -> atualizar(v, dto));
        }
    }
}