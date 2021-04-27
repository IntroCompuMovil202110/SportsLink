package com.movil.sportslink.controlador;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import static android.app.Activity.RESULT_OK;

public class Perfil_Propio extends Fragment {
    Button editarPerfil;


    @Nullable

    FirebaseAuth mAuth;

    Usuario usuarioPerfil;
    Button cerrarSesion;
    ImageView image;
    TextView nombre, desc;
    Button cambiarImg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_propio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editarPerfil = view.findViewById(R.id.buttonEditarPerfil);
        Bundle bundle= getActivity().getIntent().getBundleExtra("bundle");

        image = view.findViewById(R.id.image);
        nombre = view.findViewById(R.id.textViewNombreUsuario);
        desc = view.findViewById(R.id.descripcion);
        cambiarImg = view.findViewById(R.id.cambiarImagen);

        /*try{


            usuarioPerfil = (Usuario) getActivity().getIntent().getSerializableExtra("user");

            if(usuarioPerfil.getFoto() != null){
                image.setImageBitmap(usuarioPerfil.getFoto());
            }
            nombre.setText(usuarioPerfil.getNombre());
            desc.setText(usuarioPerfil.getDescripcion());
        }catch(Exception e){
            usuarioPerfil = null;
        }*/

        cerrarSesion = view.findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        nombre.setText(email);

        editarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PreferenciasUsuario.class);
            startActivity(intent);
        });


        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        });

        cambiarImg.setOnClickListener(v -> {
            Intent cambiarFoto = new Intent(PictureActivity.class.getName());
            startActivityForResult(cambiarFoto,2);
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                // OR
                // String returnedResult = data.getDataString();
            }
        }
    }


}