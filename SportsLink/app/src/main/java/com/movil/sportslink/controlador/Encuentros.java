package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.movil.sportslink.R;

public class Encuentros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentros);
        Button perfilButton = findViewById(R.id.button3);
        Button paisajesButton = findViewById(R.id.button2);
        perfilButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Propio.class);
            startActivity(intent);
        });
        paisajesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Galeria.class);
            startActivity(intent);
        });
    }
}