package com.bosarreyes.edwars.app_gp.entidades;

import java.io.Serializable;

public class PuntoVenta implements Serializable {

    private String nombre;
    private String encargado;
    private String telefono;
    private String usuario;
    private String password;

    public PuntoVenta(String nombre, String usuario, String password) {
        this.nombre = nombre;
        this.encargado = encargado;
        this.telefono = telefono;
        this.usuario = usuario;
        this.password = password;
    }

    public PuntoVenta(){}

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
