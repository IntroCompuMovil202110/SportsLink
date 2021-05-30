package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.EncuentrosAdapter;
import com.movil.sportslink.adapters.MiEncuentroAdapter;
import com.movil.sportslink.adapters.MyCustomAdapter;
import com.movil.sportslink.adapters.UsuarioAdapter;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.infrastructure.PersistidorUsuarios;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Usuario;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Buscar_Activity extends AppCompatActivity {
    EncuentrosAdapter encuentrosAdapter;
    UsuarioAdapter usuarioAdapter;
    TextView resutladoEncuentro;
    ListView listaEncuentros;
    ArrayList<Encuentro> encuen;
    ArrayList<Encuentro> finalEncuentros;
    ArrayList<Usuario> finalUsuarios;
    Button boton;
    String query;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_);
        boton = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        query = getIntent().getStringExtra("query");
        finalEncuentros = new ArrayList<>();
        finalUsuarios = new ArrayList<>();
        buscarEncuentros();
    }

    private void inflarEncuentros() {
        encuentrosAdapter = new EncuentrosAdapter(finalEncuentros, this);


        //handle listview and assign adapter
        ListView lView = findViewById(R.id.listaEncuentrosDynamic);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getBaseContext(), Detalle_EncuentroActivity.class);
                Bundle bundle = new Bundle();

                /*bundle.putString("nombre", finalEncuentros.get(position).getNombre() );
                bundle.putString("fecha", finalEncuentros.get(position).getFecha().toString());
                bundle.putString("actividad", finalEncuentros.get(position).getActividad().toString());
                bundle.putInt("latitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().latitude);
                bundle.putInt("longitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().longitude);*/

                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        lView.setAdapter(encuentrosAdapter);
    }

    public void buscarPersona(View view) {

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finalUsuarios.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.i("Holaaaa", ds.getKey());
                        Log.i("Holaaaa", ds.getValue().toString());
                        Log.i("listaaaaaa", String.valueOf(finalUsuarios));
                        if (ds.child("name").getValue().toString().contains(query)) {
                            Log.i("lista", String.valueOf(finalEncuentros));
                            Usuario enc = ds.getValue(Usuario.class);
                            finalUsuarios.add(enc);
                            //usuarios.add(new Usuario(nombre, latitud,longitud,estado,path,ds.getKey()));
                        }

                    }
                    inflarUsuarios();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inflarUsuarios() {
        usuarioAdapter = new UsuarioAdapter(finalUsuarios, this);


        //handle listview and assign adapter
        ListView lView = findViewById(R.id.listaEncuentrosDynamic);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getBaseContext(), Detalle_EncuentroActivity.class);
                Bundle bundle = new Bundle();

                /*bundle.putString("nombre", finalEncuentros.get(position).getNombre() );
                bundle.putString("fecha", finalEncuentros.get(position).getFecha().toString());
                bundle.putString("actividad", finalEncuentros.get(position).getActividad().toString());
                bundle.putInt("latitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().latitude);
                bundle.putInt("longitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().longitude);*/

                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        lView.setAdapter(usuarioAdapter);
    }
    public void buscarEncuentros(){
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date(System.currentTimeMillis());

        mDatabase.child("encuentros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finalEncuentros.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.i("Holaaaa", ds.getKey());
                        Log.i("Holaaaa", ds.getValue().toString());
                        try {
                            Date date = formatter1.parse((String) ds.child("fecha").getValue());
                            Log.i("listaaaaaa", String.valueOf(finalEncuentros));
                            if (date.after(now) && ds.child("nombre").getValue().toString().contains(query)) {
                                Log.i("lista", String.valueOf(finalEncuentros));
                                Encuentro enc = ds.getValue(Encuentro.class);
                                finalEncuentros.add(enc);
                                //usuarios.add(new Usuario(nombre, latitud,longitud,estado,path,ds.getKey()));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    inflarEncuentros();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void buscarEncuentros(View view) {
        buscarEncuentros();
    }
}