package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Encuentro;

public class Detalle_EncuentroActivity extends AppCompatActivity {
    //Encuentro encuentroSeleccionado;
    TextView nombre;
    TextView fecha;
    TextView actividad;
    int latitud;
    int longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__encuentro);
        nombre =  findViewById(R.id.NombreEncuentrotextView);
        fecha = findViewById(R.id.fechaEncuentrotextView);
        actividad = findViewById(R.id.Actividad_encuentrotextView);
        Bundle bundle = getIntent().getBundleExtra("Bundle");
        nombre.setText(bundle.getString("nombre"));
        fecha.setText(bundle.getString("fecha"));
        actividad.setText(bundle.getString("actividad"));
        latitud = bundle.getInt("latitud");
        longitud = bundle.getInt("longitud");
        //encuentroSeleccionado = (Encuentro) getIntent().getParcelableExtra("Bundle");
        //nombre.setText(encuentroSeleccionado.getNombre().toString());
        //fecha.setText(encuentroSeleccionado.getFecha().toString());
       // actividad.setText(encuentroSeleccionado.getActividad().toString());
    }
}