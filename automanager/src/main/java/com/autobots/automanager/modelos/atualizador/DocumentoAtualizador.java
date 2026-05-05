package com.autobots.automanager.modelos.atualizador;

import java.util.List;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.entidades.Documento;
import org.springframework.stereotype.Component;

@Component
public class DocumentoAtualizador {

	public void atualizar(Documento documento, DocumentoDTO dto) {
		if (dto == null) return;

		if (dto.tipo() != null) {
			documento.setTipo(dto.tipo());
		}
		if (dto.dataEmissao() != null) {
			documento.setDataEmissao(dto.dataEmissao());
		}
		if (dto.numero() != null && !dto.numero().isBlank()) {
			documento.setNumero(dto.numero());
		}
	}

	public void atualizar(List<Documento> documentos, List<DocumentoDTO> atualizacoes) {
		if (atualizacoes == null) return;
		for (DocumentoDTO dto : atualizacoes) {
			if (dto.id() == null) continue;
			documentos.stream()
					.filter(d -> d.getId().equals(dto.id()))
					.findFirst()
					.ifPresent(d -> atualizar(d, dto));
		}
	}
}