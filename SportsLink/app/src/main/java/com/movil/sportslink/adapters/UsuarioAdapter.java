package com.movil.sportslink.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.controlador.EncuentroActivity;
import com.movil.sportslink.controlador.Perfil_Propio;
import com.movil.sportslink.controlador.Perfil_Vista;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;

public class UsuarioAdapter  extends BaseAdapter implements ListAdapter {

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    Context context;

    public UsuarioAdapter(ArrayList<Usuario> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_usuarios, null);
        }

        TextView nombreUsuario = view.findViewById(R.id.nombreUsuario);
        nombreUsuario.setText(listaUsuarios.get(position).getName());

        Button ver = view.findViewById(R.id.verUsuario);


        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Perfil_Vista.class);
                intent.putExtra("ID",listaUsuarios.get(position));
                parent.getContext().startActivity(intent);
            }
        });
        return view;
    }
}