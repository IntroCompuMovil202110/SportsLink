package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.adapters.MensajeAdapter;
import com.movil.sportslink.modelo.Conversacion;
import com.movil.sportslink.modelo.Mensaje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;

public class ChatActivity extends AppCompatActivity {

    private final static String PATH_CONVERSACIONES = "conversaciones";
    private final static String PATH_MENSAJES = "mensajes";
    private final static String PATH_USERS = "users";
    public final static String USERS_EXTRA_BUNDLE = "com.movil.sportslink.usuarios";
    private final TreeMap<String, String> uidToNameMapping = new TreeMap<>();
    private String sourceUserUid;
    private String chatId;
    private ArrayList<String> uidUsers;
    private ArrayList<Mensaje> lastKnownMessages = new ArrayList<>();
    private TextView chatHeader;
    private ListView chatListView;

    private final ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            ArrayList<Mensaje> messages = new ArrayList<>();
            for (DataSnapshot mensaje : snapshot.getChildren()) {
                String autor = Objects.requireNonNull(mensaje.child("autor").getValue()).toString();
                String texto = Objects.requireNonNull(mensaje.child("texto").getValue()).toString();
                Log.i("LOL", "Lee un mensaje con autor: " + autor + ", texto: " + texto);
                messages.add(new Mensaje(chatId, autor, texto));
            }
            chatListView.setAdapter(new MensajeAdapter(ChatActivity.this, messages, uidToNameMapping));
            if (!messages.isEmpty()) {
                chatListView.setSelection(messages.size() - 1);
            }
            lastKnownMessages = messages;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private EditText chatEditText;

    private String getChatId() {
        Collections.sort(uidUsers);
        StringBuilder chatId = new StringBuilder();
        for (String uid : uidUsers) {
            chatId.append(uid);
        }
        return chatId.toString();
    }

    private void createChatDirAndStartChatUpdates(ArrayList<String> usersUid) {
        Collections.sort(usersUid);
        FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatId = getChatId();
                if (!snapshot.child(chatId).exists()) {
                    Log.i("LOL", "Aun no existe conversacion con id " + chatId);
                    Conversacion chat = new Conversacion(chatId, new ArrayList<>(), usersUid);
                    FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).child(chatId).setValue(chat).addOnSuccessListener(unused ->
                            FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).child(chatId).child(PATH_MENSAJES).addValueEventListener(eventListener));
                } else {
                    Log.i("LOL", "Ya existe conversacion con id " + chatId);
                    Log.i("LOL", snapshot.child(chatId).toString());
                    FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).child(chatId).child(PATH_MENSAJES).addValueEventListener(eventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

        chatHeader = findViewById(R.id.chatHeader);
        chatListView = findViewById(R.id.chatListView);
        chatEditText = findViewById(R.id.chatEditText);

        sourceUserUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        uidUsers = getIntent().getStringArrayListExtra("com.movil.sportslink.usuarios");
        chatId = getChatId();

        updateChatHeaderInfo();
        createUidToNameMapping();
        chatEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = chatEditText.getText().toString().trim();
                if (!text.isEmpty()) { // send message.
                    Mensaje sendMessage = new Mensaje(chatId, sourceUserUid, text);
                    lastKnownMessages.add(sendMessage);
                    FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).child(chatId)
                            .child(PATH_MENSAJES).setValue(lastKnownMessages);
                }
                chatEditText.setText("");
                return true;
            }
            return false;
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
            //mAuth.signOut();
            Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if(itemClicked == R.id.crearEncuentroButton){
            Intent intent = new Intent(this, CrearEncuentro1Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createChatDirAndStartChatUpdates(uidUsers);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference(PATH_CONVERSACIONES).child(chatId)
                .child(PATH_MENSAJES).removeEventListener(eventListener);
    }

    private void createUidToNameMapping() {
        for (String uid : uidUsers) {
            FirebaseDatabase.getInstance().getReference(PATH_USERS).child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    uidToNameMapping.put(uid, snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void updateChatHeaderInfo() {
        for (String uid : uidUsers) {
            FirebaseDatabase.getInstance().getReference(PATH_USERS).child(uid).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists() || !snapshot.child("name").exists()) return;
                            String currentText = chatHeader.getText().toString();
                            String commaAndSpace = currentText.isEmpty() ? "" : ", ";
                            String newText = currentText + commaAndSpace + Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                            chatHeader.setText(newText);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}