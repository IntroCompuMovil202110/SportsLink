package com.movil.sportslink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Mensaje;

import java.util.ArrayList;
import java.util.TreeMap;

public class MensajeAdapter extends BaseAdapter {
    private ArrayList<Mensaje> messages = new ArrayList<>();
    private TreeMap<String, String> uidToNameMapping;
    private Context context;

    public MensajeAdapter(Context context, ArrayList<Mensaje> messages, TreeMap<String, String> uidToNameMapping) {
        this.context = context;
        this.uidToNameMapping = uidToNameMapping;
        for (Mensaje m : messages) {
            this.messages.add(new Mensaje(m.getIdChat(), m.getAutor(), m.getTexto()));
        }
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mensaje_item, parent, false);
        }
        TextView autorMensaje = convertView.findViewById(R.id.autorMensaje);
        TextView contenidoMensaje = convertView.findViewById(R.id.contenidoMensaje);

        Mensaje message = messages.get(position);
        autorMensaje.setText(uidToNameMapping.get(message.getAutor()));
        contenidoMensaje.setText(message.getTexto());
        return convertView;
    }
}
