package com.autobots.automanager.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public record EmpresaDTO(

        Long id,

        @NotBlank(message = "Razão social é obrigatória")
        @Size(min = 2, max = 150, message = "Razão social deve ter entre 2 e 150 caracteres")
        String razaoSocial,

        @Size(max = 150, message = "Nome fantasia deve ter no máximo 150 caracteres")
        String nomeFantasia,

        @Valid
        Set<TelefoneDTO> telefones,

        @Valid
        EnderecoDTO endereco,

        LocalDateTime dataCadastro,

        Set<Long> usuarioIds,

        Set<Long> mercadoriaIds,

        Set<Long> servicoIds,

        Set<Long> vendaIds

) {
}
