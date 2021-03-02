package com.movil.sportslink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class CrearEncuentroActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro1);
        Button atrasButton = findViewById(R.id.atrasCrearEncuentroButton);
        Button continuarButton = findViewById(R.id.continuarButton);
        atrasButton.setOnClickListener(v -> finish());
        continuarButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), CrearEncuentroActivity2.class);
            startActivity(intent);
        });
    }
}