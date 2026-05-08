package com.autobots.automanager.dto.response;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private EnderecoDTO endereco;
    private Set<DocumentoDTO> documentos;
    private Set<TelefoneDTO> telefones;
    private Set<EmailDTO> emails;
    private Set<CredencialResponseDTO> credenciais;
}