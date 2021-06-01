package com.movil.sportslink.controlador;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.GeoCoordinates;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Actividad;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.Participante;
import com.movil.sportslink.modelo.Ubicacion;
import com.movil.sportslink.services.RecommendationService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.movil.sportslink.controlador.SignUpActivity.PATH_USERS;

public class CrearEncuentro1Activity extends AppCompatActivity {
    private static final String PATH_MEETINGS = "encuentros/";
    private Spinner actividadSpinner;
    private EditText nombreEditText;
    private EditText descripcionEditText;
    private Encuentro encuentroActual;
    private TimePicker horaTimePicker;
    private Spinner capacidadSpinner;
    private EditText etPlannedDate;
    private int mes, anio, dia;

    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Ubicacion ubicacionCreador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuentro1);

        actividadSpinner = findViewById(R.id.crearEncuentro1actividadSpinner);
        nombreEditText = findViewById(R.id.crearEncuentro1nombreEditText);
        descripcionEditText = findViewById(R.id.crearEncuentro1descripcionEditText);
        horaTimePicker = findViewById(R.id.crearEncuentro2horaTimePicker);
        etPlannedDate = findViewById(R.id.etPlannedDate);



        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();


        ubicacionUsuario(mAuth.getCurrentUser().getUid());
        findViewById(R.id.etPlannedDate).setOnClickListener(v -> {showDatePickerDialog();});
        //findViewById(R.id.crearEncuentro1cancelarButton).setOnClickListener(v -> finish());
        findViewById(R.id.continuar).setOnClickListener(v -> {
            // TO DO
            // Enviar a la siguiente actividad la informacion necesaria para continuar con la
            // creacion del evento (tipo actividad, nombre, descripcion).


            updateUI(guardarEncuentro());

        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.actividadesArray));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actividadSpinner.setAdapter(arrayAdapter);

        /*int capacidadMaxima = getResources().getInteger(R.integer.capacidadMaxNum);
        List<Integer> capacidades = new ArrayList<>();
        for (int i = 1; i <= capacidadMaxima; ++i) capacidades.add(i);
        ArrayAdapter<Integer> arrayAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, capacidades);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //capacidadSpinner.setAdapter(arrayAdapter2);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        capacidadSpinner.setAdapter(adapter);*/
    }

    private void updateUI(String id) {
        Intent intent = new Intent(getBaseContext(), CrearEncuentroImagenActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    private String guardarEncuentro(){
        encuentroActual = new Encuentro();
        int posicion = actividadSpinner.getSelectedItemPosition();
        switch (posicion){
            case 0:
                encuentroActual.setActividad(Actividad.CICLISMO.toString());
                break;
            case 1:
                encuentroActual.setActividad(Actividad.CICLISMO_MONTAÑA.toString());
                break;
            case 2:
                encuentroActual.setActividad(Actividad.MONTAÑISMO.toString());
                break;
            case 3:
                encuentroActual.setActividad(Actividad.SENDERISMO.toString());
                break;
        }
        ArrayList<Participante> participantes = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        encuentroActual.setNombre(nombreEditText.getText().toString());
        encuentroActual.setAutor(user.getUid());
        //encuentroActual.setCapacidad(20);
        encuentroActual.setFecha(etPlannedDate.getText().toString());
        encuentroActual.setHora(String.valueOf(horaTimePicker.getHour() + " " +horaTimePicker.getMinute()));
        //encuentroActual.setStarted(false);

        myRef = database.getReference(PATH_MEETINGS);
        participantes.add(new Participante(ubicacionCreador.getLatitude(),ubicacionCreador.getLongitude(),user.getUid()));
        encuentroActual.setParticipantes(participantes);
        DatabaseReference postsRefEnc = myRef.push();
        encuentroActual.setId(postsRefEnc.getKey());
        postsRefEnc.setValue(encuentroActual);

        /*if(ubicacionCreador != null){
            DatabaseReference postsRef = postsRefEnc.child("/participantes/" + user.getUid());
            postsRef.setValue();
        }*/

        //Luego guardar el encuentro en el usuario
        DatabaseReference referenceUser = database.getReference(PATH_USERS + user.getUid());
        ArrayList<String> enc = new ArrayList();
        enc.add(postsRefEnc.getKey());
        referenceUser.child("/encuentros").setValue(enc);
        System.out.println(referenceUser.getKey());

        DatabaseReference referenceEstadoEventos = database.getReference("estadoEventos/");
        referenceEstadoEventos.child(encuentroActual.getId()).setValue(false);
        return postsRefEnc.getKey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(CrearEncuentro1Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(itemClicked == R.id.crearEncuentroButton){
            Intent intent = new Intent(this, CrearEncuentro1Activity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.sugg){
            RecommendationService.consumeRESTVolley(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void ubicacionUsuario(String id){
        DatabaseReference reference = database.getReference("/users/" + id + "/ubicacion/");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ubicacionCreador = snapshot.getValue(Ubicacion.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDatePickerDialog() {
        CrearEncuentro2Activity.DatePickerFragment newFragment = CrearEncuentro2Activity.DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
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

        public static CrearEncuentro2Activity.DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            CrearEncuentro2Activity.DatePickerFragment fragment = new CrearEncuentro2Activity.DatePickerFragment();
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