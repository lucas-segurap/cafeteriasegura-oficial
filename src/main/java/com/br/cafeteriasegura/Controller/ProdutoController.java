package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Produto;
import com.br.cafeteriasegura.Service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET /api/produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listar() {

        return ResponseEntity.ok(
                produtoService.listarTodos()
        );
    }
}