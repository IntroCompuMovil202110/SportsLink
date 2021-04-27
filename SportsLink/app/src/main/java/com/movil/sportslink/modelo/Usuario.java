package com.movil.sportslink.modelo;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String nombre;
    private String numeroCelular;
    private String correo;
    private String descripcion;
    private ArrayList<Encuentro> encuentros;
    private ArrayList<Conversacion> conversaciones;
    private Bitmap foto;

    public Usuario(String nombre, String correo, String numero,String descripcion) {
        this.nombre = nombre;
        this.numeroCelular = numero;
        this.correo = correo;
        this.descripcion = descripcion;
        encuentros = new ArrayList<>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
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

    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }

    public void setConversaciones(ArrayList<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
    }
}
