package com.movil.sportslink.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Encuentro;

import java.util.ArrayList;

public class EncuentrosAdapter  extends BaseAdapter implements ListAdapter {

    ArrayList<Encuentro> listaEncuentros = new ArrayList<Encuentro>();
    Context context;

    public EncuentrosAdapter(ArrayList<Encuentro> listaEncuentros, Context context) {
        this.listaEncuentros = listaEncuentros;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaEncuentros.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEncuentros.get(position);
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
            view = inflater.inflate(R.layout.listaencontrados, null);
        }

        TextView nombreEncuentro = view.findViewById(R.id.nombre_EncuentrotextView);
        nombreEncuentro.setText(listaEncuentros.get(position).getNombre());
        TextView fecha = view.findViewById(R.id.FechatextView);
        fecha.setText(listaEncuentros.get(position).getFecha().toString());
        TextView actividad = view.findViewById(R.id.tipo_deportetextView);
        actividad.setText(listaEncuentros.get(position).getActividad().toString());
        //ImageView imagen = view.findViewById(R.id.imageView5);


        return view;
    }
}
