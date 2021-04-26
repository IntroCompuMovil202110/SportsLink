package com.movil.sportslink.modelo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LugarEncuentro {
    private String municipio;
    private String direccion;
    private LatLng ubicacion;

    public LugarEncuentro(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
