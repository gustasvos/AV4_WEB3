package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.atualizador.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class EnderecoServico {

    @Autowired
    private EnderecoRepositorio repositorio;
    @Autowired
    private AdicionadorLinkEndereco adicionadorLinkEndereco;
    @Autowired
    private EnderecoAtualizador atualizador;

    public ResponseEntity<Endereco> obterEndereco(long id) {
        return repositorio.findById(id)
                .map(endereco -> {
                    adicionadorLinkEndereco.adicionarLink(endereco);
                    return ResponseEntity.ok(endereco);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
        adicionadorLinkEndereco.adicionarLink(enderecos);
        return ResponseEntity.ok(enderecos);
    }

    public ResponseEntity<Endereco> cadastrarEndereco(EnderecoDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setEstado(dto.estado());
        endereco.setCidade(dto.cidade());
        endereco.setBairro(dto.bairro());
        endereco.setRua(dto.rua());
        endereco.setNumero(dto.numero());
        endereco.setCodigoPostal(dto.codigoPostal());
        endereco.setInformacoesAdicionais(dto.informacoesAdicionais());

        Endereco salvo = repositorio.save(endereco);
        adicionadorLinkEndereco.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EnderecoControle.class)
                        .obterEndereco(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Endereco> atualizarEndereco(long id, EnderecoDTO dto) {
        return repositorio.findById(id)
                .map(endereco -> {
                    atualizador.atualizar(endereco, dto);
                    Endereco salvo = repositorio.save(endereco);
                    adicionadorLinkEndereco.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}