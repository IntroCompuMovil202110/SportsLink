package com.movil.sportslink.controlador;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.ContactsAdapter;
import com.movil.sportslink.adapters.MyCustomAdapter;
import com.movil.sportslink.adapters.UsersConvAdapter;
import com.movil.sportslink.infrastructure.PersistidorUsuarios;
import com.movil.sportslink.modelo.Usuario;
import com.movil.sportslink.services.RecommendationService;

import java.util.ArrayList;

public class ConversacionesFragment extends AppCompatActivity {

    String[] mProjection;
    Cursor mCursor;
    ContactsAdapter mContactsAdapter;
    ListView mlista;
    ArrayList<String> listContactos;

    private RecyclerView recyclerView;
    private UsersConvAdapter usersConvAdapter;

    private ListView listView;
    private ArrayList<Usuario> usuarioList;
    private Button chatsGrupales;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

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
                return true;
            } else if (itemId == R.id.navigation_profile) {

                Intent intent = new Intent(this, Perfil_Propio.class);
                startActivity(intent);
            }
            return true;
        });
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

        usuarioList = new ArrayList<Usuario>();
        PersistidorUsuarios pu = new PersistidorUsuarios();
        //System.out.println(pu.getUsuarios());
        if (usuarioList == null) {
            System.out.println("NULL O VACIO");
        }
        //MyCustomAdapter adapter = new MyCustomAdapter(usuarioList, getContext());

        //handle listview and assign adapter
        listView = findViewById(R.id.listaConv);
        chatsGrupales = findViewById(R.id.chatsGrupalesButton);
        obtenerUsuariosActualizaAdapter();
        chatsGrupales.setOnClickListener(v -> {
            Intent intent = new Intent(ConversacionesFragment.this, ChatsGrupalesActivity.class);
            startActivity(intent);
        });
        //lView.setAdapter(adapter);

    }

    private void obtenerUsuariosActualizaAdapter() {
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Usuario> usuarios = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String uid = data.child("id").getValue().toString();
                    String correo = data.child("correo").getValue().toString();
                    String descripcion = data.child("descripcion").getValue().toString();
                    String lastName = data.child("lastName").getValue().toString();
                    String name = data.child("name").getValue().toString();
                    String numeroCelular = data.child("numeroCelular").getValue().toString();

                    /*boolean availability = Boolean.parseBoolean(data.child("ubicacion").child("availability").getValue().toString());
                    double latitude = Double.parseDouble(data.child("ubicacion").child("latitude").getValue().toString());
                    double longitude = Double.parseDouble(data.child("ubicacion").child("longitude").getValue().toString());*/

                    Usuario usuario = new Usuario(uid, name, lastName, numeroCelular, correo, descripcion, null);
                    usuarios.add(usuario);
                }
                listView.setAdapter(new MyCustomAdapter(usuarios, ConversacionesFragment.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

            Intent intent = new Intent(ConversacionesFragment.this, LoginActivity.class);
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




}
