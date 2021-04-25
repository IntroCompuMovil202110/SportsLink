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
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.movil.sportslink.R;

public class seleccionar_LugarActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int SETTING_GPS  =15;
    private double latitud;
    private double longitud;
    private static final int LOCATION_PERMISSION_ID =10;
    private static final String LOCATION_NAME = Manifest.permission.ACCESS_FINE_LOCATION;
    private Marker point;


    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar__lugar);

        locationRequest = createLocationRequest();
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback =new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location =locationResult.getLastLocation();
                if(location != null){
                    if(point != null){
                        point.remove();
                    }
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    LatLng loc =new LatLng(latitud,longitud);
                     point = mMap.addMarker(new MarkerOptions().position(loc).title("Usted está Aqui"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                }
            }
        };

        requestPermission(this, LOCATION_NAME, "NEEDED", LOCATION_PERMISSION_ID);
        initView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView(){

        if(ContextCompat.checkSelfPermission(this, LOCATION_NAME)== PackageManager.PERMISSION_GRANTED) {
            checkSettingLocation();
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
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                startLocationUpdates();
//            }
//        });
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitud, longitud);
        point  = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}