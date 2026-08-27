package com.pedido.pedido;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pedido.Model.Pedido;


@RestController
public class PedidoController {

    private List<Pedido> pedidos = new ArrayList<>();

    private Long siguienteId = 1L;

    @PostMapping("/pedido")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {

        pedido.setId(siguienteId++);
        pedido.setEstado(Pedido.Estado.PENDIENTE);

        pedidos.add(pedido);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedido);
    }

    @GetMapping("/pedido")
    public List<Pedido> obtenerPedidos() {

        return pedidos;
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId().equals(id)) {
                return ResponseEntity.ok(pedido);
            }
        }

        return ResponseEntity.notFound().build();
    }
    
    // ACTUALIZAR PEDIDO
    @PutMapping("/pedido/{id}")
    public ResponseEntity<Pedido> actualizarPedido(
            @PathVariable Long id,
            @RequestBody Pedido pedidoNuevo) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId().equals(id)) {

                pedido.setCliente(pedidoNuevo.getCliente());
                pedido.setProductoId(pedidoNuevo.getProductoId());
                pedido.setCantidad(pedidoNuevo.getCantidad());
                pedido.setPrioridad(pedidoNuevo.getPrioridad());

                return ResponseEntity.ok(pedido);
            }
        }

        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {

        for (int i = 0; i < pedidos.size(); i++) {

            if (pedidos.get(i).getId().equals(id)) {

                pedidos.remove(i);

                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

}