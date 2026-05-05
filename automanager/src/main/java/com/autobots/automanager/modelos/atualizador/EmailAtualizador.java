package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.entidades.Email;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailAtualizador {

    public void atualizar(Email email, EmailDTO dto) {
        if (dto == null) return;

        if (dto.endereco() != null && !dto.endereco().isBlank()) {
            email.setEndereco(dto.endereco());
        }
    }

    public void atualizar(List<Email> emails, List<EmailDTO> atualizacoes) {
        if (atualizacoes == null) return;
        for (EmailDTO dto : atualizacoes) {
            if (dto.id() == null) continue;
            emails.stream()
                    .filter(e -> e.getId().equals(dto.id()))
                    .findFirst()
                    .ifPresent(e -> atualizar(e, dto));
        }
    }
}