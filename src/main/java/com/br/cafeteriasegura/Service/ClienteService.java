package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Cliente;
import com.br.cafeteriasegura.Repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClienteService(
            ClienteRepository clienteRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente cadastrar(Cliente cliente) {

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }

        cliente.setSenha(
                passwordEncoder.encode(cliente.getSenha())
        );

        return clienteRepository.save(cliente);
    }

    public Cliente autenticar(String email, String senha) {

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("E-mail ou senha inválidos.")
                );

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        return cliente;
    }

    public Cliente buscarPorId(Long id) {

        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cliente não encontrado.")
                );
    }
}