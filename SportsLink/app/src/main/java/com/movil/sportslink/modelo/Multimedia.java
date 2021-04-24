package com.movil.sportslink.modelo;

public class Multimedia {

    private String src;
    private String location;

    public Multimedia(String src, String location) {
        this.src = src;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Multimedia{" +
                "src='" + src + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
