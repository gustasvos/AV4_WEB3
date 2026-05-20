package com.autobots.automanager.dto.request;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.enumeracoes.PerfilAcesso;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

public record UsuarioRequestDTO(

        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
        String nome,

        @Size(max = 150, message = "Nome social deve ter no máximo 150 caracteres")
        String nomeSocial,

        @NotEmpty(message = "Informe ao menos um perfil de usuário")
        Set<PerfilUsuario> perfis,

        Set<PerfilAcesso> perfisAcesso,

        @Valid
        EnderecoDTO endereco,

        @Valid
        Set<DocumentoDTO> documentos,

        @Valid
        Set<TelefoneDTO> telefones,

        @Valid
        Set<EmailDTO> emails,

        @Valid
        Set<CredencialRequestDTO> credenciais
) {}