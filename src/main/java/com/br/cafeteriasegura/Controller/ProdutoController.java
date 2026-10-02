package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(
                produtoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                produtoService.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(
            @RequestBody Produto produto) {

        return ResponseEntity.ok(
                produtoService.salvar(produto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        produtoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}