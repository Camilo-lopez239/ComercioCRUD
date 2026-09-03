package com.pedido.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pedido {
    //definimos las prioridades
    public enum Prioridad {BAJA, MEDIA, ALTA, URGENTE}
    //definimos los estados
    public enum Estado {PENDIENTE, CONFIRMADO, DESPACHADO, CANCELADO}
    
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cliente;
    private Long productoId;
    private int cantidad;
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;
    @Enumerated(EnumType.STRING)
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

    
}