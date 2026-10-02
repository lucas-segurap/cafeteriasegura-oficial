package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final PedidoService pedidoService;

    public AdminController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    // ==========================================
    // VERIFICAR ACESSO DO GERENTE
    // ==========================================

    private boolean gerenteAutenticado(
            HttpSession session) {

        Boolean gerente =
                (Boolean) session.getAttribute(
                        "gerenteLogado"
                );

        return Boolean.TRUE.equals(gerente);
    }


    // ==========================================
    // TODOS OS PEDIDOS
    // ==========================================

    @GetMapping("/pedidos")
    public ResponseEntity<?> listarPedidos(
            HttpSession session) {

        if (!gerenteAutenticado(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "erro",
                            "Acesso permitido somente aos gerentes."
                    ));
        }

        return ResponseEntity.ok(
                pedidoService.listar()
        );
    }


    // ==========================================
    // EM ATENDIMENTO
    // ==========================================

    @GetMapping("/pedidos/em-atendimento")
    public ResponseEntity<?> emAtendimento(
            HttpSession session) {

        if (!gerenteAutenticado(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "erro",
                            "Acesso permitido somente aos gerentes."
                    ));
        }

        return ResponseEntity.ok(
                pedidoService.listarEmAtendimento()
        );
    }


    // ==========================================
    // FINALIZADOS
    // ==========================================

    @GetMapping("/pedidos/finalizados")
    public ResponseEntity<?> finalizados(
            HttpSession session) {

        if (!gerenteAutenticado(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "erro",
                            "Acesso permitido somente aos gerentes."
                    ));
        }

        return ResponseEntity.ok(
                pedidoService.listarFinalizados()
        );
    }
}