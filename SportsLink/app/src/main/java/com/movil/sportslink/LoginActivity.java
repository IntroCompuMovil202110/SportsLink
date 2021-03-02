package com.movil.sportslink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUpTextView = findViewById(R.id.signUpTextView);
        Button iniciarSesion = findViewById(R.id.iniciarSesionButton);
        iniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActividadesSegunPreferenciasActivity.class);
            startActivity(intent);
        });
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
            startActivity(intent);
        });

    }
}