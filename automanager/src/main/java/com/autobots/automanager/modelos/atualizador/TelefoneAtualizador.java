package com.autobots.automanager.modelos.atualizador;

import java.util.List;

import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.stereotype.Component;

@Component
public class TelefoneAtualizador {

	public void atualizar(Telefone telefone, TelefoneDTO dto) {
		if (dto == null) return;

		if (dto.ddd() != null && !dto.ddd().isBlank()) {
			telefone.setDdd(dto.ddd());
		}
		if (dto.numero() != null && !dto.numero().isBlank()) {
			telefone.setNumero(dto.numero());
		}
	}

	public void atualizar(List<Telefone> telefones, List<TelefoneDTO> atualizacoes) {
		if (atualizacoes == null) return;
		for (TelefoneDTO dto : atualizacoes) {
			if (dto.id() == null) continue;
			telefones.stream()
					.filter(t -> t.getId().equals(dto.id()))
					.findFirst()
					.ifPresent(t -> atualizar(t, dto));
		}
	}
}