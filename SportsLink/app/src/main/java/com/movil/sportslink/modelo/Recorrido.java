package com.movil.sportslink.modelo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Recorrido implements Serializable {
    private LatLng puntoInicio;
    private LatLng puntoFinal;

    @Override
    public String toString() {
        return "Recorrido{" +
                "puntoInicio=" + puntoInicio.latitude +
                ", puntoFinal=" + puntoFinal.latitude +
                '}';
    }

    public Recorrido(LatLng puntoInicio, LatLng puntoFinal) {
        this.puntoInicio = puntoInicio;
        this.puntoFinal = puntoFinal;
    }
}
