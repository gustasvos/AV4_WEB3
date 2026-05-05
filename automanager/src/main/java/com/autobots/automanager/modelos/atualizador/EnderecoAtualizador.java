package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.entidades.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoAtualizador {

	public void atualizar(Endereco endereco, EnderecoDTO dto) {
		if (dto == null) return;

		if (dto.estado() != null && !dto.estado().isBlank()) {
			endereco.setEstado(dto.estado());
		}
		if (dto.cidade() != null && !dto.cidade().isBlank()) {
			endereco.setCidade(dto.cidade());
		}
		if (dto.bairro() != null && !dto.bairro().isBlank()) {
			endereco.setBairro(dto.bairro());
		}
		if (dto.rua() != null && !dto.rua().isBlank()) {
			endereco.setRua(dto.rua());
		}
		if (dto.numero() != null && !dto.numero().isBlank()) {
			endereco.setNumero(dto.numero());
		}
		if (dto.codigoPostal() != null && !dto.codigoPostal().isBlank()) {
			endereco.setCodigoPostal(dto.codigoPostal());
		}
		if (dto.informacoesAdicionais() != null && !dto.informacoesAdicionais().isBlank()) {
			endereco.setInformacoesAdicionais(dto.informacoesAdicionais());
		}
	}
}