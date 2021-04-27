package com.movil.sportslink.controlador;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.movil.sportslink.R;
import com.movil.sportslink.adapters.ContactsAdapter;
import com.movil.sportslink.adapters.MyCustomAdapter;
import com.movil.sportslink.adapters.UsersConvAdapter;
import com.movil.sportslink.infrastructure.PersistidorUsuarios;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ConversacionesFragment extends Fragment {

    String[] mProjection;
    Cursor mCursor;
    ContactsAdapter mContactsAdapter;
    ListView mlista;
    ArrayList<String> listContactos;

    private RecyclerView recyclerView;
    private UsersConvAdapter usersConvAdapter;
    private ArrayList<Usuario> usuarioList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);



        usuarioList = new ArrayList<Usuario>();
        PersistidorUsuarios pu = new PersistidorUsuarios();
        usuarioList = pu.obtenerUsuarios();
        //System.out.println(pu.getUsuarios());
        if(usuarioList == null){
            System.out.println("NULL O VACIO");
        }
        MyCustomAdapter adapter = new MyCustomAdapter(usuarioList, getContext());

        //handle listview and assign adapter
        ListView lView = (ListView) view.findViewById(R.id.listaConv);
        lView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }





}
