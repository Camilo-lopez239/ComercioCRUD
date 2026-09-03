package com.pedido.pedido;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pedido.Model.Pedido;
import com.pedido.Service.PedidoService;
import com.pedido.Service.ProductoService;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @PostMapping("/pedido")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El cliente es obligatorio");
        }
        if (pedido.getCantidad() <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor que cero");
        }
        if (pedido.getProductoId() == null) {
            return ResponseEntity.badRequest().body("El producto es obligatorio");
        }
        if (pedido.getPrioridad() == null) {
            return ResponseEntity.badRequest().body("La prioridad es obligatoria");
        }

        if (!productoService.existeProducto(pedido.getProductoId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }

        Pedido nuevo = pedidoService.crearPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/pedido")
    public List<Pedido> obtenerPedidos() {
        return pedidoService.obtenerPedidos();
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedido(id);
        if (pedido == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/pedido/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedidoNuevo) {
        Pedido actualizado = pedidoService.actualizarPedido(id, pedidoNuevo);
        if (actualizado == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        boolean eliminado = pedidoService.eliminarPedido(id);
        if (!eliminado)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pedido/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {
        String resultado = pedidoService.confirmarPedido(id);

        switch (resultado) {
            case "NOT_FOUND":
                return ResponseEntity.notFound().build();
            case "NO_PENDIENTE":
                return ResponseEntity.badRequest().body("El pedido no está pendiente");
            case "PRODUCTO_NO_EXISTE":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
            case "SIN_STOCK":
                return ResponseEntity.badRequest().body("No hay stock suficiente");
            default:
                return ResponseEntity.ok(pedidoService.buscarPedido(id));
        }
    }

    @PutMapping("/pedido/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        String resultado = pedidoService.cancelarPedido(id);

        switch (resultado) {
            case "NOT_FOUND":
                return ResponseEntity.notFound().build();
            case "DESPACHADO":
                return ResponseEntity.badRequest().body("No se puede cancelar un pedido despachado");
            case "YA_CANCELADO":
                return ResponseEntity.badRequest().body("El pedido ya está cancelado");
            default:
                return ResponseEntity.ok(pedidoService.buscarPedido(id));
        }
    }

    @PutMapping("/pedido/{id}/despachar")
    public ResponseEntity<?> despacharPedido(@PathVariable Long id) {
        String resultado = pedidoService.despacharPedido(id);

        switch (resultado) {
            case "NOT_FOUND":
                return ResponseEntity.notFound().build();
            case "NO_CONFIRMADO":
                return ResponseEntity.badRequest().body("Solo se puede despachar un pedido confirmado");
            default:
                return ResponseEntity.ok(pedidoService.buscarPedido(id));
        }
    }

    @GetMapping("/pedido/pendientes")
    public List<Pedido> obtenerPendientes() {
        return pedidoService.obtenerPendientes();
    }

    @GetMapping("/pedido/urgentes")
    public List<Pedido> obtenerUrgentes() {
        return pedidoService.obtenerUrgentes();
    }

    @GetMapping("/pedido/estado")
    public List<Pedido> obtenerPorEstado(@RequestParam Pedido.Estado estado) {
        return pedidoService.obtenerPorEstado(estado);
    }

    @GetMapping("/pedido/resumen")
    public String resumen() {
        return pedidoService.generarResumen();
    }

    @GetMapping("/pedido/siguiente")
    public ResponseEntity<?> siguientePedido() {
        Pedido siguiente = pedidoService.obtenerSiguiente();
        if (siguiente == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(siguiente);
    }

    @GetMapping("/pedido/en-riesgo")
    public List<Pedido> pedidosEnRiesgo() {
        return pedidoService.obtenerPedidosEnRiesgo();
    }

    
    @GetMapping("/pedido/buscar/cliente")
    public List<Pedido> buscarPorCliente(@RequestParam String nombre) {
        return pedidoService.buscarPorCliente(nombre);
    }
}