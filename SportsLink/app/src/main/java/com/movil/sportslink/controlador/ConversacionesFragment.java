package com.movil.sportslink.controlador;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class ConversacionesFragment extends Fragment {

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
                listView.setAdapter(new MyCustomAdapter(usuarios, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        usuarioList = new ArrayList<Usuario>();
        PersistidorUsuarios pu = new PersistidorUsuarios();
        //System.out.println(pu.getUsuarios());
        if (usuarioList == null) {
            System.out.println("NULL O VACIO");
        }
        //MyCustomAdapter adapter = new MyCustomAdapter(usuarioList, getContext());

        //handle listview and assign adapter
        listView = view.findViewById(R.id.listaConv);
        chatsGrupales = view.findViewById(R.id.chatsGrupalesButton);
        obtenerUsuariosActualizaAdapter();
        chatsGrupales.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatsGrupalesActivity.class);
            startActivity(intent);
        });
        //lView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
