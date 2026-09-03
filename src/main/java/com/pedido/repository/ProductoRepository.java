package com.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import com.pedido.Model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    // Búsqueda por categoría exacta
    List<Producto> findByCategoria(String categoria);

    // Búsqueda por coincidencia parcial de nombre ignorando mayúsculas/minúsculas
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Búsqueda de productos cuyo stock sea menor a un límite
    List<Producto> findByStockLessThan(int limite);
}
