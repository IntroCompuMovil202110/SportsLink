package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.UsuarioCrearChatGrupalAdapter;
import com.movil.sportslink.modelo.Usuario;
import com.movil.sportslink.services.RecommendationService;

import java.util.ArrayList;

public class CrearChatGrupalActivity extends AppCompatActivity {

    private ListView listView;
    private UsuarioCrearChatGrupalAdapter adapter = null;
    private Button crearChatGrupalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_chat_grupal);

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

        listView = findViewById(R.id.crearChatGrupalListView);
        crearChatGrupalButton = findViewById(R.id.finalizarCreacionChatGrupalButton);

        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Usuario> uid = new ArrayList<>();
                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot d : snapshot.getChildren()) {
                    String id = d.child("id").getValue().toString();
                    String name = d.child("name").getValue().toString();
                    if (id.equals(currentUid)) continue;
                    uid.add(new Usuario(id, name, null, null, null, null, null));
                }
                adapter = new UsuarioCrearChatGrupalAdapter(CrearChatGrupalActivity.this, uid);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        crearChatGrupalButton.setOnClickListener(v -> {
            Log.i("LOL", "1");
            if (adapter != null) {
                Log.i("LOL", "2");
                ArrayList<String> uids = adapter.getCheckedUsers();
                Log.i("LOL", "3");
                Log.i("LOL", "El adapter esta bien creo " + uids.size());
                if (uids.size() >= 2) {
                    Log.i("LOL", "Bien, crea el grupo");

                    Intent intent = new Intent(this, ChatActivity.class);
                    uids.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    intent.putStringArrayListExtra(ChatActivity.USERS_EXTRA_BUNDLE, uids);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("LOL", "Mal, no hay suficiente gente para crear el grupo");
                    Toast.makeText(getBaseContext(), "El chat grupal debe tener al menos 2 participantes", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("LOL", "El adapter es NULL");
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

            Intent intent = new Intent(CrearChatGrupalActivity.this, LoginActivity.class);
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