package com.movil.sportslink.controlador;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

public class IniciarEncuentroActivity extends AppCompatActivity implements SensorEventListener {
    private TextView nombreEncuentro;
    private Button rutaEncuentroButton;
    private Button recorridoButton;
    private TextView temperaturaTextView;
    private TextView humedadTextView;
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_encuentro);
        nombreEncuentro = findViewById(R.id.nombreEncuentroTextView);
        rutaEncuentroButton = findViewById(R.id.rutaEncuentroButton);
        recorridoButton = findViewById(R.id.recorridoButton);
        temperaturaTextView = findViewById(R.id.temperaturaTextView);
        humedadTextView = findViewById(R.id.humedadTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == temperatureSensor) {
            temperaturaTextView.setText(event.values[0] + "C");
        } else if (event.sensor == humiditySensor) {
            humedadTextView.setText(event.values[0] + "%");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}