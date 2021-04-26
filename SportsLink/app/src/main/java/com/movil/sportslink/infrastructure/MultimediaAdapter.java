package com.movil.sportslink.infrastructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Multimedia;

import java.util.ArrayList;

public class MultimediaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Multimedia> paisajes;

    public MultimediaAdapter(Context context, ArrayList<Multimedia> paisajes) {
        this.context = context;
        this.paisajes = paisajes;
    }

    @Override
    public int getCount() {
        return paisajes.size();
    }

    @Override
    public Object getItem(int position) {
        return paisajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_multimedia ,null);
        }

        TextView location  = (TextView) convertView.findViewById(R.id.expLocationGallery);
        location.setText( paisajes.get(position).getLocation() );

        return convertView;
    }
}