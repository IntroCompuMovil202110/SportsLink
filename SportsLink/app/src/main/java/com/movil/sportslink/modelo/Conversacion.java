package com.movil.sportslink.modelo;

import java.util.ArrayList;

public class Conversacion {

    private ArrayList<Mensaje> mensajes;

    public Conversacion(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Conversacion(){
        mensajes = new ArrayList<>();
    }

    public void añadirMensaje(String msg, Usuario autor){
        Mensaje m = new Mensaje(autor,msg);
        mensajes.add(m);
    }
}
