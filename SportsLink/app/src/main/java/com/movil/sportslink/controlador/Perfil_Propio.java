package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;

import com.movil.sportslink.R;


 


public class Perfil_Propio extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button editarPerfil;
    Button cerrarSesion;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_perfil_propio);

        editarPerfil = findViewById(R.id.buttonEditarPerfil);
     
        cerrarSesion = findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();

        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, PreferenciasUsuario.class);
            startActivity(intent);
        });

        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        });

    }
}