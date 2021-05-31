package com.movil.sportslink.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.controlador.ChatActivity;
import com.movil.sportslink.modelo.Conversacion;

import java.util.ArrayList;
import java.util.TreeMap;

public class ChatGrupalAdapter extends BaseAdapter {
    TreeMap<String, String> uidToNameMapping;
    private Context context;
    private ArrayList<Conversacion> conversaciones;

    public ChatGrupalAdapter(Context context, ArrayList<Conversacion> conversaciones, TreeMap<String, String> uidToNameMapping) {
        this.context = context;
        this.conversaciones = conversaciones;
        this.uidToNameMapping = uidToNameMapping;
    }

    @Override
    public int getCount() {
        return conversaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return conversaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_grupal_item, parent, false);
        }
        TextView tituloGrupo = convertView.findViewById(R.id.chatGrupalTextView);
        Button chatButton = convertView.findViewById(R.id.chatGrupalButton);

        String titulo = "";
        for (String uid : conversaciones.get(position).getUsuarios()) {
            if (!titulo.isEmpty()) {
                titulo += ", ";
            }
            titulo += uidToNameMapping.get(uid);
        }
        tituloGrupo.setText(titulo);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putStringArrayListExtra(ChatActivity.USERS_EXTRA_BUNDLE, conversaciones.get(position).getUsuarios());
            context.startActivity(intent);
        });
        return convertView;
    }
}
