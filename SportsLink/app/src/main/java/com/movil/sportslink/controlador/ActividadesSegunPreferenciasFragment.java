package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.movil.sportslink.R;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.modelo.Encuentro;

import java.util.ArrayList;

public class ActividadesSegunPreferenciasFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_actividades_segun_preferencias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button crearEncuentro = view.findViewById(R.id.crearButton);
        SearchView searchView= view.findViewById(R.id.actividadesSegunPreferenciasSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(getContext(), Buscar_Activity.class);
                intent.putExtra("query",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        //ImageButton verActividades = view.findViewById(R.id.ButtonVerActividad);
        crearEncuentro.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrearEncuentro1Activity.class);
            startActivity(intent);
        });
       /* verActividades.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Encuentro_Usuarios.class);
            startActivity(intent);
        });*/
    }
}