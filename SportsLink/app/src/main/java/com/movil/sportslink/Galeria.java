package com.movil.sportslink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.movil.sportslink.modelo.Multimedia;

import java.util.ArrayList;

public class Galeria extends AppCompatActivity {

    private GridView gridPaisajes;
    private MultimediaAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        gridPaisajes = (GridView) findViewById(R.id.gridPaisajes);
        ArrayList<Multimedia> paisajes = new ArrayList<>();
        Multimedia paisajeActual = new Multimedia("../res/drawable-v24/ejmplo_paisaje.jpg", "Neiva");
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajeActual = new Multimedia("../res/drawable-v24/ejmplo_paisaje.jpg", "Cucuta");
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);
        paisajes.add(paisajeActual);

        adapter = new MultimediaAdapter(this, paisajes);
        gridPaisajes.setAdapter(adapter);
    }
}