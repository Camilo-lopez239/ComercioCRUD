package com.pedido.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pedido.Model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(Pedido.Estado estado);

    List<Pedido> findByPrioridad(Pedido.Prioridad prioridad);

    List<Pedido> findByClienteContainingIgnoreCase(String cliente);
}
