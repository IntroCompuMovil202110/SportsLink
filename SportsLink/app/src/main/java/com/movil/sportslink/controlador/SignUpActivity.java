package com.movil.sportslink.controlador;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.movil.sportslink.R;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView signInTextView = findViewById(R.id.signInTextView);
        Button continueButton = findViewById(R.id.signUpContinueButton);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRedirec();
            }

        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                if(validatForm(em,pass)){
                    signUp(em,pass);
                }

            }

        });
    }

    private void loginRedirec() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    protected  void signUp(String em,String pass){
        mAuth.createUserWithEmailAndPassword(em,pass)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null){
                        //UserProfileChangeRequest.
                        updateUI(user);
                    }
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            //Autenticado
            System.out.println("Registrado");
            startActivity(new Intent(this, MainActivity.class));
        }else{
            email.setText("");
            password.setText("");
        }
    }

    private boolean validatForm(String email, String password){
        if(email != null && password != null){
            if(!email.isEmpty() && !password.isEmpty()){
                if(email.contains("@") && password.length()>5){
                    return true;
                }else{
                    this.email.setError("Invalid email address");
                    this.password.setError("Password must be at leat 6 characters long");
                }
            }
        }else{

        }
        return false;
    }
}