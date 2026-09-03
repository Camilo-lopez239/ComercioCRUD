package com.pedido.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedido.Model.Producto;
import com.pedido.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarProducto(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoNuevo) {

        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null) {return null;}
        producto.setNombre(productoNuevo.getNombre());
        producto.setCategoria(productoNuevo.getCategoria());
        producto.setStock(productoNuevo.getStock());

        return productoRepository.save(producto);
    }

    public boolean eliminarProducto(Long id) {

        if (!productoRepository.existsById(id)) {
            return false;
        }

        productoRepository.deleteById(id);

        return true;
    }

    public boolean existeProducto(Long id) {
        return productoRepository.existsById(id);
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorStockBajo(int limite) {
        return productoRepository.findByStockLessThan(limite);
    }
}
