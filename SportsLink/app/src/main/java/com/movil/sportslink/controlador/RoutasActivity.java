package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.movil.sportslink.R;
import com.movil.sportslink.infrastructure.PermissionsRequestor;
import com.movil.sportslink.infrastructure.RoutingMachine;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.here.sdk.mapviewlite.MapViewLite;

import java.io.IOException;
import java.util.List;

public class RoutasActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final double RADIUS_OF_EARTH_KM = 6378;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation;

    private static final String TAG = RoutasActivity.class.getSimpleName();

    private PermissionsRequestor permissionsRequestor;
    private MapViewLite mapView;
    private RoutingMachine routingMachine;

    private GeoCoordinates pInicio;
    private GeoCoordinates pfinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routas);
        Intent intent = getIntent();
        // Get a MapView instance from layout.
        mapView = findViewById(R.id.map_view);
        System.out.println(mapView);
        mapView.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            permisos();
        }else{
            mLocationRequest = createLocationRequest();
        }

        Bundle bundle = getIntent().getExtras();

        pInicio = new GeoCoordinates(bundle.getDouble("LATINICIO"),bundle.getDouble("LNGINICIO"));
        pfinal = new GeoCoordinates(bundle.getDouble("LATFINAL"),bundle.getDouble("LNGFINAL"));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        handleAndroidPermissions();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override public void onSuccess(Location location) {Log.i("LOCATION", "onSuccess location");
                if (location != null) {
                    Log.i(" LOCATION ", "Longitud: " + location.getLongitude());
                    Log.i(" LOCATION ", "Latitud: " + location.getLatitude());

                    pInicio = new GeoCoordinates(location.getLatitude(),location.getLongitude());
                    if(pInicio != null && pfinal != null && routingMachine != null){
                        routingMachine.clearMap();
                        cambiarRuta(pInicio,pfinal);
                    }
                }
            }
        });



        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    //latitude.setText("Latitude:  " + String.valueOf(location.getLatitude()));
                    //longitude.setText("Longitude:  " + String.valueOf(location.getLongitude()));
                    //altitude.setText("Altitude:   " + String.valueOf(location.getAltitude()));
                    userLocation = location;
                    pInicio = new GeoCoordinates(location.getLatitude(),location.getLongitude());

                    if(pInicio != null && pfinal != null && routingMachine != null){
                        routingMachine.clearMap();
                        cambiarRuta(pInicio,pfinal);
                    }
                    //Toast.makeText(mapas.this, "La dirección es"+location.getLatitude(), Toast.LENGTH_SHORT).show();
                }
            }
        };



        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes
                            .RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(RoutasActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {

                        }
                        break;
                    case LocationSettingsStatusCodes
                            .SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }



    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Log.d(TAG, "Location update started ..............: ");
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest= new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void permisos(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                // Show anexpanationto theuser*asynchronously*
            }
            // requestthe
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CHECK_SETTINGS);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS es una// constante definida en la aplicación, se debe usar// en el callbackpara identificar el permiso }
        }
    }

    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionsRequestor(this);
        permissionsRequestor.request(new PermissionsRequestor.ResultListener(){

            @Override
            public void permissionsGranted() {
                loadMapScene();
            }

            @Override
            public void permissionsDenied() {
                Log.e(TAG, "Permissions denied by user.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {
                    routingMachine = new RoutingMachine(RoutasActivity.this, mapView);
                    if(pInicio != null && pfinal != null && routingMachine != null){

                        cambiarRuta(pInicio,pfinal);
                    }
                } else {
                    Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

    public void addRouteButtonClicked(View view) {

        routingMachine.addRoute(pInicio,
                pfinal);
    }

    public void volverButtonClicked(View view){
        finish();
    }

    public void cambiarRuta(GeoCoordinates i, GeoCoordinates f){
        routingMachine.addRoute(i,f);
    }

    public void addWaypointsButtonClicked(View view) {
        routingMachine.addWaypoints();
    }

    public void clearMapButtonClicked(View view) {
        routingMachine.clearMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        startLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }



}