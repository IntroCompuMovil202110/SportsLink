package com.movil.sportslink.modelo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Recorrido {
    private LatLng puntoInicio;
    private LatLng puntoFinal;

    public Recorrido(LatLng puntoInicio, LatLng puntoFinal) {
        this.puntoInicio = puntoInicio;
        this.puntoFinal = puntoFinal;
    }
}
