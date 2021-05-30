package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

public class Perfil_Vista extends AppCompatActivity {
    TextView nombre, numero;
    ImageView foto;
    Button agregar;
    Usuario usuario;
    TextView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__vista);
        usuario = (Usuario)getIntent().getSerializableExtra("ID");
        nombre = findViewById(R.id.nombreUsuario);
        numero = findViewById(R.id.numero);
        agregar = findViewById(R.id.buttonAñadirContacto);
        descripcion = findViewById(R.id.textViewDescripcion4);
        foto = findViewById(R.id.imageView6);

        nombre.setText(usuario.getName());
        numero.setText(usuario.getNumeroCelular());
        descripcion.setText(usuario.getDescripcion());

    }

}