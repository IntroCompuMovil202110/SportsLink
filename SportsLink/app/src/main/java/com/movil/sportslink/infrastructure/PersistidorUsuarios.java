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

        Usuario u = new Usuario("Koz","koz@gmail.com","3207642","Me gustan las camintas");
        users.add(u);
        u = new Usuario("Carl","carl@gmail.com","32016411","Quiero explorar montañas");
        users.add(u);
        u = new Usuario("Lyn","lyn@gmail.com","31064978","Hola");
        users.add(u);
        u = new Usuario("Sam","sam@gmail.com","315064318","Not your best app isn´t it?");
        users.add(u);
        u = new Usuario("Van","van@gmail.com","319348734","No, not really my best project");
        users.add(u);

        return users;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
