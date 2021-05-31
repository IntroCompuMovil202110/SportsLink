package com.movil.sportslink.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;
import java.util.TreeSet;

public class UsuarioCrearChatGrupalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Usuario> usuarios;
    private TreeSet<String> checkedUsers = new TreeSet<>();

    public UsuarioCrearChatGrupalAdapter(Context context, ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<String> getCheckedUsers() {
        ArrayList<String> uids = new ArrayList<>();
        uids.addAll(checkedUsers);
        return uids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.usuario_chat_grupal_item, parent, false);
        }
        TextView nombreUsuario = convertView.findViewById(R.id.usuarioChatGrupalTextView);
        SwitchCompat switchCompat = convertView.findViewById(R.id.usuarioChatGrupalSwitch);

        nombreUsuario.setText(usuarios.get(position).getName());
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String uid = usuarios.get(position).getId();
            Log.i("LOL", "Cambia el estado de " + uid);
            if (isChecked) {
                checkedUsers.add(uid);
            } else {
                checkedUsers.remove(uid);
            }
        });
        return convertView;
    }
}
