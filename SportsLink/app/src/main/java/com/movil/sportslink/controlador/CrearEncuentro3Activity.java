package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

public class CrearEncuentro3Activity extends AppCompatActivity {
    private Spinner ciudadSpinner;
    private EditText lugarEncuentroEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro3);
        //Button seleccionarLugarEncuentroButton =findViewById(R.id.seleccionarLugarEncuentroButton);



        findViewById(R.id.seleccionarLugarEncuentroButton).setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), seleccionar_LugarActivity.class));
            // TO DO
            // Logica del boton que selecciona el lugar de encuentro usando el mapa.
        });
        findViewById(R.id.crearEncuentro3crearRecorridoButton).setOnClickListener(v -> {
            // TO DO
            // Logica del boton que crea el encuentro con recorrido.
        });
        findViewById(R.id.crearEncuentro3crearSinRecorridoButton).setOnClickListener(v -> {
            // TO DO
            // Logica del boton que crea el encuentro sin recorrido.
        });
        findViewById(R.id.crearEncuentro3atrasButton).setOnClickListener(v -> finish());
        findViewById(R.id.crearEncuentro3cancelarButton).setOnClickListener(v -> {
            // TO DO
            // Mirar como hacer esta parte. En teoria vuelve a la pantalla principal,
            // entonces toca indicar a la actividad anterior que tambien debe terminar,
            // tal vez usando startActivityForResult desde la actividad anterior?.
        });

        // TO DO
        // Cambiar las ciudades del spinner?. No se cuales poner.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ciudadesArray));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ciudadSpinner.setAdapter(arrayAdapter);
    }
}