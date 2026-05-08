package com.autobots.automanager.controles;

import com.autobots.automanager.dto.*;
import com.autobots.automanager.dto.request.CredencialRequestDTO;
import com.autobots.automanager.dto.request.UsuarioRequestDTO;
import com.autobots.automanager.dto.response.UsuarioResponseDTO;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControle {

    @Autowired
    private UsuarioServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
        return servico.obterUsuario(id);
//
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        return servico.obterUsuarios();
//
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        return servico.cadastrarUsuario(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable long id,
            @RequestBody @Valid UsuarioRequestDTO dto) {
        return servico.atualizarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirUsuario(@PathVariable long id) {
        return servico.excluirUsuario(id);
    }

    // credenciais
    @PostMapping("/{id}/credenciais")
    public ResponseEntity<UsuarioResponseDTO> adicionarCredencial(
            @PathVariable long id,
            @RequestBody @Valid CredencialRequestDTO dto) {
        return servico.adicionarCredencial(id, dto);
    }

    @PutMapping("/{id}/credenciais/{credencialId}")
    public ResponseEntity<UsuarioResponseDTO> atualizarCredencial(
            @PathVariable long id,
            @PathVariable long credencialId,
            @RequestBody @Valid CredencialRequestDTO dto) {
        return servico.atualizarCredencial(id, credencialId, dto);
    }

    @DeleteMapping("/{id}/credenciais/{credencialId}")
    public ResponseEntity<Object> excluirCredencial(
            @PathVariable long id,
            @PathVariable long credencialId) {
        return servico.excluirCredencial(id, credencialId);
    }

    // emails
    @PostMapping("/{id}/emails")
    public ResponseEntity<UsuarioResponseDTO> adicionarEmail(
            @PathVariable long id,
            @RequestBody @Valid EmailDTO dto) {
        return servico.adicionarEmail(id, dto);
    }

    @DeleteMapping("/{id}/emails/{emailId}")
    public ResponseEntity<Object> excluirEmail(
            @PathVariable long id,
            @PathVariable long emailId) {
        return servico.excluirEmail(id, emailId);
    }

    // telefones
    @PostMapping("/{id}/telefones")
    public ResponseEntity<UsuarioResponseDTO> adicionarTelefone(
            @PathVariable long id,
            @RequestBody @Valid TelefoneDTO dto) {
        return servico.adicionarTelefone(id, dto);
    }

    @DeleteMapping("/{id}/telefones/{telefoneId}")
    public ResponseEntity<Object> excluirTelefone(
            @PathVariable long id,
            @PathVariable long telefoneId) {
        return servico.excluirTelefone(id, telefoneId);
    }

    // documentos
    @PostMapping("/{id}/documentos")
    public ResponseEntity<UsuarioResponseDTO> adicionarDocumento(
            @PathVariable long id,
            @RequestBody @Valid DocumentoDTO dto) {
        return servico.adicionarDocumento(id, dto);
    }

    @DeleteMapping("/{id}/documentos/{documentoId}")
    public ResponseEntity<Object> excluirDocumento(
            @PathVariable long id,
            @PathVariable long documentoId) {
        return servico.excluirDocumento(id, documentoId);
    }
}