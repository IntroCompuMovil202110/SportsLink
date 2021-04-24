package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

public class CrearEncuentro1Activity extends AppCompatActivity {
    private Spinner actividadSpinner;
    private EditText nombreEditText;
    private EditText descripcionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro1);

        actividadSpinner = findViewById(R.id.crearEncuentro1actividadSpinner);
        nombreEditText = findViewById(R.id.crearEncuentro1nombreEditText);
        descripcionEditText = findViewById(R.id.crearEncuentro1descripcionEditText);

        findViewById(R.id.crearEncuentro1cancelarButton).setOnClickListener(v -> finish());
        findViewById(R.id.crearEncuentro1siguienteButton).setOnClickListener(v -> {
            // TO DO
            // Enviar a la siguiente actividad la informacion necesaria para continuar con la
            // creacion del evento (tipo actividad, nombre, descripcion).
            Intent intent = new Intent(getBaseContext(), CrearEncuentro2Activity.class);
            startActivity(intent);
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.actividadesArray));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actividadSpinner.setAdapter(arrayAdapter);
    }
}