package com.pedido.Model;


public class Pedido {
    //definimos las prioridades
    public enum Prioridad {
        BAJA,
        MEDIA,
        ALTA,
        URGENTE
    }
    //definimos los estados
    public enum Estado {
        PENDIENTE,
        CONFIRMADO,
        DESPACHADO,
        CANCELADO
    }
    private Long id;
    private String cliente;
    private Long productoId;
    private int cantidad;
    private Prioridad prioridad;
    private Estado estado;

    public Pedido() {
    }
    
    public Pedido(Long id, String cliente, Long productoId, int cantidad, Prioridad prioridad, Estado estado) {
        this.id = id;
        this.cliente = cliente;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.estado = estado;
    }
    //Definimos los getters
    public Long getId() {return id;}
    public String getCliente() {return cliente;}
    public Long getProductoId() {return productoId;}
    public int getCantidad() {return cantidad;}
    public Prioridad getPrioridad() {return prioridad;}
    public Estado getEstado() {return estado;}
    
    //Definimos los setters
    public void setId(Long id) {this.id = id;}
    public void setCliente(String cliente) {this.cliente = cliente;}
    public void setProductoId(Long productoId) {this.productoId = productoId;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}
    public void setPrioridad(Prioridad prioridad) {this.prioridad = prioridad;}
    public void setEstado(Estado estado) {this.estado = estado;}

    public static class Producto {

        private Long id;
        private int stock;

        public Producto() {}

        public Long getId() {return id;}
        public int getStock() {return stock;}

        public void setId(Long id) {this.id = id;}
        public void setStock(int stock) {this.stock = stock;}
    }
}