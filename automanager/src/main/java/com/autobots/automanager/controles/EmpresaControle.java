package com.autobots.automanager.controles;

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.servicos.EmpresaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaControle {

    @Autowired
    private EmpresaServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
        return servico.obterEmpresa(id);
//
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        return servico.obterEmpresas();
//        
    }

    @PostMapping
    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody @Valid EmpresaDTO dto) {
        return servico.cadastrarEmpresa(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresa(
            @PathVariable long id,
            @RequestBody @Valid EmpresaDTO dto) {
        return servico.atualizarEmpresa(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirEmpresa(@PathVariable long id) {
        return servico.excluirEmpresa(id);
    }

    // usuarios
    @PostMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Empresa> associarUsuario(
            @PathVariable long id,
            @PathVariable long usuarioId) {
        return servico.associarUsuario(id, usuarioId);
    }

    @DeleteMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Object> desassociarUsuario(
            @PathVariable long id,
            @PathVariable long usuarioId) {
        return servico.desassociarUsuario(id, usuarioId);
    }

    // mercadorias
    @PostMapping("/{id}/mercadorias/{mercadoriaId}")
    public ResponseEntity<Empresa> associarMercadoria(
            @PathVariable long id,
            @PathVariable long mercadoriaId) {
        return servico.associarMercadoria(id, mercadoriaId);
    }

    @DeleteMapping("/{id}/mercadorias/{mercadoriaId}")
    public ResponseEntity<Object> desassociarMercadoria(
            @PathVariable long id,
            @PathVariable long mercadoriaId) {
        return servico.desassociarMercadoria(id, mercadoriaId);
    }

    // servicos
    @PostMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<Empresa> associarServico(
            @PathVariable long id,
            @PathVariable long servicoId) {
        return servico.associarServico(id, servicoId);
    }

    @DeleteMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<Object> desassociarServico(
            @PathVariable long id,
            @PathVariable long servicoId) {
        return servico.desassociarServico(id, servicoId);
    }

    // vendas
    @PostMapping("/{id}/vendas/{vendaId}")
    public ResponseEntity<Empresa> associarVenda(
            @PathVariable long id,
            @PathVariable long vendaId) {
        return servico.associarVenda(id, vendaId);
    }

    @DeleteMapping("/{id}/vendas/{vendaId}")
    public ResponseEntity<Object> desassociarVenda(
            @PathVariable long id,
            @PathVariable long vendaId) {
        return servico.desassociarVenda(id, vendaId);
    }

    // telefones
    @PostMapping("/{id}/telefones")
    public ResponseEntity<Empresa> adicionarTelefone(
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

    // endereco
    @PutMapping("/{id}/endereco")
    public ResponseEntity<Empresa> atualizarEndereco(
            @PathVariable long id,
            @RequestBody @Valid EnderecoDTO dto) {
        return servico.atualizarEndereco(id, dto);
    }
}