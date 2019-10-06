package com.bosarreyes.edwars.app_gp.entidades;

import java.io.Serializable;

public class Producto implements Serializable{
    private String peso;
    private String valvula;
    private String precio;
    private int imagen;


    public Producto(String peso, String valvula, String precio, int imagen) {
        this.peso = peso;
        this.valvula = valvula;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Producto(){}

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getValvula() {
        return valvula;
    }

    public void setValvula(String valvula) {
        this.valvula = valvula;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
