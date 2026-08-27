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

    private List<Pedido.Producto> productos = new ArrayList<>();

    //productos del inventario
    @PostMapping("/producto")
    public ResponseEntity<Pedido.Producto> agregarProducto(
            @RequestBody Pedido.Producto producto) {

        productos.add(producto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(producto);
    }


    @PostMapping("/pedido")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {

       
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

    @PutMapping("/pedido/{id}/confirmar")

    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {

        // Buscar el pedido
        for (Pedido pedido : pedidos) {

            if (pedido.getId().equals(id)) {

                // Comprobar que esté pendiente
                if (pedido.getEstado() != Pedido.Estado.PENDIENTE) {

                    return ResponseEntity
                            .badRequest()
                            .body("El pedido no está pendiente");
                }

                // Buscar el producto en el inventario
                for (Pedido.Producto producto : productos) {

                    if (producto.getId().equals(pedido.getProductoId())) {

                        // Comprobar stock
                        if (producto.getStock() < pedido.getCantidad()) {

                            return ResponseEntity
                                    .badRequest()
                                    .body("No hay stock suficiente");
                        }

                        // Descontar stock
                        int nuevoStock = producto.getStock() - pedido.getCantidad();

                        producto.setStock(nuevoStock);

                        // Cambiar estado del pedido
                        pedido.setEstado(Pedido.Estado.CONFIRMADO);

                        return ResponseEntity.ok(pedido);
                    }
                }

                // El producto no existe
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("El producto no existe");
            }
        }

        // El pedido no existe
        return ResponseEntity.notFound().build();
    }
}