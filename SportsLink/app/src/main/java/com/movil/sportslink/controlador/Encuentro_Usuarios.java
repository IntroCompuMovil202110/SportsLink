package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.movil.sportslink.R;

public class Encuentro_Usuarios extends AppCompatActivity {
    Button participante3;
    Button participante4;
    Button ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentro__usuarios);
        participante3 = findViewById(R.id.buttonParticipante3);
        participante4 = findViewById(R.id.buttonParticipante4);
        ruta = findViewById(R.id.buttonRuta);

        participante3.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        participante4.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        ruta.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), EnrutamientoEncuentro.class);
            startActivity(intent);
        });
    }
}