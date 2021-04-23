package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.movil.sportslink.R;

public class Recorrido_Manual extends AppCompatActivity {
    Button crearRuta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido__manual);
        crearRuta = findViewById(R.id.buttonCrearRuta);
        crearRuta.setOnClickListener(v -> finish());
    }
}