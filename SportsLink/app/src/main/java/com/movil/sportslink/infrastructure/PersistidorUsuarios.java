package com.movil.sportslink.infrastructure;

import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;

public class PersistidorUsuarios {
    ArrayList<Usuario> usuarios;

    public void PersistidorUsuarios(){
        usuarios = obtenerUsuarios();
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> users = new ArrayList<>();

        /*Usuario u = new Usuario("Koz","32076477832");
        users.add(u);
        u = new Usuario("Carl","32016412541");
        users.add(u);
        u = new Usuario("Lyn","3106497839");
        users.add(u);
        u = new Usuario("Sam","31506431838");
        users.add(u);
        u = new Usuario("Van","31934873423");
        users.add(u);*/

        Usuario u = new Usuario("Koz","koz@gmail.com","32076477832","Me gustan las camintas");
        users.add(u);
        u = new Usuario("Carl","carl@gmail.com","32016412541","Quiero explorar montañas");
        users.add(u);
        u = new Usuario("Lyn","lyn@gmail.com","3106497839","Hola");
        users.add(u);
        u = new Usuario("Sam","sam@gmail.com","31506431838","Not your best app isn´t it?");
        users.add(u);
        u = new Usuario("Van","van@gmail.com","31934873423","No, not really my best project");
        users.add(u);

        return users;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
