package com.movil.sportslink.modelo;

import java.util.ArrayList;

public class Conversacion {
    private String id;
    private ArrayList<Mensaje> mensajes;
    private ArrayList<String> usuarios;

    public Conversacion() {
    }

    public Conversacion(String id, ArrayList<Mensaje> mensajes, ArrayList<String> usuarios) {
        this.id = id;
        this.mensajes = mensajes;
        this.usuarios = usuarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public ArrayList<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<String> usuarios) {
        this.usuarios = usuarios;
    }
}
