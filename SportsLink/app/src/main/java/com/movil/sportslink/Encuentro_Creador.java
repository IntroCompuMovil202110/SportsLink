package com.movil.sportslink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Encuentro_Creador extends AppCompatActivity {
    Button participante1;
    Button participante2;
    Button fotoLugar;
    Button modificar;
    Button editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentro__creador);
        participante1 = findViewById(R.id.buttonParticipante1);
        participante2 = findViewById(R.id.buttonParticipante2);
        fotoLugar = findViewById(R.id.ButtonFotoLugar);
        modificar = findViewById(R.id.buttonModificar);
        editar = findViewById(R.id.ButtonEditar);

        participante1.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        participante2.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        fotoLugar.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), GenerarMultimedia.class);
            startActivity(intent);
        });
        modificar.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Recorrido_Manual.class);
            startActivity(intent);
        });
        editar.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), CrearEncuentroActivity1.class);
            startActivity(intent);
        });
    }
}