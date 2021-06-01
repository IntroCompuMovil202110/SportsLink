package com.movil.sportslink.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.movil.sportslink.R;
import com.movil.sportslink.controlador.Detalle_EncuentroActivity;
import com.movil.sportslink.controlador.EncuentroActivity;
import com.movil.sportslink.controlador.RoutasActivity;
import com.movil.sportslink.modelo.Encuentro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EncuentrosAdapter  extends BaseAdapter implements ListAdapter {

    ArrayList<Encuentro> listaEncuentros = new ArrayList<>();
    Context context;

    FirebaseStorage storage;
    StorageReference storageReference;

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
        ImageView imageView = view.findViewById(R.id.imageView5);
        fecha.setText(listaEncuentros.get(position).getFecha());
        TextView actividad = view.findViewById(R.id.tipo_deportetextView);
        actividad.setText(listaEncuentros.get(position).getActividad());
        //ImageView imagen = view.findViewById(R.id.imageView5);*/
        System.out.println("Se añadio " + nombreEncuentro.getText());
        Button buver = view.findViewById(R.id.buver);
        Button buruta = view.findViewById(R.id.buruta);

        buver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Detalle_EncuentroActivity.class);
                intent.putExtra("ID",listaEncuentros.get(position).getId());
                parent.getContext().startActivity(intent);
            }
        });

        buruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Encuentro e = listaEncuentros.get(position);
                Bundle bundlen = new Bundle();
                bundlen.putDouble("LATINICIO",e.getLatPuntoEncuentro());
                bundlen.putDouble("LNGINICIO", e.getLngPuntoFinal());
                bundlen.putDouble("LATFINAL",e.getLatPuntoFinal());
                bundlen.putDouble("LNGFINAL",e.getLngPuntoFinal());

                Intent intent = new Intent(parent.getContext(),RoutasActivity.class);
                intent.putExtras(bundlen);
                intent.putExtra("ID",e.getId());
                parent.getContext().startActivity(intent);
            }
        });

        /*try {
            downloadImage(imageView,listaEncuentros.get(position).getId(),view);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return view;
    }

    private void downloadImage(ImageView imageView, String id, View view) throws IOException {

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();

        StorageReference islandRef = storageReference.child("images/" +id);

        File localFile = File.createTempFile("images", "jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                imageView.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Drawable myDrawable = view.getResources().getDrawable(R.drawable.reyes);
                imageView.setImageDrawable(myDrawable);
            }
        });
    }
}
