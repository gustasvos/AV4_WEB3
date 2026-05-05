package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.ClienteDTO;
import com.autobots.automanager.entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteAtualizador {

	@Autowired
	private EnderecoAtualizador enderecoAtualizador;
	@Autowired
	private DocumentoAtualizador documentoAtualizador;
	@Autowired
	private TelefoneAtualizador telefoneAtualizador;

	public void atualizar(Cliente cliente, ClienteDTO dto) {
		if (dto == null) return;

		if (dto.nome() != null && !dto.nome().isBlank()) {
			cliente.setNome(dto.nome());
		}
		if (dto.nomeSocial() != null && !dto.nomeSocial().isBlank()) {
			cliente.setNomeSocial(dto.nomeSocial());
		}
		if (dto.dataNascimento() != null) {
			cliente.setDataNascimento(dto.dataNascimento());
		}
		if (dto.dataCadastro() != null) {
			cliente.setDataCadastro(dto.dataCadastro());
		}
		if (dto.endereco() != null) {
			enderecoAtualizador.atualizar(cliente.getEndereco(), dto.endereco());
		}
		if (dto.documentos() != null) {
			documentoAtualizador.atualizar(cliente.getDocumentos(), dto.documentos());
		}
		if (dto.telefones() != null) {
			telefoneAtualizador.atualizar(cliente.getTelefones(), dto.telefones());
		}
	}
}