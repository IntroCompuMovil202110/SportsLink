package com.movil.sportslink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import java.util.List;

public class UsersConvAdapter extends RecyclerView.Adapter<UsersConvAdapter.ViewHolder> {

    private Context mcontext;
    private List<Usuario> usuarios;

    public UsersConvAdapter(Context mcontext, List<Usuario> usuarios) {
        this.mcontext = mcontext;
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_contacto,parent,false);
        return new UsersConvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersConvAdapter.ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        //holder.nombre.setText(usuario.getNombre());

    }

    @Override
    public int getItemCount(){
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreUsuario);
        }
    }



}
