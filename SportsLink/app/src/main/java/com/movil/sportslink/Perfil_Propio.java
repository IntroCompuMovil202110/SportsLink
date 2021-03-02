package com.movil.sportslink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Perfil_Propio extends AppCompatActivity {
    Button editarPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__propio);

        editarPerfil = findViewById(R.id.buttonEditarPerfil);
        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), PreferenciasUsuario.class);
            startActivity(intent);
        });

    }
}