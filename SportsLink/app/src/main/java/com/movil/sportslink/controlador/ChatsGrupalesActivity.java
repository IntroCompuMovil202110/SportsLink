package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.ChatGrupalAdapter;
import com.movil.sportslink.modelo.Conversacion;
import com.movil.sportslink.services.RecommendationService;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class ChatsGrupalesActivity extends AppCompatActivity {

    private ListView listView;
    private Button crearChatGrupalButton;
    private TreeMap<String, String> uidToNameMapping = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_grupales);

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

        listView = findViewById(R.id.chatsGrupalesListView);
        crearChatGrupalButton = findViewById(R.id.crearChatGrupalButton);

        crearChatGrupalButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearChatGrupalActivity.class);
            startActivity(intent);
        });
        FirebaseDatabase.getInstance().getReference("conversaciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Conversacion> conversaciones = new ArrayList<>();
                String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot d : snapshot.getChildren()) {
                    String id = d.child("id").getValue().toString();
                    ArrayList<String> usuariosUid = new ArrayList<>();
                    for (DataSnapshot u : d.child("usuarios").getChildren()) {
                        usuariosUid.add(u.getValue().toString());
                    }
                    if (usuariosUid.contains(currentUserUid) && usuariosUid.size() > 2) {
                        conversaciones.add(new Conversacion(id, null, usuariosUid));
                    }
                }
                createUidToNameMapping(conversaciones);
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

            Intent intent = new Intent(ChatsGrupalesActivity.this, LoginActivity.class);
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

    private void createUidToNameMapping(ArrayList<Conversacion> conversaciones) {
        TreeSet<String> uids = new TreeSet<>();
        for (Conversacion c : conversaciones) {
            for (String uidUser : c.getUsuarios()) {
                uids.add(uidUser);
            }
        }
        for (String uid : uids) {
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    uidToNameMapping.put(uid, snapshot.getValue().toString());
                    if (uidToNameMapping.size() == uids.size()) {
                        listView.setAdapter(new ChatGrupalAdapter(ChatsGrupalesActivity.this, conversaciones, uidToNameMapping));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}