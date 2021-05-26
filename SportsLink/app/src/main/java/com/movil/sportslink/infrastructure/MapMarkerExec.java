package com.movil.sportslink.infrastructure;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.Metadata;
import com.here.sdk.core.Point2D;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapMarker3D;
import com.here.sdk.mapview.MapMarker3DModel;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.MapViewBase;
import com.here.sdk.mapview.PickMapItemsResult;

import com.movil.sportslink.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MapMarkerExec {

    private Context context;
    private MapView mapView;
    private final List<MapMarker> mapMarkerList = new ArrayList<>();
    private final List<MapMarker3D> mapMarker3DList = new ArrayList<>();


    public MapMarkerExec(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        MapCamera camera = mapView.getCamera();
        double distanceInMeters = 1000 * 10;
        camera.lookAt(new GeoCoordinates(52.520798, 13.409408), distanceInMeters);

        // Setting a tap handler to pick markers from map.
        setTapGestureHandler();

        Toast.makeText(context, "You can tap 2D markers.", Toast.LENGTH_LONG).show();
    }

    public void showAnchoredMapMarkers() {
        unTiltMap();

        for (int i = 0; i < 10; i++) {
            GeoCoordinates geoCoordinates = createRandomGeoCoordinatesAroundMapCenter();

            // Centered on location. Shown below the POI image to indicate the location.
            // The draw order is determined from what is first added to the map.
            addCircleMapMarker(geoCoordinates);

            // Anchored, pointing to location.
            addPOIMapMarker(geoCoordinates);
        }
    }

    public void showCenteredMapMarkers() {
        unTiltMap();

        GeoCoordinates geoCoordinates = createRandomGeoCoordinatesAroundMapCenter();

        // Centered on location.
        addPhotoMapMarker(geoCoordinates);

        // Centered on location. Shown above the photo marker to indicate the location.
        // The draw order is determined from what is first added to the map.
        addCircleMapMarker(geoCoordinates);
    }

    public void showFlatMarker() {
        // Tilt the map for a better 3D effect.
        tiltMap();

        GeoCoordinates geoCoordinates = createRandomGeoCoordinatesAroundMapCenter();

        // Adds a flat POI marker that rotates and tilts together with the map.
        addFlatMarker3D(geoCoordinates);

        // A centered 2D map marker to indicate the exact location.
        // Note that 3D map markers are always drawn on top of 2D map markers.
        addCircleMapMarker(geoCoordinates);
    }

    public void showMapMarker3D() {
        // Tilt the map for a better 3D effect.
        tiltMap();

        GeoCoordinates geoCoordinates = createRandomGeoCoordinatesAroundMapCenter();

        // Adds a textured 3D model.
        // It's origin is centered on the location.
        addMapMarker3D(geoCoordinates);
    }

    public void clearMap() {
        for (MapMarker mapMarker : mapMarkerList) {
            mapView.getMapScene().removeMapMarker(mapMarker);
        }
        mapMarkerList.clear();

        for (MapMarker3D mapMarker3D : mapMarker3DList) {
            mapView.getMapScene().removeMapMarker3d(mapMarker3D);
        }
        mapMarker3DList.clear();
    }

    private void addPOIMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.poi);

        // The bottom, middle position should point to the location.
        // By default, the anchor point is set to 0.5, 0.5.
        Anchor2D anchor2D = new Anchor2D(0.5F, 1);
        MapMarker mapMarker = new MapMarker(geoCoordinates, mapImage, anchor2D);

        Metadata metadata = new Metadata();
        metadata.setString("key_poi", "This is a POI.");
        mapMarker.setMetadata(metadata);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private void addPhotoMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.here_car);
        MapMarker mapMarker = new MapMarker(geoCoordinates, mapImage);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private void addCircleMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.circle);
        MapMarker mapMarker = new MapMarker(geoCoordinates, mapImage);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private void addFlatMarker3D(GeoCoordinates geoCoordinates) {
        // Place the files in the "assets" directory.
        // Full path example: app/src/main/assets/plane.obj
        // Adjust file name and path as appropriate for your project.
        // Note: The bottom of the plane is centered on the origin.
        String geometryFile = "plane.obj";

        // The POI texture is a square, so we can easily wrap it onto the 2 x 2 plane model.
        String textureFile = "poi_texture.png";
        checkIfFileExistsInAssetsFolder(geometryFile);
        checkIfFileExistsInAssetsFolder(textureFile);

        MapMarker3DModel mapMarker3DModel = new MapMarker3DModel(geometryFile, textureFile);
        MapMarker3D mapMarker3D = new MapMarker3D(geoCoordinates, mapMarker3DModel);
        // Scale marker. Note that we used a normalized length of 2 units in 3D space.
        mapMarker3D.setScale(60);

        mapView.getMapScene().addMapMarker3d(mapMarker3D);
        mapMarker3DList.add(mapMarker3D);
    }

    private void addMapMarker3D(GeoCoordinates geoCoordinates) {
        // Place the files in the "assets" directory.
        // Full path example: app/src/main/assets/obstacle.obj
        // Adjust file name and path as appropriate for your project.
        String geometryFile = "obstacle.obj";
        String textureFile = "obstacle_texture.png";
        checkIfFileExistsInAssetsFolder(geometryFile);
        checkIfFileExistsInAssetsFolder(textureFile);

        MapMarker3DModel mapMarker3DModel = new MapMarker3DModel(geometryFile, textureFile);
        MapMarker3D mapMarker3D = new MapMarker3D(geoCoordinates, mapMarker3DModel);
        mapMarker3D.setScale(6);

        mapView.getMapScene().addMapMarker3d(mapMarker3D);
        mapMarker3DList.add(mapMarker3D);
    }

    private void checkIfFileExistsInAssetsFolder(String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            assetManager.open(fileName);
        } catch (IOException e) {
            Log.e("MapMarkerExample", "Error: File not found!");
        }
    }

    private GeoCoordinates createRandomGeoCoordinatesAroundMapCenter() {
        GeoCoordinates centerGeoCoordinates = mapView.viewToGeoCoordinates(
                new Point2D(mapView.getWidth() / 2, mapView.getHeight() / 2));
        if (centerGeoCoordinates == null) {
            // Should never happen for center coordinates.
            throw new RuntimeException("CenterGeoCoordinates are null");
        }
        double lat = centerGeoCoordinates.latitude;
        double lon = centerGeoCoordinates.longitude;
        return new GeoCoordinates(getRandom(lat - 0.02, lat + 0.02),
                getRandom(lon - 0.02, lon + 0.02));
    }

    private double getRandom(double min, double max) {
        return min + Math.random() * (max - min);
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
                    String string = metadata.getString("key_poi");
                    if (string != null) {
                        message = string;
                    }
                    showDialog("Map Marker picked", message);
                    return;
                }

                showDialog("Map marker picked:", "Location: " +
                        topmostMapMarker.getCoordinates().latitude + ", " +
                        topmostMapMarker.getCoordinates().longitude);
            }
        });
    }

    private void tiltMap() {
        MapCamera.OrientationUpdate targetOrientation = new MapCamera.OrientationUpdate();
        targetOrientation.tilt = 60D;
        mapView.getCamera().setTargetOrientation(targetOrientation);
    }

    private void unTiltMap() {
        MapCamera.OrientationUpdate targetOrientation = new MapCamera.OrientationUpdate();
        targetOrientation.tilt = 0D;
        mapView.getCamera().setTargetOrientation(targetOrientation);
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
