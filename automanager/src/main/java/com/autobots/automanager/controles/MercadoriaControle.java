package com.autobots.automanager.controles;

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.servicos.MercadoriaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {

    @Autowired
    private MercadoriaServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
        return servico.obterMercadoria(id);
//
    }

    @GetMapping
    public ResponseEntity<List<Mercadoria>> obterMercadorias() {
        return servico.obterMercadorias();
//
    }

    @PostMapping
    public ResponseEntity<Mercadoria> cadastrarMercadoria(@RequestBody @Valid MercadoriaDTO dto) {
        return servico.cadastrarMercadoria(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mercadoria> atualizarMercadoria(
            @PathVariable long id,
            @RequestBody @Valid MercadoriaDTO dto) {
        return servico.atualizarMercadoria(id, dto);
    }
}
