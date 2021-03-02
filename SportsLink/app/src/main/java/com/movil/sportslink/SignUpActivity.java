package com.movil.sportslink;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView signInTextView = findViewById(R.id.signInTextView);
        signInTextView.setOnClickListener(v -> finish());
    }
}