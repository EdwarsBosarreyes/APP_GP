package com.bosarreyes.edwars.app_gp.entidades;

import java.io.Serializable;

public class Cilindro implements Serializable{
    private int cantidad;
    private String peso;
    private String valvula;
    private boolean selected;

    public Cilindro(int cantidad, String peso, String valvula, boolean selected) {
        this.cantidad = cantidad;
        this.peso = peso;
        this.valvula = valvula;
        this.selected = selected;
    }

    public Cilindro(int cantidad, String peso, String valvula) {
        this.cantidad = cantidad;
        this.peso = peso;
        this.valvula = valvula;
    }

    public Cilindro() {

    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
