package com.movil.sportslink.infrastructure;

import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;
import com.movil.sportslink.modelo.Actividad;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.LugarEncuentro;
import com.movil.sportslink.modelo.Recorrido;
import com.movil.sportslink.modelo.Usuario;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class PersistidorEncuentro {

    public static ArrayList<Encuentro> encuentrosTodos = new ArrayList<Encuentro>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PersistidorEncuentro(){
        encuentrosTodos = hacerEncuentros();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Encuentro> hacerEncuentros(){
        ArrayList<Encuentro> encuentros = new ArrayList<>();

        LocalDateTime t = LocalDateTime.of(2021,
                Month.JULY, 29, 19, 30, 40);
        LatLng l = new LatLng(4.655339, -74.008268);
        LugarEncuentro lugar = new LugarEncuentro(l);
        Usuario u = new Usuario("jin","jiin@gmail.com","31324782394","Me gusta crear actividades");
        Encuentro enc = new Encuentro(t,5,"Un recorrido",lugar,u, Actividad.CICLISMO);
        encuentros.add(enc);

        t = LocalDateTime.of(2021,
                Month.JUNE, 01, 8, 10, 0);
        l = new LatLng(4.671247217956718, -74.03870764126252);
        enc = new Encuentro(t,4,"Viaje",new LugarEncuentro(l),u, Actividad.SENDERISMO);
        encuentros.add(enc);
        Recorrido recorrido = new Recorrido(l,new LatLng(4.9325035073728305, -73.83278120802645));

        t = LocalDateTime.of(2021,
                Month.JUNE, 02, 7, 0, 0);
        l = new LatLng(4.598165607309617, -74.0649054086511);
        enc = new Encuentro(t,8,"Hiking por monserrate",new LugarEncuentro(l),u, Actividad.SENDERISMO);
        encuentros.add(enc);
        System.out.println("Hay "+encuentros.size());
        return encuentros;
    }

    public ArrayList<Encuentro> getEncuentrosTodos() {
        return encuentrosTodos;
    }

    public void añadirEncuentro(Encuentro encuentro){
        Log.i("mirar", "encuentro.toString()");
        if(encuentrosTodos.isEmpty()){
           encuentrosTodos = hacerEncuentros();
            Log.i("lol", "encuentro.toString()");
        }

        encuentrosTodos.add(encuentro);
        for (Encuentro encuentros:encuentrosTodos) {
                  Log.i("Encuentros",encuentros.toString());
        }
    }
}
