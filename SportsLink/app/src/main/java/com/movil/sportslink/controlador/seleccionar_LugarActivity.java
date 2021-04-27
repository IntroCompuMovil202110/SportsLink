package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.util.BuddhistCalendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.movil.sportslink.R;
import com.movil.sportslink.infrastructure.PersistidorEncuentro;
import com.movil.sportslink.modelo.Actividad;
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.LugarEncuentro;
import com.movil.sportslink.modelo.Recorrido;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class seleccionar_LugarActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int SETTING_GPS  =15;
    private double latitud;
    Geocoder geocoder;
    EditText address;
    private double longitud;
    private LatLng inicioL;
    private LatLng finalL;
    private static final int LOCATION_PERMISSION_ID =10;
    private static final String LOCATION_NAME = Manifest.permission.ACCESS_FINE_LOCATION;
    private Marker point;
    private Marker search;
    private Boolean ciclismo = false, continuar = false, home = true, sear = true;
    private int anio, mes, dia, capacidad, actividad, hora, minuto;
    private String nombre;
    private Marker init, finit;



    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Encuentro encuentro = new Encuentro();
        setContentView(R.layout.activity_seleccionar__lugar);
        Bundle bundle3 = getIntent().getBundleExtra("Bundle2");

        if(bundle3.getInt("posicion") == 0){
            Log.i("hola", "si es igual");
            ciclismo = true;
        }
        locationRequest = createLocationRequest();

        findViewById(R.id.continuar).setOnClickListener(v -> {

            if(ciclismo && continuar){
                setsEncuentro(bundle3, encuentro);
                Recorrido recorrido = new Recorrido(init.getPosition(), finit.getPosition());
                encuentro.setRecorrido(recorrido);
                PersistidorEncuentro.añadirEncuentro(encuentro);
                Intent intent = new Intent(getBaseContext(), RoutasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("LATINICIO",inicioL.latitude);
                bundle.putDouble("LNGINICIO",inicioL.longitude);
                bundle.putDouble("LATFINAL",finit.getPosition().latitude);
                bundle.putDouble("LNGFINAL",finit.getPosition().longitude);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            if(ciclismo && !continuar){
                Toast.makeText(this,"Seleccione el punto final", Toast.LENGTH_LONG).show();
                continuar = true;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(init.getPosition()).title("Punto de inicio") ) ;
            }
            if(!ciclismo){
                //adsadadas
                setsEncuentro(bundle3, encuentro);
                PersistidorEncuentro.añadirEncuentro(encuentro);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        address = findViewById(R.id.edtTxtViewMap);
        geocoder = new Geocoder(this);


        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback =new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location =locationResult.getLastLocation();
                if(location != null && latitud != location.getLatitude() && longitud != location.getLongitude()){


                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    LatLng loc =new LatLng(location.getLatitude(),location.getLongitude());

                    try {
                        inicioL = loc;
                        List<Address> addressesList = geocoder.getFromLocation(latitud, longitud, 2);
                        point = mMap.addMarker(new MarkerOptions().position(loc).title(addressesList.get(0).getAddressLine(0)));
                        init = point;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        requestPermission(this, LOCATION_NAME, "NEEDED", LOCATION_PERMISSION_ID);
        initView();
        address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String addresString = address.getText().toString();
                    if (!addresString.isEmpty()) {
                        try {
                            List<Address> addressesList = geocoder.getFromLocationName(addresString, 2);
                            if (addressesList != null && !addressesList.isEmpty()) {
                                Address result = addressesList.get(0);
                                if (mMap != null) {

                                    LatLng location = new LatLng(result.getLatitude(), result.getLongitude());
                                    latitud = result.getLatitude();
                                    longitud = result.getLongitude();

                                    if(!continuar)
                                    {
                                        Log.i("sear" ,"!continuar");
                                        if(point != null)
                                        {
                                            Log.i("sear2", "point");
                                            point.remove();
                                        }
                                        if(search != null)
                                        {
                                            Log.i("sear3", "search");
                                            search.remove();
                                        }

                                    }else {
                                        if(point != null)
                                        {
                                            Log.i("sear2", "point");
                                            point.remove();
                                        }
                                        if(search != null)
                                        {
                                            Log.i("sear3", "search");
                                            search.remove();
                                        }

                                    }

                                    search = mMap.addMarker(new MarkerOptions().position(location).title(result.getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    if(!continuar)
                                    {
                                        init = search;
                                    }else
                                    {
                                        finit = search;
                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                                    stopLocationUpdates();
                                } else {
                                    Toast.makeText(v.getContext(), "address not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView(){

        if(ContextCompat.checkSelfPermission(this, LOCATION_NAME)== PackageManager.PERMISSION_GRANTED) {
            checkSettingLocation();
        }
    }
    private void setsEncuentro(Bundle bundle, Encuentro encuentro){

        anio = bundle.getInt("anio");
        mes = bundle.getInt("mes");
        dia = bundle.getInt("dia");
        hora = bundle.getInt("hora");
        minuto = bundle.getInt("minuto");

        capacidad = bundle.getInt("capacidad");
        nombre = bundle.getString("nombre");
        actividad = bundle.getInt("posicion");

        encuentro.setFecha(LocalDateTime.of(anio,mes,dia,hora,minuto));
        encuentro.setCapacidad(capacidad);
        encuentro.setNombre(nombre);
        encuentro.setLugarEncuentro(new LugarEncuentro(init.getPosition()));

        switch (actividad){
            case 0:
                encuentro.setActividad(Actividad.CICLISMO);
                break;
            case 1:
                encuentro.setActividad(Actividad.CICLISMO_MONTAÑA);
                break;
            case 2:
                encuentro.setActividad(Actividad.MONTAÑISMO);
                break;
            case 3:
                encuentro.setActividad(Actividad.SENDERISMO);
                break;
        }
    }
    private void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(this, LOCATION_NAME)== PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest,locationCallback, null);
        }
    }
    private void stopLocationUpdates(){
        locationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void checkSettingLocation(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode= ((ApiException)e).getStatusCode();
                switch (statusCode){
                    case CommonStatusCodes
                            .RESOLUTION_REQUIRED:
                        try{
                            ResolvableApiException resolvable = (ResolvableApiException)e;
                            resolvable.startResolutionForResult(seleccionar_LugarActivity.this,SETTING_GPS);
                        }catch (IntentSender.SendIntentException sendex){

                        }break;
                    case LocationSettingsStatusCodes
                            .SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
    private LocationRequest createLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if(ContextCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, id);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_ID){
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTING_GPS){
            startLocationUpdates();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if(!continuar)
                {
                    if (point != null)
                    {
                        point.remove();
                    }
                    if(search != null)
                    {
                        search.remove();
                    }
                }else {
                    if (point != null)
                    {
                        point.remove();
                    }
                    if(search != null)
                    {
                        search.remove();
                    }
                }

                initView();

                latitud = latLng.latitude;
                longitud = latLng.longitude;
                point = mMap.addMarker(new MarkerOptions().position(latLng).title(geoCoderSearchLatLng(latLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                if(!continuar)
                {
                    init = point;
                }else
                {
                    finit = point;
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                stopLocationUpdates();
            }
        });

        LatLng sydney = new LatLng(latitud, longitud);
        point  = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private String geoCoderSearchLatLng(LatLng position) {
        String ret = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude,2);
            if(addresses != null && !addresses.isEmpty())
            {
                Address addressResult = addresses.get(0);
                ret = addressResult.getAddressLine(0);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public void finalizarEncuentro(View view) {

        startActivity(new Intent(view.getContext(), MainActivity.class));
    }
}