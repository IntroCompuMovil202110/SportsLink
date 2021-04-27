package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.movil.sportslink.R;
import com.movil.sportslink.adapters.EncuentrosAdapter;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.modelo.Encuentro;

import java.util.ArrayList;

public class ActividadesSegunPreferenciasFragment extends Fragment {

    ListView viewl;
    ArrayList<Encuentro> finalEncuentros;

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
        viewl = view.findViewById(R.id.listaInicio);
        PersistidorEncuentro per = new PersistidorEncuentro();

        if(per.encuentrosTodos != null)
        {
            finalEncuentros =  per.encuentrosTodos;
        }else
        {
            finalEncuentros = per.hacerEncuentros();
        }

        EncuentrosAdapter encuentrosAdapter = new EncuentrosAdapter(finalEncuentros,getContext());


        //handle listview and assign adapter

        viewl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), Detalle_EncuentroActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("nombre", finalEncuentros.get(position).getNombre() );
                bundle.putString("fecha", finalEncuentros.get(position).getFecha().toString());
                bundle.putString("actividad", finalEncuentros.get(position).getActividad().toString());
                bundle.putInt("latitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().latitude);
                bundle.putInt("longitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().longitude);

                intent.putExtra("Bundle",bundle);
                startActivity(intent);
            }
        });
        viewl.setAdapter(encuentrosAdapter);

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