package com.movil.sportslink.modelo;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Encuentro implements Serializable {
    private LocalDateTime fecha;
    private int capacidad;
    private String nombre;
    //private ArrayList<Usuario> participantes;
    private LugarEncuentro lugarEncuentro;
    private Recorrido recorrido;
   // private  Usuario autor;
    private Actividad actividad;

    public Encuentro(LocalDateTime fecha, int capacidad, String nombre, LugarEncuentro lugarEncuentro, Usuario autor, Actividad actividad) {
        this.fecha = fecha;
        this.capacidad = capacidad;
        this.nombre = nombre;

        this.lugarEncuentro = lugarEncuentro;

        //this.autor = autor;
        this.actividad = actividad;
    }

    public Encuentro() {
        this.fecha = LocalDateTime.now();
        this.capacidad = 0;
        this.nombre = "admin";
        this.lugarEncuentro = new LugarEncuentro(new LatLng(4,12));
        this.recorrido = new Recorrido(new LatLng(4,12),new LatLng(-4,12));
        this.actividad = Actividad.CICLISMO;

    }

    @Override
    public String toString() {
        return "Encuentro{" +
                "fecha=" + fecha +
                ", capacidad=" + capacidad +
                ", nombre='" + nombre + '\'' +
                ", lugarEncuentro=" + lugarEncuentro +
                ", recorrido=" + recorrido +
                ", actividad=" + actividad +
                '}';
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public ArrayList<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Usuario> participantes) {
        this.participantes = participantes;
    }*/

    public LugarEncuentro getLugarEncuentro() {
        return lugarEncuentro;
    }

    public void setLugarEncuentro(LugarEncuentro lugarEncuentro) {
        this.lugarEncuentro = lugarEncuentro;
    }

    public Recorrido getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(Recorrido recorrido) {
        this.recorrido = recorrido;
    }

   /* public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }*/

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
