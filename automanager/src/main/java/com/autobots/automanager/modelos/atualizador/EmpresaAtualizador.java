package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.entidades.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpresaAtualizador {

    @Autowired
    private EnderecoAtualizador enderecoAtualizador;
    @Autowired
    private TelefoneAtualizador telefoneAtualizador;

    public void atualizar(Empresa empresa, EmpresaDTO dto) {
        if (dto == null) return;

        if (dto.razaoSocial() != null && !dto.razaoSocial().isBlank()) {
            empresa.setRazaoSocial(dto.razaoSocial());
        }
        if (dto.nomeFantasia() != null && !dto.nomeFantasia().isBlank()) {
            empresa.setNomeFantasia(dto.nomeFantasia());
        }
        if (dto.endereco() != null) {
            enderecoAtualizador.atualizar(empresa.getEndereco(), dto.endereco());
        }
        if (dto.telefones() != null) {
            telefoneAtualizador.atualizar(empresa.getTelefones(), dto.telefones());
        }
    }
}