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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.movil.sportslink.R;
import com.movil.sportslink.controlador.EncuentroActivity;
import com.movil.sportslink.controlador.TrackingActivity;
import com.movil.sportslink.modelo.Encuentro;

import java.util.ArrayList;

public class MiEncuentroAdapter extends BaseAdapter implements ListAdapter {

    ArrayList<Encuentro> list = new ArrayList<>();
    Context context;

    public MiEncuentroAdapter(ArrayList<Encuentro> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            view = inflater.inflate(R.layout.miencuentro_layout, null);
        }

        TextView listItemText = view.findViewById(R.id.nombre);
        listItemText.setText(list.get(position).getNombre());

        Button binicio = (Button) view.findViewById(R.id.binicio);
        Button track = view.findViewById(R.id.btrack);
        Button ver = view.findViewById(R.id.bver);

        binicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(parent.getContext(),);
            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TrackingActivity.class);
                intent.putExtra("ID",list.get(position).getId());
                parent.getContext().startActivity(intent);
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), EncuentroActivity.class);
                intent.putExtra("ID",list.get(position).getId());
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
