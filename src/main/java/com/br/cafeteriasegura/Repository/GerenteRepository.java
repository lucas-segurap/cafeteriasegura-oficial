package com.br.cafeteriasegura.Repository;

import com.br.cafeteriasegura.Model.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {

    Optional<Gerente> findByEmail(String email);
}