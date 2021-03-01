package com.movil.sportslink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class CrearEncuentroActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro1);
        Button atrasButton = findViewById(R.id.atrasCrearEncuentroButton);
        atrasButton.setOnClickListener(v -> finish());
    }
}