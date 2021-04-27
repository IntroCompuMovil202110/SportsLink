package com.movil.sportslink.controlador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.movil.sportslink.R;
import com.movil.sportslink.infrastructure.PermissionsRequestor;
import com.movil.sportslink.infrastructure.RoutingMachine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.here.sdk.mapviewlite.MapViewLite;

public class RoutasActivity extends AppCompatActivity {

    private static final String TAG = RoutasActivity.class.getSimpleName();

    private PermissionsRequestor permissionsRequestor;
    private MapViewLite mapView;
    private RoutingMachine routingMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routas);

        // Get a MapView instance from layout.
        mapView = findViewById(R.id.map_view);
        System.out.println(mapView);
        mapView.onCreate(savedInstanceState);

        handleAndroidPermissions();
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
                } else {
                    Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

    public void addRouteButtonClicked(View view) {
        routingMachine.addRoute(new GeoCoordinates(4.632713, -74.086050),
                new GeoCoordinates(4.62718184324595, -74.06538050924736));
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