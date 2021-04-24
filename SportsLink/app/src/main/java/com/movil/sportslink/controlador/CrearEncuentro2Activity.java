package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

import java.util.ArrayList;
import java.util.List;

public class CrearEncuentro2Activity extends AppCompatActivity {
    private TextView fechaTextView;
    private ImageButton fechaImageButton;
    private TimePicker horaTimePicker;
    private Spinner capacidadSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro2);

        fechaTextView = findViewById(R.id.crearEncuentro2fechaTextView);
        fechaImageButton = findViewById(R.id.crearEncuentro2fechaImageButton);
        horaTimePicker = findViewById(R.id.crearEncuentro2horaTimePicker);
        capacidadSpinner = findViewById(R.id.crearEncuentro2capacidadSpinner);

        findViewById(R.id.crearEncuentro2atrasButton).setOnClickListener(v -> finish());
        findViewById(R.id.crearEncuentro2siguienteButton).setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), CrearEncuentro3Activity.class);
            // TO DO
            // Enviar a la siguiente actividad la informacion necesaria para continuar con la
            // creacion del evento (fecha, hora, capacidad).
            startActivity(intent);
        });
        findViewById(R.id.crearEncuentro2cancelarButton).setOnClickListener(v -> {
            // TO DO
            // Mirar como hacer esta parte. En teoria vuelve a la pantalla principal,
            // entonces toca indicar a la actividad anterior que tambien debe terminar,
            // tal vez usando startActivityForResult desde la actividad anterior?
        });

        fechaImageButton.setOnClickListener(v -> {
            // TO DO
            // Agregar la logica para obtener la fecha usando la clase DatePicker?.
        });

        int capacidadMaxima = getResources().getInteger(R.integer.capacidadMaxNum);
        List<Integer> capacidades = new ArrayList<>();
        for (int i = 1; i <= capacidadMaxima; ++i) capacidades.add(i);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, capacidades);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        capacidadSpinner.setAdapter(arrayAdapter);
    }
}