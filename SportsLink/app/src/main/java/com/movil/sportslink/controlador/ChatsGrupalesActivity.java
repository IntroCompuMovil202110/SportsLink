package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.ChatGrupalAdapter;
import com.movil.sportslink.modelo.Conversacion;

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