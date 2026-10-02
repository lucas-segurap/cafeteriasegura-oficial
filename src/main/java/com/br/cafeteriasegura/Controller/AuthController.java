package com.br.cafeteriasegura.Controller;

import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final ClienteService clienteService;

    public AuthController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ==========================================
    // CADASTRO
    // ==========================================

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(
            @RequestBody Cliente cliente) {

        try {

            Cliente novoCliente =
                    clienteService.cadastrar(cliente);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensagem", "Cadastro realizado com sucesso!"
                    ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "erro", e.getMessage()
                    ));
        }
    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest dados,
            HttpSession session) {

        Cliente cliente =
                clienteService.autenticar(
                        dados.email(),
                        dados.senha()
                );

        if (cliente == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "erro",
                            "E-mail ou senha inválidos."
                    ));
        }

        session.setAttribute(
                "clienteId",
                cliente.getId()
        );

        return ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Login realizado com sucesso!",
                        "id",
                        cliente.getId(),
                        "nome",
                        cliente.getNome()
                )
        );
    }


    // ==========================================
    // USUÁRIO LOGADO
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<?> usuarioLogado(
            HttpSession session) {

        Long clienteId =
                (Long) session.getAttribute("clienteId");

        if (clienteId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "erro",
                            "Usuário não autenticado."
                    ));
        }

        Cliente cliente =
                clienteService.buscarPorId(clienteId);

        if (cliente == null) {

            session.invalidate();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "erro",
                            "Usuário não encontrado."
                    ));
        }

        return ResponseEntity.ok(
                Map.of(
                        "id", cliente.getId(),
                        "nome", cliente.getNome(),
                        "telefone", cliente.getTelefone(),
                        "endereco", cliente.getEndereco(),
                        "email", cliente.getEmail()
                )
        );
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Logout realizado com sucesso."
                )
        );
    }


    // ==========================================
    // DTO DO LOGIN
    // ==========================================

    public record LoginRequest(
            String email,
            String senha
    ) {
    }
}