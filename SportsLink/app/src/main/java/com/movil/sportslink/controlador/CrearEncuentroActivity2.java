package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

public class CrearEncuentroActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro2);
        TextView atrasCrearEncuentroButton = findViewById(R.id.atrasCrearEncuentroButton2);
        Button crearEncuentroButton = findViewById(R.id.crearEncuentroButton);
        atrasCrearEncuentroButton.setOnClickListener(v -> finish());
        crearEncuentroButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Encuentro_Creador.class);
            startActivity(intent);
        });
    }
}