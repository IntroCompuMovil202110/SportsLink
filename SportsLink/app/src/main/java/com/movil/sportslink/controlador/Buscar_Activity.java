package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.adapters.EncuentrosAdapter;
import com.movil.sportslink.adapters.MyCustomAdapter;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.infrastructure.PersistidorUsuarios;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Buscar_Activity extends AppCompatActivity {
    EncuentrosAdapter encuentrosAdapter;
    TextView resutladoEncuentro;
    ListView listaEncuentros;
    ArrayList<Encuentro> encuen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_);
        String query = getIntent().getStringExtra("query");
        if(PersistidorEncuentro.encuentrosTodos != null)
        {
            encuen =  PersistidorEncuentro.encuentrosTodos;
        }else
        {
            encuen = PersistidorEncuentro.hacerEncuentros();
        }
        Log.i("lista", String.valueOf(encuen.size()));
        ArrayList<Encuentro> finalEncuentros = new ArrayList<Encuentro>();
        for(Encuentro encuentros: encuen){
            if(encuentros.getNombre().contains(query)){
                finalEncuentros.add(encuentros);
            }
        }
        encuentrosAdapter  = new EncuentrosAdapter(finalEncuentros, this);

        //handle listview and assign adapter
        ListView lView = findViewById(R.id.listaEncuentrosDynamic);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getBaseContext(), Detalle_EncuentroActivity.class);
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
        lView.setAdapter(encuentrosAdapter);
    }
}