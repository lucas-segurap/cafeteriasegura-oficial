package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.Gerente;
import com.br.cafeteriasegura.Repository.GerenteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GerenteService {

    private final GerenteRepository gerenteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public GerenteService(
            GerenteRepository gerenteRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.gerenteRepository = gerenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Gerente autenticar(
            String email,
            String senha) {

        Gerente gerente = gerenteRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "E-mail ou senha inválidos."
                        )
                );

        if (!passwordEncoder.matches(
                senha,
                gerente.getSenha())) {

            throw new RuntimeException(
                    "E-mail ou senha inválidos."
            );
        }

        return gerente;
    }

    public Gerente salvar(Gerente gerente) {

        gerente.setSenha(
                passwordEncoder.encode(
                        gerente.getSenha()
                )
        );

        return gerenteRepository.save(gerente);
    }
}