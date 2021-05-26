package com.movil.sportslink.modelo;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String id;
    private String name;
    private String lastName;
    private String numeroCelular;
    private String correo;
    private String descripcion;
    private ArrayList<String> encuentros;
    private Ubicacion ubicacion;


    public Usuario() {
    }

    public Usuario(String id, String name, String lastName, String numeroCelular, String correo, String descripcion, ArrayList<String> encuentros) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.numeroCelular = numeroCelular;
        this.correo = correo;
        this.descripcion = descripcion;
        this.encuentros = encuentros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<String> getEncuentros() {
        return encuentros;
    }

    public void setEncuentros(ArrayList<String> encuentros) {
        this.encuentros = encuentros;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
