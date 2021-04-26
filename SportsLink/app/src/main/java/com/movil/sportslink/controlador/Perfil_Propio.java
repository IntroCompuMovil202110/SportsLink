package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;

public class Perfil_Propio extends Fragment {
    Button editarPerfil;

    @Nullable





    FirebaseAuth mAuth;


    Button cerrarSesion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_propio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editarPerfil = view.findViewById(R.id.buttonEditarPerfil);


        cerrarSesion = view.findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();


        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PreferenciasUsuario.class);
            startActivity(intent);
        });


        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        });


    }
}