package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilAcesso;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VendaServico {

    @Autowired
    private VendaRepositorio repositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private VeiculoRepositorio veiculoRepositorio;
    @Autowired
    private AdicionadorLinkVenda adicionadorLink;
    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;
    @Autowired
    private ServicoRepositorio servicoRepositorio;

    public ResponseEntity<?> obterVenda(long id) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(venda -> {
                    if (isVendedor(autenticado) &&
                            !venda.getFuncionario().getId().equals(autenticado.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    if (isCliente(autenticado) &&
                            !venda.getCliente().getId().equals(autenticado.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    adicionadorLink.adicionarLink(venda);
                    return ResponseEntity.ok(venda);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Venda>> obterVendas() {
        Usuario autenticado = obterAutenticado();
        List<Venda> vendas = repositorio.findAll();

        if (isVendedor(autenticado)) {
            vendas = vendas.stream()
                    .filter(v -> v.getFuncionario() != null &&
                            v.getFuncionario().getId().equals(autenticado.getId()))
                    .toList();
        }
        if (isCliente(autenticado)) {
            vendas = vendas.stream()
                    .filter(v -> v.getCliente() != null &&
                            v.getCliente().getId().equals(autenticado.getId()))
                    .toList();
        }

        adicionadorLink.adicionarLink(vendas);
        return ResponseEntity.ok(vendas);
    }

    public ResponseEntity<?> cadastrarVenda(VendaDTO dto) {
        Usuario autenticado = obterAutenticado();

        if (isVendedor(autenticado) &&
                !dto.funcionarioId().equals(autenticado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Venda venda = new Venda();
        venda.setIdentificacao(dto.identificacao());
        venda.setDataCadastro(LocalDateTime.now());

        usuarioRepositorio.findById(dto.clienteId()).ifPresent(venda::setCliente);
        usuarioRepositorio.findById(dto.funcionarioId()).ifPresent(venda::setFuncionario);

        if (dto.veiculoId() != null) {
            veiculoRepositorio.findById(dto.veiculoId()).ifPresent(venda::setVeiculo);
        }
        if (dto.mercadoriaIds() != null) {
            Set<Mercadoria> mercadorias = dto.mercadoriaIds().stream()
                    .map(mercadoriaRepositorio::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            venda.setMercadorias(mercadorias);
        }
        if (dto.servicoIds() != null) {
            Set<Servico> servicos = dto.servicoIds().stream()
                    .map(servicoRepositorio::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            venda.setServicos(servicos);
        }

        Venda salvo = repositorio.save(venda);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVenda(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<?> atualizarVenda(long id, VendaDTO dto) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(venda -> {
                    if (isVendedor(autenticado) || isCliente(autenticado)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    if (dto.identificacao() != null && !dto.identificacao().isBlank()) {
                        venda.setIdentificacao(dto.identificacao());
                    }
                    if (dto.clienteId() != null) {
                        usuarioRepositorio.findById(dto.clienteId()).ifPresent(venda::setCliente);
                    }
                    if (dto.funcionarioId() != null) {
                        usuarioRepositorio.findById(dto.funcionarioId()).ifPresent(venda::setFuncionario);
                    }
                    if (dto.veiculoId() != null) {
                        veiculoRepositorio.findById(dto.veiculoId()).ifPresent(venda::setVeiculo);
                    }
                    Venda salvo = repositorio.save(venda);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirVenda(long id) {
        Usuario autenticado = obterAutenticado();
        return repositorio.findById(id)
                .map(venda -> {
                    if (isVendedor(autenticado) || isCliente(autenticado)) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    repositorio.delete(venda);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    private Usuario obterAutenticado() {
        String nomeUsuario = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepositorio.findByNomeUsuario(nomeUsuario).orElse(null);
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
    private boolean isCliente(Usuario u) {
        return u.getPerfisAcesso().contains(PerfilAcesso.ROLE_CLIENTE);
    }
}