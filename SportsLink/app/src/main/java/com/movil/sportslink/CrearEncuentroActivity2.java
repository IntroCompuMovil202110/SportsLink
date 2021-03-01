package com.movil.sportslink;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearEncuentroActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro2);
        TextView atrasCrearEncuentroButton = findViewById(R.id.atrasCrearEncuentroButton2);
        atrasCrearEncuentroButton.setOnClickListener(v -> finish());
    }
}