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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCoordinates;

import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Metadata;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.MapViewBase;
import com.here.sdk.mapview.PickMapItemsResult;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Notice;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Section;
import com.here.sdk.routing.Waypoint;
import com.movil.sportslink.R;
import com.movil.sportslink.infrastructure.PermissionsRequestor;
import com.movil.sportslink.infrastructure.RoutingMachine;
import com.movil.sportslink.modelo.LocationPermissionsRequestor;
import com.movil.sportslink.modelo.PlatformPositioningProvider;
import com.movil.sportslink.services.RecommendationService;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoutasActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final double RADIUS_OF_EARTH_KM = 6378;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    private static final String TAG = "A:";
    //Permission
    private String locationPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_ID=1;
    private boolean locationEnable=false;
    private LocationPermissionsRequestor permissionsRequestor;

    //light sensor
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    //Map
    private MapView mapView;
    private MapImage staticLocationImage;
    private Button centerButton;
    MapMarker clickMarker=null;
    MapImage clickImage;
    RoutingEngine routingEngine;
    MapPolyline routeMapPolyline=null;

    //My Location
    private android.location.Location myLocation;
    private PlatformPositioningProvider platformPositioningProvider = null;
    private MapImage myMapImage;
    private MapImage encImage;
    private MapMarker myMarker=null;
    private MapMarker marker = null;
    private MapMarker puntoEncuentro = null;
    private Location userLocation;

    private RoutingMachine routingMachine;

    private GeoCoordinates pInicio;
    private GeoCoordinates pfinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routas);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(this, ActividadesSegunPreferenciasFragment.class);
                startActivity(intent);
                /*fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ActividadesSegunPreferenciasFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();*/
            } else if (itemId == R.id.navigation_search) {
                Intent intent = new Intent(this, EncuentrosUsuarioFragment.class);
                startActivity(intent);
            } else if (itemId == R.id.navigation_chat) {
                Intent intent = new Intent(this, ConversacionesFragment.class);
                startActivity(intent);
            } else if (itemId == R.id.navigation_profile) {

                Intent intent = new Intent(this, Perfil_Propio.class);
                startActivity(intent);
            }
            return true;
        });
        bottomNavigationView.setItemIconTintList(null);

        Intent intent = getIntent();
        // Get a MapView instance from layout.

        System.out.println(mapView);
        mapView = findViewById(R.id.map_view_ruta);
        mapView.onCreate(savedInstanceState);




        Bundle bundle = getIntent().getExtras();

        pInicio = new GeoCoordinates(bundle.getDouble("LATINICIO"),bundle.getDouble("LNGINICIO"));
        pfinal = new GeoCoordinates(bundle.getDouble("LATFINAL"),bundle.getDouble("LNGFINAL"));




        //Permissions
        handleAndroidPermissions();

        //Map
        initMyLocation();

        setTapGestureHandler();


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
        loadMapScene();
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

    //Permissions
    private void handleAndroidPermissions() {
        permissionsRequestor = new LocationPermissionsRequestor(this);
        permissionsRequestor.request(new LocationPermissionsRequestor.ResultListener(){

            @Override
            public void permissionsGranted() {
                initMyLocation();
            }

            @Override
            public void permissionsDenied() {
                loadMapScene();
                Log.e("PermissionRequestor", "Permissions denied by user.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    //Initialize Map
    private void loadMapScene(){
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, mapError -> {
            if(mapError == null){
                if(!locationEnable){
                    mapView.getCamera().lookAt(new GeoCoordinates(0,0),2500);

                }
                //onClickListener();
                setTapGestureHandler();

            }else{
                //TODO: log error
            }
        });
    }

    private void setTapGestureHandler() {
        mapView.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(Point2D touchPoint) {
                pickMapMarker(touchPoint);
            }
        });
    }

    private void pickMapMarker(final Point2D touchPoint) {

        float radiusInPixel = 2;
        mapView.pickMapItems(touchPoint, radiusInPixel, new MapViewBase.PickMapItemsCallback() {
            @Override
            public void onPickMapItems(@NonNull PickMapItemsResult pickMapItemsResult) {
                // Note that 3D map markers can't be picked yet. Only marker, polgon and polyline map items are pickable.
                List<MapMarker> mapMarkerList = pickMapItemsResult.getMarkers();
                if (mapMarkerList.size() == 0) {
                    return;
                }
                MapMarker topmostMapMarker = mapMarkerList.get(0);
                Metadata metadata = topmostMapMarker.getMetadata();
                if (metadata != null) {
                    String message = "No message found.";
                    String string = metadata.getString("Distancia");
                    if (string != null) {
                        message = string;
                    }
                    Toast.makeText(RoutasActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                if(locationEnable){
                    showRoute(topmostMapMarker);


                }else{
                    Toast.makeText(RoutasActivity.this, "No se puede mostrar la ruta sin localizacion actual", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showRoute(MapMarker destination) {
        if(locationEnable && !destination.getCoordinates().equals(myMarker.getCoordinates())) {
            try {
                routingEngine = new RoutingEngine();
                Waypoint startWaypoint = new Waypoint(myMarker.getCoordinates());
                Waypoint destinationWaypoint = new Waypoint(destination.getCoordinates());

                List<Waypoint> waypoints =
                        new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

                routingEngine.calculateRoute(
                        waypoints,
                        new CarOptions(),
                        new CalculateRouteCallback() {
                            @Override
                            public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {
                                if (routingError == null) {
                                    Route route = routes.get(0);
                                    showRouteOnMap(route);
                                    //Metadata metadata = new Metadata();
                                    //metadata.setString("Distancia", "Distancia: " + route.getLengthInMeters() + " metros");
                                    //marker.setMetadata(metadata);
                                    logRouteViolations(route);
                                } else {
                                    Log.e("onRouteCalculated", "Error while calculating a route:" + routingError.toString());
                                }
                            }
                        });
            } catch (InstantiationErrorException e) {
                throw new RuntimeException("Initialization of RoutingEngine failed: " + e.error.name());
            }
        }
    }

    private void showRouteOnMap(Route route) {
        // Show route as polyline.
        GeoPolyline routeGeoPolyline;
        try {
            routeGeoPolyline = new GeoPolyline(route.getPolyline());
        } catch (InstantiationErrorException e) {
            // It should never happen that a route polyline contains less than two vertices.
            return;
        }

        float widthInPixels = 20;
        if(routeMapPolyline!=null){
            mapView.getMapScene().removeMapPolyline(routeMapPolyline);
        }
        routeMapPolyline = new MapPolyline(routeGeoPolyline,widthInPixels, Color.valueOf(0, 0.56f, 0.54f, 0.63f)); // RGBA
        mapView.getMapScene().addMapPolyline(routeMapPolyline);
    }

    // A route may contain several warnings, for example, when a certain route option could not be fulfilled.
    // An implementation may decide to reject a route if one or more violations are detected.
    private void logRouteViolations(Route route) {
        for (Section section : route.getSections()) {
            for (Notice notice : section.getNotices()) {
                Log.e("RouteViolation", "This route contains the following warning: " + notice.code.toString());
            }
        }
    }

    //My Location Updates
    private void initMyLocation() {
        if(locationEnable)
            return;
        if(ContextCompat.checkSelfPermission(this, locationPerm)== PackageManager.PERMISSION_GRANTED) {
            locationEnable=true;
            myMapImage= MapImageFactory.fromResource(this.getResources(),R.drawable.poi);
            encImage = MapImageFactory.fromResource(this.getResources(),R.drawable.poi8);
            platformPositioningProvider = new PlatformPositioningProvider(RoutasActivity.this);
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
                if(routeMapPolyline!=null){
                    mapView.getMapScene().removeMapPolyline(routeMapPolyline);
                }
                if(myMarker==null){
                    Anchor2D anchor2D=new Anchor2D(0.5f,1.0f);
                    myMarker=new MapMarker(new GeoCoordinates(location.getLatitude(),location.getLongitude()),myMapImage,anchor2D);
                    mapView.getMapScene().addMapMarker(myMarker);
                    mapView.getCamera().lookAt(new GeoCoordinates(pfinal.latitude,pfinal.longitude),2500);
                    puntoEncuentro = new MapMarker(new GeoCoordinates(pfinal.latitude,pfinal.longitude),encImage,anchor2D);
                    mapView.getMapScene().addMapMarker(puntoEncuentro);
                }else{
                    myMarker.setCoordinates(new GeoCoordinates(location.getLatitude(),location.getLongitude()));
                    cambiarRuta(myMarker.getCoordinates(),pfinal);
                }
            }
        });
    }

    public void addRouteButtonClicked(View view) {

    }

    public void volverButtonClicked(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void cambiarRuta(GeoCoordinates i, GeoCoordinates f){
        //routingMachine.addRoute(i,f);
    }

    public void addWaypointsButtonClicked(View view) {
        //routingMachine.addWaypoints();
    }

    public void clearMapButtonClicked(View view) {
        //routingMachine.clearMap();
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

            Intent intent = new Intent(RoutasActivity.this, LoginActivity.class);
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