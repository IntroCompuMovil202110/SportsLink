package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import com.movil.sportslink.R;
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
        Button bttnShareLandscape = findViewById(R.id.bttnShareLandscape);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnMeets = findViewById(R.id.btnMeets);
        bttnShareLandscape.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), GenerarMultimedia.class);
            startActivity(intent);
        });
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Propio.class);
            startActivity(intent);
        });
        btnMeets.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Encuentros.class);
            startActivity(intent);
        });
    }
}