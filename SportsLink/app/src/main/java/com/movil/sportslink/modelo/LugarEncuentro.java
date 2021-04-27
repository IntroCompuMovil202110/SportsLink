package com.movil.sportslink.modelo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LugarEncuentro  implements Serializable {


    private LatLng ubicacion;


    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "LugarEncuentro{" +
                "ubicacion=" + ubicacion.latitude +
                '}';
    }

    public LugarEncuentro(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
