package com.movil.sportslink.modelo;

public class Ubicacion {
    private double latitude;
    private double longitude;
    private boolean availability;

    public Ubicacion(double latitude, double longitude, boolean availability) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.availability = availability;
    }

    public Ubicacion() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
