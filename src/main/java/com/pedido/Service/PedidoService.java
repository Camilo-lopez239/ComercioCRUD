package com.pedido.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.pedido.Model.Pedido;
import com.pedido.Model.Producto;
import com.pedido.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado(Pedido.Estado.PENDIENTE);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarPedido(Long id, Pedido pedidoNuevo) {
        Pedido pedido = buscarPedido(id);
        if (pedido == null)
            return null;

        pedido.setCliente(pedidoNuevo.getCliente());
        pedido.setProductoId(pedidoNuevo.getProductoId());
        pedido.setCantidad(pedidoNuevo.getCantidad());
        pedido.setPrioridad(pedidoNuevo.getPrioridad());

        return pedidoRepository.save(pedido);
    }

    public boolean eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id))
            return false;
        pedidoRepository.deleteById(id);
        return true;
    }

    // ACCIÓN: CONFIRMAR PEDIDO
    public String confirmarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        if (pedido == null)
            return "NOT_FOUND";
        if (pedido.getEstado() != Pedido.Estado.PENDIENTE)
            return "NO_PENDIENTE";

        Producto producto = productoService.buscarProducto(pedido.getProductoId());
        if (producto == null)
            return "PRODUCTO_NO_EXISTE";

        if (producto.getStock() < pedido.getCantidad())
            return "SIN_STOCK";

        // Descontar stock y guardar
        producto.setStock(producto.getStock() - pedido.getCantidad());
        productoService.agregarProducto(producto);

        // Cambiar estado y guardar pedido
        pedido.setEstado(Pedido.Estado.CONFIRMADO);
        pedidoRepository.save(pedido);

        return "OK";
    }

    // ACCIÓN: CANCELAR PEDIDO
    public String cancelarPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        if (pedido == null)
            return "NOT_FOUND";

        if (pedido.getEstado() == Pedido.Estado.DESPACHADO)
            return "DESPACHADO";
        if (pedido.getEstado() == Pedido.Estado.CANCELADO)
            return "YA_CANCELADO";

        // Si estaba confirmado, reponer el stock
        if (pedido.getEstado() == Pedido.Estado.CONFIRMADO) {
            Producto producto = productoService.buscarProducto(pedido.getProductoId());
            if (producto != null) {
                producto.setStock(producto.getStock() + pedido.getCantidad());
                productoService.agregarProducto(producto);
            }
        }

        pedido.setEstado(Pedido.Estado.CANCELADO);
        pedidoRepository.save(pedido);

        return "OK";
    }

    // ACCIÓN: DESPACHAR PEDIDO
    public String despacharPedido(Long id) {
        Pedido pedido = buscarPedido(id);
        if (pedido == null)
            return "NOT_FOUND";

        if (pedido.getEstado() != Pedido.Estado.CONFIRMADO)
            return "NO_CONFIRMADO";

        pedido.setEstado(Pedido.Estado.DESPACHADO);
        pedidoRepository.save(pedido);

        return "OK";
    }

    // CONSULTAS ADICIONALES
    public List<Pedido> obtenerPendientes() {
        return pedidoRepository.findByEstado(Pedido.Estado.PENDIENTE);
    }

    public List<Pedido> obtenerUrgentes() {
        return pedidoRepository.findByPrioridad(Pedido.Prioridad.URGENTE);
    }

    public List<Pedido> obtenerPorEstado(Pedido.Estado estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public String generarResumen() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        int pendientes = 0, confirmados = 0, despachados = 0, cancelados = 0, urgentes = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == Pedido.Estado.PENDIENTE) {
                pendientes++;
            }

            if (pedido.getEstado() == Pedido.Estado.CONFIRMADO) {
                confirmados++;
            }

            if (pedido.getEstado() == Pedido.Estado.DESPACHADO) {
                despachados++;
            }

            if (pedido.getEstado() == Pedido.Estado.CANCELADO) {
                cancelados++;
            }

            if (pedido.getPrioridad() == Pedido.Prioridad.URGENTE) {
                urgentes++;
            }
        }

        return "Total de pedidos: " + pedidos.size()
                + "\nPendientes: " + pendientes
                + "\nConfirmados: " + confirmados
                + "\nDespachados: " + despachados
                + "\nCancelados: " + cancelados
                + "\nUrgentes: " + urgentes;
    }

    public Pedido obtenerSiguiente() {
        List<Pedido> pendientes = obtenerPendientes();
        Pedido siguiente = null;

        for (Pedido pedido : pendientes) {
            if (siguiente == null) {
                siguiente = pedido;
            } else {
                if (pedido.getPrioridad() == Pedido.Prioridad.URGENTE
                        && siguiente.getPrioridad() != Pedido.Prioridad.URGENTE) {
                    siguiente = pedido;
                } else if (pedido.getPrioridad() == Pedido.Prioridad.ALTA
                        && siguiente.getPrioridad() != Pedido.Prioridad.URGENTE
                        && siguiente.getPrioridad() != Pedido.Prioridad.ALTA) {
                    siguiente = pedido;
                } else if (pedido.getPrioridad() == Pedido.Prioridad.MEDIA
                        && siguiente.getPrioridad() == Pedido.Prioridad.BAJA) {
                    siguiente = pedido;
                } else if (pedido.getPrioridad() == siguiente.getPrioridad() && pedido.getId() < siguiente.getId()) {
                    siguiente = pedido;
                }
            }
        }
        return siguiente;
    }

    public List<Pedido> obtenerPedidosEnRiesgo() {
        List<Pedido> pendientes = obtenerPendientes();
        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : pendientes) {
            Producto producto = productoService.buscarProducto(pedido.getProductoId());
            if (producto != null && pedido.getCantidad() > producto.getStock()) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }
    
    public List<Pedido> buscarPorCliente(String cliente) {
        return pedidoRepository.findByClienteContainingIgnoreCase(cliente);
    }
}