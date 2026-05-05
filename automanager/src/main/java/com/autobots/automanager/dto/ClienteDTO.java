package com.autobots.automanager.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.List;

public record ClienteDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String nomeSocial,

        @Past(message = "Data de nascimento inválida")
        Date dataNascimento,

        @PastOrPresent(message = "Data de cadastro inválida")
        Date dataCadastro,

        @Valid
        EnderecoDTO endereco,

        @Valid
        List<DocumentoDTO> documentos,

        @Valid
        List<TelefoneDTO> telefones
) {
}