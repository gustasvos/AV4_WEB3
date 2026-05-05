package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.entidades.Servico;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicoAtualizador {

    public void atualizar(Servico servico, ServicoDTO dto) {
        if (dto == null) return;

        if (dto.nome() != null && !dto.nome().isBlank()) {
            servico.setNome(dto.nome());
        }
        if (dto.valor() != null) {
            servico.setValor(dto.valor());
        }
        if (dto.descricao() != null && !dto.descricao().isBlank()) {
            servico.setDescricao(dto.descricao());
        }
    }

    public void atualizar(List<Servico> servicos, List<ServicoDTO> atualizacoes) {
        if (atualizacoes == null) return;
        for (ServicoDTO dto : atualizacoes) {
            if (dto.id() == null) continue;
            servicos.stream()
                    .filter(e -> e.getId().equals(dto.id()))
                    .findFirst()
                    .ifPresent(e -> atualizar(e, dto));
        }
    }
}
