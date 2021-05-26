package com.movil.sportslink.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Encuentro implements Serializable {

    private String id;
    private String nombre;
    private String autor;
    private String fecha;
    private String hora;
    private double latPuntoEncuentro;
    private double lngPuntoEncuentro;
    private double latPuntoFinal;
    private double lngPuntoFinal;
    private String municipio;
    private String actividad;
    private ArrayList<Participante> participantes;


    public Encuentro() {
    }

    public Encuentro(String nombre, String autor, String fecha, String hora, double latPuntoEncuentro, double lngPuntoEncuentro, double latPuntoFinal, double lngPuntoFinal, String municipio, String actividad) {
        this.nombre = nombre;
        this.autor = autor;
        this.fecha = fecha;
        this.hora = hora;

        this.latPuntoEncuentro = latPuntoEncuentro;
        this.lngPuntoEncuentro = lngPuntoEncuentro;
        this.latPuntoFinal = latPuntoFinal;
        this.lngPuntoFinal = lngPuntoFinal;
        this.municipio = municipio;
        this.actividad = actividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public double getLatPuntoEncuentro() {
        return latPuntoEncuentro;
    }

    public void setLatPuntoEncuentro(double latPuntoEncuentro) {
        this.latPuntoEncuentro = latPuntoEncuentro;
    }

    public double getLngPuntoEncuentro() {
        return lngPuntoEncuentro;
    }

    public void setLngPuntoEncuentro(double lngPuntoEncuentro) {
        this.lngPuntoEncuentro = lngPuntoEncuentro;
    }

    public double getLatPuntoFinal() {
        return latPuntoFinal;
    }

    public void setLatPuntoFinal(double latPuntoFinal) {
        this.latPuntoFinal = latPuntoFinal;
    }

    public double getLngPuntoFinal() {
        return lngPuntoFinal;
    }

    public void setLngPuntoFinal(double lngPuntoFinal) {
        this.lngPuntoFinal = lngPuntoFinal;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public ArrayList<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Participante> participantes) {
        this.participantes = participantes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
