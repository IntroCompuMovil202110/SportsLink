package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Encuentro;

public class Detalle_EncuentroActivity extends AppCompatActivity {
    //Encuentro encuentroSeleccionado;
    TextView nombre;
    TextView fecha;
    TextView actividad;
    Button recorri;
    int latitud;
    int longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__encuentro);
        nombre =  findViewById(R.id.NombreEncuentrotextView);
        fecha = findViewById(R.id.fechaEncuentrotextView);
        actividad = findViewById(R.id.Actividad_encuentrotextView);
        recorri = findViewById(R.id.Recorridobutton);
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

        recorri.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), RoutasActivity.class);
            Bundle bundlen = new Bundle();
            bundlen.putDouble("LATINICIO",4.769347740268768);
            bundlen.putDouble("LNGINICIO", -74.15699849763922);
            bundlen.putDouble("LATFINAL",4.870754183361389);
            bundlen.putDouble("LNGFINAL",-74.14501749482967);
            intent.putExtras(bundlen);
            startActivity(intent);
        });
    }
}