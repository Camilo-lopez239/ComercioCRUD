package com.pedido.pedido;

import java.util.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pedido.Model.Producto;
import com.pedido.Service.ProductoService;

@RestController
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    // CONSULTAR TODOS
    @GetMapping("/producto")
    public List<Producto> obtenerProductos() {

        return productoService.obtenerProductos();
    }

    // BUSCAR POR ID
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id) {

        Producto producto = productoService.buscarProducto(id);

        if (producto == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.ok(producto);
    }

    // CREAR
    @PostMapping("/producto")
    public ResponseEntity<Producto> agregarProducto(
            @RequestBody Producto producto) {

        Producto productoGuardado = productoService.agregarProducto(producto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoGuardado);
    }

    // ACTUALIZAR
    @PutMapping("/producto/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoNuevo) {

        Producto productoActualizado = productoService.actualizarProducto(id, productoNuevo);

        if (productoActualizado == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.ok(productoActualizado);
    }

    // ELIMINAR
    @DeleteMapping("/producto/{id}")
    public ResponseEntity<?> eliminarProducto(
            @PathVariable Long id) {

        boolean eliminado = productoService.eliminarProducto(id);

        if (!eliminado) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.noContent().build();
    }

    // VERIFICAR EXISTENCIA
    @GetMapping("/producto/{id}/existe")
    public ResponseEntity<Boolean> existeProducto(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.existeProducto(id));
    }

    // GET /producto/categoria/Electrónica
    @GetMapping("/producto/categoria/{categoria}")
    public List<Producto> obtenerPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    // GET /producto/buscar?nombre=pant
    @GetMapping("/producto/buscar")
    public List<Producto> obtenerPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    // GET /producto/stock-bajo?limite=5
    @GetMapping("/producto/stock-bajo")
    public List<Producto> obtenerPorStockBajo(@RequestParam int limite) {
        return productoService.buscarPorStockBajo(limite);
    }

}
