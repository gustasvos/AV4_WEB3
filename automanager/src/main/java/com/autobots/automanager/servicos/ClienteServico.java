package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.dto.ClienteDTO;
import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkCliente;
import com.autobots.automanager.modelos.atualizador.ClienteAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class ClienteServico {

    @Autowired
    private ClienteRepositorio repositorio;
    @Autowired
    private AdicionadorLinkCliente adicionadorLink;
    @Autowired
    private ClienteAtualizador atualizador;

    public ResponseEntity<Cliente> obterCliente(long id) {
        return repositorio.findById(id)
                .map(cliente -> {
                    adicionadorLink.adicionarLink(cliente);
                    return ResponseEntity.ok(cliente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Cliente>> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        adicionadorLink.adicionarLink(clientes);
        return ResponseEntity.ok(clientes);
    }

    public ResponseEntity<Cliente> cadastrarCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setNomeSocial(dto.nomeSocial());
        cliente.setDataNascimento(dto.dataNascimento());
        cliente.setDataCadastro(dto.dataCadastro());

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setEstado(dto.endereco().estado());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setRua(dto.endereco().rua());
            endereco.setNumero(dto.endereco().numero());
            endereco.setCodigoPostal(dto.endereco().codigoPostal());
            endereco.setInformacoesAdicionais(dto.endereco().informacoesAdicionais());
            cliente.setEndereco(endereco);
        }

        if (dto.documentos() != null) {
            for (DocumentoDTO docDto : dto.documentos()) {
                Documento documento = new Documento();
                documento.setTipo(docDto.tipo());
                documento.setDataEmissao(docDto.dataEmissao());
                documento.setNumero(docDto.numero());
                cliente.getDocumentos().add(documento);
            }
        }

        if (dto.telefones() != null) {
            for (TelefoneDTO telDto : dto.telefones()) {
                Telefone telefone = new Telefone();
                telefone.setDdd(telDto.ddd());
                telefone.setNumero(telDto.numero());
                cliente.getTelefones().add(telefone);
            }
        }

        Cliente salvo = repositorio.save(cliente);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ClienteControle.class)
                        .obterCliente(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Cliente> atualizarCliente(long id, ClienteDTO dto) {
        return repositorio.findById(id)
                .map(cliente -> {
                    atualizador.atualizar(cliente, dto);
                    Cliente salvo = repositorio.save(cliente);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirCliente(long id) {
        return repositorio.findById(id)
                .map(cliente -> {
                    repositorio.delete(cliente);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }
}