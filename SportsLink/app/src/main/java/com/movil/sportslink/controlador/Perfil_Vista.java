package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.movil.sportslink.R;

import static android.content.ContentValues.TAG;

public class Perfil_Vista extends AppCompatActivity {
    TextView nombre, numero;
    ImageView foto;
    Button agregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__vista);
        Intent intent = getIntent();
        Bundle bundle= getIntent().getExtras();;
        nombre = findViewById(R.id.nombre);
        numero = findViewById(R.id.numero);
        agregar = findViewById(R.id.buttonAñadirContacto);



        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);

            Log.d(TAG, String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }
        String nomb = bundle.get("NOMBRE").toString();
        String num = bundle.get("NUMERO").toString();
        System.out.println(nomb+num);
        nombre.setText(nomb);
        numero.setText(num);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarContacto(nomb,num);
            }
        });

    }

    private void agregarContacto(String nomb, String num) {
        // Creates a new Intent to insert a contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME,nomb);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,num);

        startActivity(intent);
    }
}