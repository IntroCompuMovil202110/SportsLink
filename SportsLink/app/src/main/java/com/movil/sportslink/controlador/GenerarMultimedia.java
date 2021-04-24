package com.movil.sportslink.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.movil.sportslink.R;

public class GenerarMultimedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_multimedia);
        Button btnContinueTakePicture = findViewById(R.id.btnContinueTakePicture);
        btnContinueTakePicture.setOnClickListener(v -> finish());
    }
}