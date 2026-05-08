package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelos.atualizador.EmpresaAtualizador;
import com.autobots.automanager.modelos.atualizador.EnderecoAtualizador;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpresaServico {

    @Autowired
    private EmpresaRepositorio repositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;
    @Autowired
    private ServicoRepositorio servicoRepositorio;
    @Autowired
    private VendaRepositorio vendaRepositorio;
    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;
    @Autowired
    private EnderecoAtualizador enderecoAtualizador;
    @Autowired
    private EmpresaAtualizador atualizador;

    public ResponseEntity<Empresa> obterEmpresa(long id) {
        return repositorio.findById(id)
                .map(empresa -> {
                    adicionadorLink.adicionarLink(empresa);
                    return ResponseEntity.ok(empresa);
                })
                .orElse(ResponseEntity.notFound().build());
//
    }

    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        adicionadorLink.adicionarLink(empresas);
        return ResponseEntity.ok(empresas);
//        
    }

    public ResponseEntity<Empresa> cadastrarEmpresa(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.razaoSocial());
        empresa.setNomeFantasia(dto.nomeFantasia());
        empresa.setDataCadastro(LocalDateTime.now());

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setEstado(dto.endereco().estado());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setRua(dto.endereco().rua());
            endereco.setNumero(dto.endereco().numero());
            endereco.setCodigoPostal(dto.endereco().codigoPostal());
            endereco.setInformacoesAdicionais(dto.endereco().informacoesAdicionais());
            empresa.setEndereco(endereco);
        }

        if (dto.telefones() != null) {
            for (TelefoneDTO telDto : dto.telefones()) {
                Telefone telefone = new Telefone();
                telefone.setDdd(telDto.ddd());
                telefone.setNumero(telDto.numero());
                empresa.getTelefones().add(telefone);
            }
        }

        Empresa salvo = repositorio.save(empresa);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresa(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Empresa> atualizarEmpresa(long id, EmpresaDTO dto) {
        return repositorio.findById(id)
                .map(empresa -> {
                    atualizador.atualizar(empresa, dto);
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirEmpresa(long id) {
        return repositorio.findById(id)
                .map(empresa -> {
                    repositorio.delete(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // usuarios
    public ResponseEntity<Empresa> associarUsuario(long id, long usuarioId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    usuarioRepositorio.findById(usuarioId)
                            .ifPresent(u -> empresa.getUsuarios().add(u));
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> desassociarUsuario(long id, long usuarioId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    empresa.getUsuarios().removeIf(u -> u.getId().equals(usuarioId));
                    repositorio.save(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // mercadorias
    public ResponseEntity<Empresa> associarMercadoria(long id, long mercadoriaId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    mercadoriaRepositorio.findById(mercadoriaId)
                            .ifPresent(m -> empresa.getMercadorias().add(m));
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> desassociarMercadoria(long id, long mercadoriaId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    empresa.getMercadorias().removeIf(m -> m.getId().equals(mercadoriaId));
                    repositorio.save(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // servicos
    public ResponseEntity<Empresa> associarServico(long id, long servicoId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    servicoRepositorio.findById(servicoId)
                            .ifPresent(s -> empresa.getServicos().add(s));
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> desassociarServico(long id, long servicoId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    empresa.getServicos().removeIf(s -> s.getId().equals(servicoId));
                    repositorio.save(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // vendas
    public ResponseEntity<Empresa> associarVenda(long id, long vendaId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    vendaRepositorio.findById(vendaId)
                            .ifPresent(v -> empresa.getVendas().add(v));
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> desassociarVenda(long id, long vendaId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    empresa.getVendas().removeIf(v -> v.getId().equals(vendaId));
                    repositorio.save(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // telefones
    public ResponseEntity<Empresa> adicionarTelefone(long id, TelefoneDTO dto) {
        return repositorio.findById(id)
                .map(empresa -> {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(dto.ddd());
                    telefone.setNumero(dto.numero());
                    empresa.getTelefones().add(telefone);
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirTelefone(long id, long telefoneId) {
        return repositorio.findById(id)
                .map(empresa -> {
                    empresa.getTelefones().removeIf(t -> t.getId().equals(telefoneId));
                    repositorio.save(empresa);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

    // endereco
    public ResponseEntity<Empresa> atualizarEndereco(long id, EnderecoDTO dto) {
        return repositorio.findById(id)
                .map(empresa -> {
                    if (empresa.getEndereco() == null) {
                        Endereco endereco = new Endereco();
                        enderecoAtualizador.atualizar(endereco, dto);
                        empresa.setEndereco(endereco);
                    } else {
                        enderecoAtualizador.atualizar(empresa.getEndereco(), dto);
                    }
                    Empresa salvo = repositorio.save(empresa);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}