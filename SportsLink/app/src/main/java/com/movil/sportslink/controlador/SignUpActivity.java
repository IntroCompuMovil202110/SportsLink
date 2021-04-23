package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.movil.sportslink.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView signInTextView = findViewById(R.id.signInTextView);
        Button continueButton = findViewById(R.id.signUpContinueButton);
        signInTextView.setOnClickListener(v -> finish());
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), BuscarEncuentroActivity.class);
            startActivity(intent);
        });
    }
}