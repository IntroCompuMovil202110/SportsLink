package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.GestureState;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.routing.RoutingEngine;
import com.movil.sportslink.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;


import com.movil.sportslink.infrastructure.MapMarkerExec;
import com.movil.sportslink.infrastructure.PermissionsRequestor;
import com.movil.sportslink.modelo.LocationPermissionsRequestor;
import com.movil.sportslink.modelo.PlatformPositioningProvider;

import java.util.ArrayList;
import java.util.List;

public class CrearRutaEncuentro extends AppCompatActivity {

    private String locationPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_ID=1;
    private boolean locationEnable=false;

    private android.location.Location myLocation;
    private PlatformPositioningProvider platformPositioningProvider = null;
    private MapImage myMapImage;
    private MapMarker myMarker=null;
    //private Button centerButton;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PATH_MEETINGS = "encuentros/";
    private Context context;
    private PermissionsRequestor permissionsRequestor;
    private MapView mapView;
    private MapMarkerExec mapMarkerExample;
    private List<MapMarker> waypointMarkers;
    private RoutingEngine routingEngine;
    private MapPolyline mapPolyline;
    private Boolean inicio = true;
    private String id;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ruta_encuentro);
        id = getIntent().getStringExtra("ID");
        // Get a MapView instance from layout
        mapView = findViewById(R.id.map_view_ruta);
        mapView.onCreate(savedInstanceState);
        waypointMarkers = new ArrayList<>();
        context = getApplicationContext();



        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        handleAndroidPermissions();

        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            e.printStackTrace();
        }

        setLongPressGestureHandler();


        //Map
        initMyLocation();
    }

    private void initMyLocation() {
        if(locationEnable)
            return;
        if(ContextCompat.checkSelfPermission(this, locationPerm)== PackageManager.PERMISSION_GRANTED) {
            locationEnable=true;
            myMapImage= MapImageFactory.fromResource(this.getResources(),R.drawable.poi);
            platformPositioningProvider = new PlatformPositioningProvider(CrearRutaEncuentro.this);
            //centerButton.setVisibility(View.VISIBLE);
            starLocating();
        }
        loadMapScene();
    }

    private void starLocating() {
        if(platformPositioningProvider==null){
            //TODO: Handle error
            return;
        }
        platformPositioningProvider.startLocating(new PlatformPositioningProvider.PlatformLocationListener() {
            @Override
            public void onLocationUpdated(android.location.Location location) {
                myLocation=location;

                if(myMarker==null){
                    Anchor2D anchor2D=new Anchor2D(0.5f,1.0f);
                    myMarker=new MapMarker(new GeoCoordinates(location.getLatitude(),location.getLongitude()),myMapImage,anchor2D);
                    mapView.getMapScene().addMapMarker(myMarker);
                    mapView.getCamera().lookAt(new GeoCoordinates(location.getLatitude(),location.getLongitude()),2500);
                }else{
                    myMarker.setCoordinates(new GeoCoordinates(location.getLatitude(),location.getLongitude()));
                }
            }
        });
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
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    mapMarkerExample = new MapMarkerExec(CrearRutaEncuentro.this, mapView);
                } else {
                    Log.d(TAG, "onLoadScene failed: " + mapError.toString());
                }
            }
        });
    }

    private void setLongPressGestureHandler(){
        mapView.getGestures().setLongPressListener((((gestureState, touchPoint) -> {
            if(gestureState == GestureState.BEGIN){
                MapImage waypointImage = MapImageFactory.fromResource(context.getResources(),R.drawable.poi3);
                MapMarker waypointMarker = new MapMarker(mapView.viewToGeoCoordinates(touchPoint),waypointImage);
                mapView.getMapScene().addMapMarker(waypointMarker);
                if(waypointMarkers.size() != 2){

                        waypointMarkers.add(waypointMarker);

                }else{
                    if(inicio){
                        mapView.getMapScene().removeMapMarker(waypointMarkers.get(0));
                        waypointMarkers.set(0,waypointMarker);
                        inicio = false;
                    }else{
                        mapView.getMapScene().removeMapMarker(waypointMarkers.get(1));
                        waypointMarkers.set(1,waypointMarker);
                        inicio = true;
                    }



                }

            }
        })));
    }

    public void anchoredMapMarkersButtonClicked(View view) {
        mapMarkerExample.showAnchoredMapMarkers();
    }

    public void centeredMapMarkersButtonClicked(View view) {
        mapMarkerExample.showCenteredMapMarkers();
    }

    public void clearMapButtonClicked(View view) {
        if(waypointMarkers != null && waypointMarkers.size() > 0 && id != null){
            myRef = database.getReference(PATH_MEETINGS + id);
            MapMarker act = waypointMarkers.get(0);
            myRef.child("latPuntoEncuentro").setValue(act.getCoordinates().latitude);
            myRef.child("lngPuntoEncuentro").setValue(act.getCoordinates().longitude);
            if(waypointMarkers.size() == 2){
                act = waypointMarkers.get(1);
                myRef.child("latPuntoFinal").setValue(act.getCoordinates().latitude);
                myRef.child("lngPuntoFinal").setValue(act.getCoordinates().longitude);
            }
        }

        Intent intent = new Intent(getBaseContext(), Detalle_EncuentroActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}