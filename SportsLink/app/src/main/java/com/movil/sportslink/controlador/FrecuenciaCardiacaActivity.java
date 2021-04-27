package com.movil.sportslink.controlador;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.movil.sportslink.R;

public class FrecuenciaCardiacaActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView heartRate;
    private final String PERMISSION = Manifest.permission.BODY_SENSORS;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frecuencia_cardiaca);
        heartRate = findViewById(R.id.frecuenciaCardiacaTextView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        heartRate.setText(Math.round(event.values[0]) + " BPM");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void requestPermission() {
        if (!hasPermission()) {
            if (shouldShowRequestPermissionRationale(PERMISSION)) {
                Toast.makeText(this, "NEEDED", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{PERMISSION}, PERMISSION_CODE);
        }
    }

    public boolean hasPermission() {
        return checkSelfPermission(PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }
}