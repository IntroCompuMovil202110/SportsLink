package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Metadata;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapScene;
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
import com.movil.sportslink.modelo.Encuentro;
import com.movil.sportslink.modelo.LocationPermissionsRequestor;
import com.movil.sportslink.modelo.Participante;
import com.movil.sportslink.modelo.PlatformPositioningProvider;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackingActivity extends AppCompatActivity implements SensorEventListener {

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
    private MapMarker myMarker=null;
    private MapMarker marker = null;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;


    private Sensor temperatureSensor;
    private Sensor humiditySensor;

    Boolean inicio = false;
    String idUsuarioSeguir;
    private ArrayList<MapMarker> usersMarkers;
    private ArrayList<MapImage> mapImages;
    TextView temperatura;
    TextView humedad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        temperatura = findViewById(R.id.temperatura);
        humedad = findViewById(R.id.humedad);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        usersMarkers = new ArrayList<>();
        //Inflate
        centerButton= findViewById(R.id.centerButton);
        centerButton.setVisibility(View.INVISIBLE);
        //staticLocationImage= MapImageFactory.fromResource(this.getResources(),R.drawable.poi2);

        //DataBase
        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        mapImages = new ArrayList<>();
        iniciarIconos();
        //Light Sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        initLightSensor();

        // Get a MapViewLite instance from the layout.
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        //Permissions
        handleAndroidPermissions();

        //Map
        idUsuarioSeguir = getIntent().getStringExtra("ID");
        System.out.println(idUsuarioSeguir);
        initMyLocation();

        seguimientoUsuarios(idUsuarioSeguir);
    }

    private void iniciarIconos() {
        MapImage otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi2);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi3);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi4);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi5);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi6);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi7);
        mapImages.add(otherImage);
        otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi8);
        mapImages.add(otherImage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        sensorManager.unregisterListener(lightSensorListener);
        if(locationEnable){
            platformPositioningProvider.stopLocating();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        temperatura.setText("Temperatura: "+"20 " + "C");
        humedad.setText("Humedad: " +"23%");
        if(locationEnable){
            starLocating();
        }else{
            initMyLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

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


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(Seguimiento.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (itemClicked == R.id.contacts){
            Intent intent = new Intent(Seguimiento.this, ContactsActivity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.availability){

        }
        return super.onOptionsItemSelected(item);
    }*/

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
                    Toast.makeText(TrackingActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                if(locationEnable){
                    showRoute(topmostMapMarker);


                }else{
                    Toast.makeText(TrackingActivity.this, "No se puede mostrar la ruta sin localizacion actual", Toast.LENGTH_SHORT).show();
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
            platformPositioningProvider = new PlatformPositioningProvider(TrackingActivity.this);
            centerButton.setVisibility(View.VISIBLE);
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
                    mapView.getCamera().lookAt(new GeoCoordinates(location.getLatitude(),location.getLongitude()),2500);
                }else{
                    myMarker.setCoordinates(new GeoCoordinates(location.getLatitude(),location.getLongitude()));
                }
            }
        });
    }

    private void actualizarUbicacion(String lat, String lng){

        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .se
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });*/
    }

    private void addLocationMarker(double latitude, double longitude, String name) {
        if(name!=null || !name.isEmpty()){
            TextView textView = new TextView(this);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setText(name);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setBackgroundResource(R.color.teal_700);
            linearLayout.setPadding(10, 10, 10, 10);
            linearLayout.addView(textView);

            mapView.pinView(linearLayout, new GeoCoordinates(latitude,longitude));
//            Anchor2D anchor2D=new Anchor2D(0.5f,1.0f);
//            MapMarker locationsMapMarker=new MapMarker(new GeoCoordinates(latitude,longitude),staticLocationImage,anchor2D);
//            Metadata metadata = new Metadata();
//            metadata.setString("name_key", name);
//            locationsMapMarker.setMetadata(metadata);
//            mapView.getMapScene().addMapMarker(locationsMapMarker);
        }
    }

    //Light Sensor
    private void initLightSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor!=null){
            lightSensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (mapView != null) {
                        if (event.values[0] < 10000) {
                            Log.i("MAPS", "DARK MAP " + event.values[0]);
                            changeScheme(MapScheme.NORMAL_NIGHT);
                        } else {
                            Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                            changeScheme(MapScheme.NORMAL_DAY);
                        }
                    }
                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
        }
    }

    private void changeScheme(MapScheme scheme) {
        mapView.getMapScene().loadScene(scheme, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if(mapError==null){

                }else{
                    //TODO: handle error
                }
            }
        });
    }

    //Extra Services
    public void centerMap(View view){
        if(locationEnable){
            mapView.getCamera().lookAt(new GeoCoordinates(myLocation.getLatitude(),myLocation.getLongitude()),2500);
        }
    }

    private void seguimientoUsuarios(String id){
        String idUser = mAuth.getCurrentUser().getUid();
        reference = database.getReference("/encuentros/" + id);
        DatabaseReference partReference = reference.child("participantes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                Encuentro encuentro = snapshot.getValue(Encuentro.class);
                ArrayList<Participante> participantes = encuentro.getParticipantes();

                participantes.removeIf(n -> (n.getId().equals(idUser)));

                for(Participante p : participantes){
                        System.out.println("USUARIO " + p.getId());
                        MapMarker nuevo = null;
                        MapMarker viejo = null;
                        Metadata metadata;
                        metadata = new Metadata();
                        int i = 0;
                        metadata.setString("ID",p.getId());
                        for(MapMarker n : usersMarkers){
                            try{
                                if(n.getMetadata().getString("ID") == null){
                                    System.out.println("LA TIENE NUL " + p.getId());
                                    break;
                                }
                            }catch (Exception e){
                                System.out.println("EL CULPABLE ES" + p.getId());
                                break;
                            }

                            if(n.getMetadata().getString("ID").equals(p.getId())){
                                nuevo = new MapMarker(new GeoCoordinates(p.getLatitude(), p.getLongitude()),
                                        (usersMarkers.indexOf(n) > 6) ? mapImages.get(6) : mapImages.get(usersMarkers.indexOf(n))
                                );
                                viejo = n;
                                i = usersMarkers.indexOf(n);
                                continue;

                            }
                        }

                        if(usersMarkers.isEmpty()){
                            MapMarker n;

                            n = new MapMarker(new GeoCoordinates(p.getLatitude(),p.getLongitude()),mapImages.get(0));
                            n.setMetadata(metadata);
                            usersMarkers.add(n);
                            mapView.getMapScene().addMapMarker(n);
                            System.out.println("PRIMER MARCADOR " + n.getMetadata());
                            continue;
                        }

                        if(nuevo != null && viejo != null){
                            nuevo.setMetadata(metadata);
                            viejo.setMetadata(metadata);
                            System.out.println(i + "ACTUALIZO A " + p.getId());
                            mapView.getMapScene().removeMapMarker(usersMarkers.get(i));
                            System.out.println("ANTES YA EN LISTA " + usersMarkers.size());
                            usersMarkers.set(usersMarkers.indexOf(viejo),nuevo);
                            System.out.println("INCREMENTO YA EN LISTA " + usersMarkers.size());
                            mapView.getMapScene().addMapMarker(nuevo);
                        }else{

                            if(usersMarkers.size() < 7){
                                System.out.println(usersMarkers.size());
                                nuevo = new MapMarker(new GeoCoordinates(p.getLatitude(), p.getLongitude()),mapImages.get(usersMarkers.size()-1));

                            }else{
                                nuevo = new MapMarker(new GeoCoordinates(p.getLatitude(), p.getLongitude()),mapImages.get(6));
                            }

                            nuevo.setMetadata(metadata);
                            System.out.println("ANTES " + usersMarkers.size());
                            usersMarkers.add(nuevo);
                            System.out.println("INCREMENTO " + usersMarkers.size());
                            mapView.getMapScene().addMapMarker(nuevo);
                            System.out.println("SE AGREGO A " + p.getId() + " COMO " +usersMarkers.size());
                        }



                }

                /*if(marker != null){
                    mapView.getMapScene().removeMapMarker(marker);
                }
                if(routeMapPolyline != null){
                    mapView.getMapScene().removeMapPolyline(routeMapPolyline);
                }*/
                //Participante other = snapshot.getValue(Participante.class);
                /*if(true){

                    if(!inicio){
                        Toast.makeText(TrackingActivity.this,"El usuario no esta disponible",Toast.LENGTH_LONG).show();
                        inicio = true;
                    }
                }else{
                    //Anchor2D anchor2DP=new Anchor2D(0.5f,1.0f);
                    //MapImage otherImage= MapImageFactory.fromResource(TrackingActivity.this.getResources(),R.drawable.poi2);
                    //marker =new MapMarker(new GeoCoordinates(other.getLatitude(),other.getLongitude()),otherImage, anchor2DP);
                    //mapView.getMapScene().addMapMarker(marker);
                    //showRoute(marker);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == temperatureSensor) {
            temperatura.setText("Temperatura: "+ event.values[0] + "C");
        } else if (event.sensor == humiditySensor) {
            humedad.setText("Humedad: " + event.values[0] + "%");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}