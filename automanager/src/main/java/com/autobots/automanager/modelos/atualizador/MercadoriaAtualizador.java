package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MercadoriaAtualizador {

    public void atualizar(Mercadoria mercadoria, MercadoriaDTO dto) {
        if (dto == null) return;

        if (dto.dataValidade() != null) {
            mercadoria.setDataValidade(dto.dataValidade());
        }
        if (dto.dataFabricacao() != null) {
            mercadoria.setDataFabricacao(dto.dataFabricacao());
        }
        if (dto.nome() != null && !dto.nome().isBlank()) {
            mercadoria.setNome(dto.nome());
        }
        if (dto.quantidade() != null) {
            mercadoria.setQuantidade(dto.quantidade());
        }
        if (dto.valor() != null) {
            mercadoria.setValor(dto.valor());
        }
        if (dto.descricao() != null && !dto.descricao().isBlank()) {
            mercadoria.setDescricao(dto.descricao());
        }
    }

    public void atualizar(List<Mercadoria> mercadorias, List<MercadoriaDTO> atualizacoes) {
        if (atualizacoes == null) return;
        for (MercadoriaDTO dto : atualizacoes) {
            if (dto.id() == null) continue;
            mercadorias.stream()
                    .filter(e -> e.getId().equals(dto.id()))
                    .findFirst()
                    .ifPresent(e -> atualizar(e, dto));
        }
    }
}
