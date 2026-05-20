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
import com.autobots.automanager.enumeracoes.PerfilAcesso;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.atualizador.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ResponseEntity<?> obterUsuario(long id) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(alvo -> {
                    if (!isAdmin(autenticado) && !isGerente(autenticado)
                            && !isVendedor(autenticado)
                            && !autenticado.getId().equals(alvo.getId())) {
                        return ResponseEntity.<Usuario>status(HttpStatus.FORBIDDEN).build();
                    }
                    adicionadorLink.adicionarLink(alvo);
                    return ResponseEntity.ok(alvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Usuario>> obterUsuarios() {
        Usuario autenticado = obterAutenticado();
        List<Usuario> todos = repositorio.findAll();

        if (!isAdmin(autenticado) && !isGerente(autenticado) && !isVendedor(autenticado)) {
            todos = todos.stream()
                    .filter(u -> u.getId().equals(autenticado.getId()))
                    .toList();
        }

        adicionadorLink.adicionarLink(todos);
        return ResponseEntity.ok(todos);
    }

    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(UsuarioRequestDTO dto) {
        Usuario autenticado = obterAutenticado();

        if (isVendedor(autenticado) &&
                dto.perfisAcesso() != null &&
                !dto.perfisAcesso().stream().allMatch(p -> p == PerfilAcesso.ROLE_CLIENTE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (isGerente(autenticado) &&
                dto.perfisAcesso() != null &&
                dto.perfisAcesso().stream().anyMatch(p ->
                        p == PerfilAcesso.ROLE_ADMIN || p == PerfilAcesso.ROLE_GERENTE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setNomeSocial(dto.nomeSocial());
        if (dto.perfis() != null) {
            usuario.setPerfis(dto.perfis());
        }
        if (dto.perfisAcesso() != null) {
            usuario.setPerfisAcesso(dto.perfisAcesso());
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

    public ResponseEntity<?> atualizarUsuario(long id, UsuarioRequestDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(alvo -> {
                    if (isVendedor(autenticado) &&
                            !alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_CLIENTE)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    if (isGerente(autenticado) &&
                            (alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_ADMIN) ||
                                    alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_GERENTE))) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    atualizador.atualizar(alvo, dto);
                    Usuario salvo = repositorio.save(alvo);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirUsuario(long id) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(alvo -> {
                    if (isVendedor(autenticado) &&
                            !alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_CLIENTE)) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    if (isGerente(autenticado) &&
                            (alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_ADMIN) ||
                                    alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_GERENTE))) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    repositorio.delete(alvo);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // credenciais
    public ResponseEntity<?> adicionarCredencial(long id, CredencialRequestDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

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

    public ResponseEntity<?> excluirCredencial(long id, long credencialId) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;
                    usuario.getCredenciais()
                            .removeIf(c -> c.getId().equals(credencialId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // emails
    public ResponseEntity<?> adicionarEmail(long id, EmailDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

                    Email email = new Email();
                    email.setEndereco(dto.endereco());
                    usuario.getEmails().add(email);
                    Usuario salvo = repositorio.save(usuario);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> excluirEmail(long id, long emailId) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

                    usuario.getEmails().removeIf(e -> e.getId().equals(emailId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // telefones
    public ResponseEntity<?> adicionarTelefone(long id, TelefoneDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

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

    public ResponseEntity<?> excluirTelefone(long id, long telefoneId) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

                    usuario.getTelefones()
                            .removeIf(t -> t.getId().equals(telefoneId));
                    repositorio.save(usuario);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // documentos
    public ResponseEntity<?> adicionarDocumento(long id, DocumentoDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;

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

    public ResponseEntity<?> excluirDocumento(long id, long documentoId) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(usuario -> {
                    ResponseEntity<?> bloqueio = verificarPermissaoSubRota(autenticado, usuario);
                    if (bloqueio != null) return bloqueio;
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

    private ResponseEntity<?> verificarPermissaoSubRota(Usuario autenticado, Usuario alvo) {
        if (!isAdmin(autenticado) && !isGerente(autenticado)
                && !isVendedor(autenticado)
                && !autenticado.getId().equals(alvo.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (isVendedor(autenticado) &&
                !alvo.getPerfisAcesso().contains(PerfilAcesso.ROLE_CLIENTE)
                && !autenticado.getId().equals(alvo.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return null; // null = permitido
    }

    private Usuario obterPorNome(String nomeUsuario) {
        return repositorio.findByNomeUsuario(nomeUsuario).orElse(null);
    }

    private Usuario obterAutenticado() {
        String nomeUsuario = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return repositorio.findByNomeUsuario(nomeUsuario).orElse(null);
    }

    private boolean isAdmin(Usuario u) {
        return u.getPerfisAcesso().contains(PerfilAcesso.ROLE_ADMIN);
    }
    private boolean isGerente(Usuario u) {
        return u.getPerfisAcesso().contains(PerfilAcesso.ROLE_GERENTE);
    }
    private boolean isVendedor(Usuario u) {
        return u.getPerfisAcesso().contains(PerfilAcesso.ROLE_VENDEDOR);
    }
}