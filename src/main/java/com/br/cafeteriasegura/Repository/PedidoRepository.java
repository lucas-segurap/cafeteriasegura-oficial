package com.br.cafeteriasegura.Repository;

import com.br.cafeteriasegura.Model.Pedido;
import com.br.cafeteriasegura.Model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByStatus(StatusPedido status);
}