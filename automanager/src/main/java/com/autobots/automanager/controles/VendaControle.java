package com.autobots.automanager.controles;

import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.servicos.VendaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaControle {

    @Autowired
    private VendaServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
        return servico.obterVenda(id);
//
    }

    @GetMapping
    public ResponseEntity<List<Venda>> obterVendas() {
        return servico.obterVendas();
//        
    }

    @PostMapping
    public ResponseEntity<Venda> cadastrarVenda(@RequestBody @Valid VendaDTO dto) {
        return servico.cadastrarVenda(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizarVenda(
            @PathVariable long id,
            @RequestBody @Valid VendaDTO dto) {
        return servico.atualizarVenda(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirVenda(@PathVariable long id) {
        return servico.excluirVenda(id);
    }
}