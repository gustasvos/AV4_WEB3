package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.dto.request.CredencialRequestDTO;
import com.autobots.automanager.dto.request.UsuarioRequestDTO;
import com.autobots.automanager.dto.response.CredencialResponseDTO;
import com.autobots.automanager.dto.response.UsuarioResponseDTO;
import com.autobots.automanager.entidades.*;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.atualizador.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio repositorio;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;
    @Autowired
    private UsuarioAtualizador atualizador;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public ResponseEntity<Usuario> obterUsuario(long id) {
        return repositorio.findById(id)
                .map(usuario -> {
                    adicionadorLink.adicionarLink(usuario);
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.notFound().build());
//
    }

    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        adicionadorLink.adicionarLink(usuarios);
        return ResponseEntity.ok(usuarios);
//        
    }

    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setNomeSocial(dto.nomeSocial());
        if (dto.perfis() != null) {
            usuario.setPerfis(dto.perfis());
        }

        Usuario salvo = repositorio.save(usuario);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(toResponseDTO(salvo));
    }

    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(long id, UsuarioRequestDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    atualizador.atualizar(usuario, dto);
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirUsuario(long id) {
        return repositorio.findById(id)
                .map(usuario -> {
                    repositorio.delete(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // credenciais
    public ResponseEntity<UsuarioResponseDTO> adicionarCredencial(long id, CredencialRequestDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
                    credencial.setNomeUsuario(dto.nomeUsuario());
                    credencial.setSenha(encoder.encode(dto.senha()));
                    credencial.setCriacao(LocalDateTime.now());
                    credencial.setInativo(false);
                    usuario.getCredenciais().add(credencial);
                    Usuario salvo = repositorio.save(usuario);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UsuarioResponseDTO> atualizarCredencial(long id, long credencialId, CredencialRequestDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    usuario.getCredenciais().stream()
                            .filter(c -> c.getId().equals(credencialId))
                            .findFirst()
                            .ifPresent(c -> {
                                CredencialUsuarioSenha cus = (CredencialUsuarioSenha) c;
                                if (dto.nomeUsuario() != null && !dto.nomeUsuario().isBlank()) {
                                    cus.setNomeUsuario(dto.nomeUsuario());
                                }
                                if (dto.senha() != null && !dto.senha().isBlank()) {
                                    cus.setSenha(encoder.encode(dto.senha()));
                                }
                            });
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirCredencial(long id, long credencialId) {
        return repositorio.findById(id)
                .map(usuario -> {
                    usuario.getCredenciais()
                            .removeIf(c -> c.getId().equals(credencialId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // emails
    public ResponseEntity<UsuarioResponseDTO> adicionarEmail(long id, EmailDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    Email email = new Email();
                    email.setEndereco(dto.endereco());
                    usuario.getEmails().add(email);
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirEmail(long id, long emailId) {
        return repositorio.findById(id)
                .map(usuario -> {
                    usuario.getEmails()
                            .removeIf(e -> e.getId().equals(emailId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // telefones
    public ResponseEntity<UsuarioResponseDTO> adicionarTelefone(long id, TelefoneDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(dto.ddd());
                    telefone.setNumero(dto.numero());
                    usuario.getTelefones().add(telefone);
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElseGet(() -> ResponseEntity.<UsuarioResponseDTO>notFound().build());
    }

    public ResponseEntity<Object> excluirTelefone(long id, long telefoneId) {
        return repositorio.findById(id)
                .map(usuario -> {
                    usuario.getTelefones()
                            .removeIf(t -> t.getId().equals(telefoneId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // documentos
    public ResponseEntity<UsuarioResponseDTO> adicionarDocumento(long id, DocumentoDTO dto) {
        return repositorio.findById(id)
                .map(usuario -> {
                    Documento documento = new Documento();
                    documento.setTipo(dto.tipo());
                    documento.setDataEmissao(dto.dataEmissao());
                    documento.setNumero(dto.numero());
                    usuario.getDocumentos().add(documento);
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirDocumento(long id, long documentoId) {
        return repositorio.findById(id)
                .map(usuario -> {
                    usuario.getDocumentos()
                            .removeIf(d -> d.getId().equals(documentoId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    private CredencialResponseDTO toDTO(Credencial credencial) {
        CredencialUsuarioSenha cus = (CredencialUsuarioSenha) credencial;
        return new CredencialResponseDTO(
                cus.getId(),
                cus.getNomeUsuario(),
                cus.getCriacao(),
                cus.getUltimoAcesso(),
                cus.isInativo()
        );
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getNomeSocial(),
                usuario.getPerfis(),
                usuario.getEndereco() == null ? null : new EnderecoDTO(
                        usuario.getEndereco().getId(),
                        usuario.getEndereco().getEstado(),
                        usuario.getEndereco().getCidade(),
                        usuario.getEndereco().getBairro(),
                        usuario.getEndereco().getRua(),
                        usuario.getEndereco().getNumero(),
                        usuario.getEndereco().getCodigoPostal(),
                        usuario.getEndereco().getInformacoesAdicionais()
                ),
                usuario.getDocumentos().stream()
                        .map(d -> new DocumentoDTO(d.getId(), d.getTipo(), d.getDataEmissao(), d.getNumero()))
                        .collect(Collectors.toSet()),
                usuario.getTelefones().stream()
                        .map(t -> new TelefoneDTO(t.getId(), t.getDdd(), t.getNumero()))
                        .collect(Collectors.toSet()),
                usuario.getEmails().stream()
                        .map(e -> new EmailDTO(e.getId(), e.getEndereco()))
                        .collect(Collectors.toSet()),
                usuario.getCredenciais().stream()
                        .map(c -> {
                            CredencialUsuarioSenha cus = (CredencialUsuarioSenha) c;
                            return new CredencialResponseDTO(
                                    cus.getId(),
                                    cus.getNomeUsuario(),
                                    cus.getCriacao(),
                                    cus.getUltimoAcesso(),
                                    cus.isInativo()
                            );
                        })
                        .collect(Collectors.toSet())
        );

        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(usuario.getId()))
                .withSelfRel());

        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuarios())
                .withRel("usuarios"));

        return dto;
    }
}