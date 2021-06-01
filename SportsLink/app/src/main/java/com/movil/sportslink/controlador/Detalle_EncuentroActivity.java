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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Detalle_EncuentroActivity extends AppCompatActivity {
    //Encuentro encuentroSeleccionado;
    TextView nombre;
    TextView fecha;
    TextView actividad;
    Button recorri;
    int latitud;
    int longitud;

    Button unirse;
    Button ruta;

    private String id;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseStorage storage;
    StorageReference storageReference;
    Encuentro encuentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__encuentro);

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

        id = getIntent().getStringExtra("ID");

        ImageView imageView = findViewById(R.id.imagenEnc);
        nombre =  findViewById(R.id.NombreEncuentrotextView);
        fecha = findViewById(R.id.fechaEncuentrotextView);
        actividad = findViewById(R.id.Actividad_encuentrotextView);
        recorri = findViewById(R.id.Recorridobutton);

        unirse = findViewById(R.id.unirse);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        //encuentroSeleccionado = (Encuentro) getIntent().getParcelableExtra("Bundle");
        //nombre.setText(encuentroSeleccionado.getNombre().toString());
        //fecha.setText(encuentroSeleccionado.getFecha().toString());
       // actividad.setText(encuentroSeleccionado.getActividad().toString());

        recorri.setOnClickListener(v -> {
            if(encuentro != null){
                Intent intent = new Intent(getBaseContext(), RoutasActivity.class);
                Bundle bundlen = new Bundle();
                bundlen.putDouble("LATINICIO",encuentro.getLatPuntoEncuentro());
                bundlen.putDouble("LNGINICIO", encuentro.getLngPuntoFinal());
                bundlen.putDouble("LATFINAL",encuentro.getLatPuntoFinal());
                bundlen.putDouble("LNGFINAL",encuentro.getLngPuntoFinal());
                intent.putExtras(bundlen);
                startActivity(intent);
            }

        });

        try {
            downloadImage(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        obtenerDatos();
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

    public void obtenerDatos(){
        myRef = database.getReference("encuentros/" + id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                encuentro = snapshot.getValue(Encuentro.class);
                nombre.setText(encuentro.getNombre());
                actividad.setText(encuentro.getNombre());
                fecha.setText(encuentro.getFecha());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void agregarUsuarioParticipante(View view){
        FirebaseUser user = mAuth.getCurrentUser();

        myRef = database.getReference("encuentros/" + id + "participantes/");
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
                DatabaseReference refEnc = database.getReference("encuentros/" + id);
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

    private void downloadImage(ImageView imageView) throws IOException {

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();

        StorageReference islandRef = storageReference.child("images/" +id);

        File localFile = File.createTempFile("images", "jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                imageView.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

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
            Intent intent = new Intent(Detalle_EncuentroActivity.this, LoginActivity.class);
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