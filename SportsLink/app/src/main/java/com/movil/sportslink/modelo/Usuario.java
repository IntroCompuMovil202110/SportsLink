package com.movil.sportslink.modelo;

import java.util.ArrayList;

public class Usuario {

    private String nombre;
    private int numeroCelular;
    private String correo;
    private ArrayList<Encuentro> encuentros;

    public Usuario(String nombre) {
        this.nombre = nombre;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(int numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ArrayList<Encuentro> getEncuentros() {
        return encuentros;
    }

    public void setEncuentros(ArrayList<Encuentro> encuentros) {
        this.encuentros = encuentros;
    }
}
