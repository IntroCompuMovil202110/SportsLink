package com.movil.sportslink.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.EncuentrosAdapter;
import com.movil.sportslink.adapters.MiEncuentroAdapter;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.services.RecommendationService;

import java.util.ArrayList;
import java.util.List;

public class ActividadesSegunPreferenciasFragment extends AppCompatActivity {

    ListView viewl;
    ArrayList<Encuentro> finalEncuentros;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_actividades_segun_preferencias);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(this, ActividadesSegunPreferenciasFragment.class);
                startActivity(intent);
                /*fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ActividadesSegunPreferenciasFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();*/
            } else if (itemId == R.id.navigation_search) {
                Intent intent = new Intent(this, EncuentrosUsuarioFragment.class);
                startActivity(intent);
            } else if (itemId == R.id.navigation_chat) {
                Intent intent = new Intent(this, ConversacionesFragment.class);
                startActivity(intent);
            } else if (itemId == R.id.navigation_profile) {

                Intent intent = new Intent(this, Perfil_Propio.class);
                startActivity(intent);
            }
            return true;
        });
        bottomNavigationView.setItemIconTintList(null);

        //Button crearEncuentro = findViewById(R.id.crearButton);
        SearchView searchView= findViewById(R.id.actividadesSegunPreferenciasSearchView);
        viewl = findViewById(R.id.listaInicio);


        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        finalEncuentros = new ArrayList<>();



        //EncuentrosAdapter encuentrosAdapter = new EncuentrosAdapter(finalEncuentros,this);


        //handle listview and assign adapter

        viewl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ActividadesSegunPreferenciasFragment.this, Detalle_EncuentroActivity.class);
                Bundle bundle = new Bundle();

                /*bundle.putString("nombre", finalEncuentros.get(position).getNombre() );
                bundle.putString("fecha", finalEncuentros.get(position).getFecha().toString());
                bundle.putString("actividad", finalEncuentros.get(position).getActividad().toString());
                bundle.putInt("latitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().latitude);
                bundle.putInt("longitud", (int) finalEncuentros.get(position).getLugarEncuentro().getUbicacion().longitude);*/

                intent.putExtra("Bundle",bundle);
                startActivity(intent);
            }
        });

        obtenerObjectosEncuentro(new FirebaseCallBack() {
            @Override
            public void onCallBack(List<Encuentro> encuentros) {
                if(encuentros != null && !encuentros.isEmpty()){

                    System.out.println("Preparando adaptador");
                    EncuentrosAdapter miEncuentroAdapter = new EncuentrosAdapter((ArrayList<Encuentro>) encuentros,ActividadesSegunPreferenciasFragment.this);
                    viewl.setAdapter(miEncuentroAdapter);
                }
            }


                    /*@Override
                    public void onCallBack(List<User> users) {
                        if(usuarios != null && !usuarios.isEmpty()){
                            usuarios.removeIf(user -> (!user.isAvailability()));
                            usuarios.removeIf(user -> (user.getId().equals(userAu.getUid())));
                            MyCustomAdapter adapter = new MyCustomAdapter(usuarios, UsuariosDisponibles.this);
                            //handle listview and assign adapter
                            ListView lView = findViewById(R.id.listaConv);
                            lView.setAdapter(adapter);
                        }
                    }*/
        });
        //viewl.setAdapter(encuentrosAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(ActividadesSegunPreferenciasFragment.this, Buscar_Activity.class);
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
        /*crearEncuentro.setOnClickListener(v -> {

            Intent intent = new Intent(this, CrearEncuentro1Activity.class);
            startActivity(intent);
        });
       /* verActividades.setOnClickListener(v -> {
            Intent intent = new Intent(this, EncuentroActivity.class);
            startActivity(intent);
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(ActividadesSegunPreferenciasFragment.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(itemClicked == R.id.crearEncuentroButton){
            Intent intent = new Intent(this, CrearEncuentro1Activity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.sugg){
            RecommendationService.consumeRESTVolley(this);
        }
        return super.onOptionsItemSelected(item);
    }




    private void obtenerObjectosEncuentro(FirebaseCallBack firebaseCallBack) {
        System.out.println("Obtener encuentros");
        myRef = database.getReference("encuentros/");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    Encuentro enc = s.getValue(Encuentro.class);
                    finalEncuentros.add(enc);
                    System.out.println("Obtener encuentros " + enc.getId());
                }
                firebaseCallBack.onCallBack(finalEncuentros);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallBack{
        void onCallBack(List<Encuentro> encuentros);
    }
}