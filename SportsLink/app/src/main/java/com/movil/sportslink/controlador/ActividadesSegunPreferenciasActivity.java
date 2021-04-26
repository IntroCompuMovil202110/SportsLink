package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.movil.sportslink.R;

public class ActividadesSegunPreferenciasFragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button crearEncuentro = findViewById(R.id.crearButton);
        ImageButton verActividades = findViewById(R.id.ButtonVerActividad);
        crearEncuentro.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearEncuentro1Activity.class);
            startActivity(intent);
        });
        verActividades.setOnClickListener(v -> {
            Intent intent = new Intent(this, Encuentro_Usuarios.class);
            startActivity(intent);
        });
    }
}