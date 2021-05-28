package com.movil.sportslink.modelo;

public class Mensaje {

    private String idChat;
    private String autor;
    private String texto;

    public Mensaje(String idChat, String autor, String texto) {
        this.idChat = idChat;
        this.autor = autor;
        this.texto = texto;
    }

    public Mensaje(){

    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
