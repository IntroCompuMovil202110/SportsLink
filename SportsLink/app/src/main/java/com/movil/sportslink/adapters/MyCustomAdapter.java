package com.movil.sportslink.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import com.movil.sportslink.controlador.ChatActivity;
import com.movil.sportslink.controlador.Perfil_Vista;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Usuario> list;
    private Context context;


    public MyCustomAdapter(ArrayList<Usuario> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_contacto, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.nombreUsuario);
        listItemText.setText(list.get(position).getName());

        //Handle buttons and add onClickListeners

        Button addBtn = (Button) view.findViewById(R.id.verUsuario);
        ImageButton chatBtn = (ImageButton) view.findViewById(R.id.chatUsuario);
        ImageButton perfilBtn = (ImageButton) view.findViewById(R.id.perfilUsuario);

        /*addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
            }
        });*/

        chatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putStringArrayListExtra(ChatActivity.USERS_EXTRA_BUNDLE, new ArrayList<>(Arrays.
                    asList(new String[]{list.get(position).getId(), FirebaseAuth.getInstance().getCurrentUser().getUid()})));
            context.startActivity(intent);
        });

        perfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //String nombre = list.get(position).getNombre();
                String num = list.get(position).getNumeroCelular();
                //bundle.putString("NOMBRE", list.get(position).getNombre());
                bundle.putString("NUMERO", list.get(position).getNumeroCelular());

                Intent intent = new Intent(parent.getContext(), Perfil_Vista.class);
                // intent.putExtra("NOMBRE",nombre);
                //intent.putExtra("NUMERO",num);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}




