package com.movil.sportslink.controlador;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Encuentro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearEncuentro2Activity extends AppCompatActivity {

    private TimePicker horaTimePicker;
    private Spinner capacidadSpinner;
    private EditText etPlannedDate;
    private int mes, anio, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro2);


        horaTimePicker = findViewById(R.id.crearEncuentro2horaTimePicker);
        capacidadSpinner = findViewById(R.id.crearEncuentro2capacidadSpinner);
        etPlannedDate = findViewById(R.id.etPlannedDate);
        Bundle bundle2  = getIntent().getBundleExtra("Bundle");

        findViewById(R.id.crearEncuentro2atrasButton).setOnClickListener(v -> finish());
        findViewById(R.id.etPlannedDate).setOnClickListener(v -> {showDatePickerDialog();});
        findViewById(R.id.crearEncuentro2siguienteButton).setOnClickListener(v -> {

            bundle2.putInt("anio", anio);
            bundle2.putInt("mes", mes);
            bundle2.putInt("dia", dia);
            bundle2.putInt("hora", horaTimePicker.getHour());
            bundle2.putInt("minuto", horaTimePicker.getMinute());
            bundle2.putInt("capacidad", (Integer)capacidadSpinner.getSelectedItem());


            Intent intent = new Intent(getBaseContext(), seleccionar_LugarActivity.class);
            intent.putExtra("Bundle2", bundle2);

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



        int capacidadMaxima = getResources().getInteger(R.integer.capacidadMaxNum);
        List<Integer> capacidades = new ArrayList<>();
        for (int i = 1; i <= capacidadMaxima; ++i) capacidades.add(i);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, capacidades);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        capacidadSpinner.setAdapter(arrayAdapter);
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                anio = year;
                mes = month+1;
                dia  = day;
                etPlannedDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;

        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            Log.i("fecha", year + " " + month + " "+ day);
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

    }
}