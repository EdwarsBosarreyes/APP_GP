package com.bosarreyes.edwars.app_gp.entidades;

import java.io.Serializable;

public class Pedido implements Serializable {

    private String pedido;
    private String fecha;
    private String estado;

    public Pedido(String pedido, String fecha, String estado) {
        this.pedido = pedido;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Pedido(){}

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
