package com.movil.sportslink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActividadesSegunPreferenciasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_segun_preferencias);
        Button paisajesButton = findViewById(R.id.paisajesButton);
        Button encuentroButton = findViewById(R.id.encuentroButton);
        Button perfilButton = findViewById(R.id.perfilButton);
        Button crearEncuentro = findViewById(R.id.crearButton);
        Button seleccionarActividadesButton = findViewById(R.id.seleccionarActividadesButton);
        paisajesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Galeria.class);
            startActivity(intent);
        });
        encuentroButton.setOnClickListener(v -> {
            /*Intent intent = new Intent(getBaseContext(), );
            startActivity(intent);*/
        });
        perfilButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Propio.class);
            startActivity(intent);
        });
        crearEncuentro.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), CrearEncuentroActivity1.class);
            startActivity(intent);
        });
        seleccionarActividadesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), BuscarEncuentroActivity.class);
            startActivity(intent);
        });
    }
}