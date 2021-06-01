package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Participante;
import com.movil.sportslink.modelo.Ubicacion;
import com.movil.sportslink.modelo.Usuario;
import com.movil.sportslink.services.RecommendationService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.movil.sportslink.controlador.SignUpActivity.PATH_USERS;

public class EncuentroActivity extends AppCompatActivity {
    private static final String PATH_MEETINGS = "encuentros/";
    Button participante3;
    Button participante4;
    Button unirse;
    Button ruta;

    private String id;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentro__usuarios);
        id = getIntent().getStringExtra("ID");

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

        unirse = findViewById(R.id.unirse);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        /*participante3 = findViewById(R.id.buttonParticipante3);
        participante4 = findViewById(R.id.buttonParticipante4);
        ruta = findViewById(R.id.buttonRuta);

        participante3.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        participante4.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Perfil_Vista.class);
            startActivity(intent);
        });
        ruta.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), EnrutamientoEncuentro.class);
            startActivity(intent);
        });*/
    }

    public void iniciarEncuentro(View view){
        myRef = database.getReference("estadoEventos/" + id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRef.setValue(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void agregarUsuarioParticipante(View view){
        FirebaseUser user = mAuth.getCurrentUser();

        myRef = database.getReference(PATH_MEETINGS + id + "/participantes/");
        DatabaseReference refUser = database.getReference(PATH_USERS + user.getUid());
        DatabaseReference nuevo = myRef.child(user.getUid());

        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> encuentros = new ArrayList<>();
                Usuario u = snapshot.getValue(Usuario.class);
                //Luego guardar el encuentro en el usuario
                DatabaseReference referenceUser = database.getReference(PATH_USERS + user.getUid());
                if(u.getEncuentros() != null && !u.getEncuentros().isEmpty()){
                    encuentros = u.getEncuentros();
                }
                encuentros.add(id);
                referenceUser.child("encuentros").setValue(encuentros);



                //refUser.child("encuentros").setValue(encuentros);
                Ubicacion ub = u.getUbicacion();
                //nuevo.setValue(new Participante(ub.getLatitude(),ub.getLongitude(),u.getId()));
                DatabaseReference refEnc = database.getReference(PATH_MEETINGS + id);
                refEnc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Encuentro e = snapshot.getValue(Encuentro.class);
                        ArrayList<Participante> participantes = e.getParticipantes();
                        participantes.add(new Participante(ub.getLatitude(),ub.getLongitude(),u.getId()));
                        refEnc.child("participantes").setValue(participantes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
            mAuth.signOut();
            Intent intent = new Intent(EncuentroActivity.this, LoginActivity.class);
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