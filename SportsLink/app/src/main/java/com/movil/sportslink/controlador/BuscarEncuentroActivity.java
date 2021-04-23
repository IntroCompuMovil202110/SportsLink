package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.movil.sportslink.R;

public class BuscarEncuentroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_encuentro);
        Button buscarEncuentro = findViewById(R.id.buscarEncuentroButton);
        buscarEncuentro.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActividadesSegunPreferenciasActivity.class);
            startActivity(intent);
        });
    }
}