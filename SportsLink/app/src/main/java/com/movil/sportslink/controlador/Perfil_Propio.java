package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.movil.sportslink.R;

public class Perfil_Propio extends Fragment {
    Button editarPerfil;

    @Nullable
=======
import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;

public class Perfil_Propio extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button editarPerfil;
    Button cerrarSesion;
<<<<<<< HEAD
>>>>>>> autenticacion
=======
>>>>>>> parent of d9b3dab (Update Perfil_Propio.java)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_propio, container, false);
    }

<<<<<<< HEAD
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editarPerfil = view.findViewById(R.id.buttonEditarPerfil);
=======
        editarPerfil = findViewById(R.id.buttonEditarPerfil);
        cerrarSesion = findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();

>>>>>>> autenticacion
        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PreferenciasUsuario.class);
            startActivity(intent);
        });
<<<<<<< HEAD
=======

        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        });

>>>>>>> autenticacion
    }
}