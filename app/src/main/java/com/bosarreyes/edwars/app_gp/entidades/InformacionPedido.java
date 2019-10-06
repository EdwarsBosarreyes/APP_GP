package com.bosarreyes.edwars.app_gp.entidades;

import java.io.Serializable;

public class InformacionPedido implements Serializable {
    private int numero;
    private String nombre;
    private String apellido;
    private String telefono;
    private String fecha;
    private String pedido;
    private double latitud;
    private double longitud;
    private String direccion;

    public InformacionPedido(int numero, String nombre, String apellido,String telefono, String fecha, String pedido, double latitud, double longitud, String direccion) {
        this.numero = numero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fecha = fecha;
        this.pedido = pedido;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }

    public InformacionPedido(){}

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
