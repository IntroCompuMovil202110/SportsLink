package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;

public class Perfil_Propio extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button editarPerfil;
    Button cerrarSesion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__propio);

        editarPerfil = findViewById(R.id.buttonEditarPerfil);
        cerrarSesion = findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();

        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), PreferenciasUsuario.class);
            startActivity(intent);
        });

        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        });

    }
}