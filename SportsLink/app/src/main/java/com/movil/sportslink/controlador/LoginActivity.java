package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.movil.sportslink.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Views
    EditText email, password;
    Button login;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUpTextView = findViewById(R.id.signUpTextView);
<<<<<<< HEAD
        Button iniciarSesion = findViewById(R.id.iniciarSesionButton);
        iniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        });
=======
        Button login = findViewById(R.id.iniciarSesionButton);

>>>>>>> autenticacion
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
            startActivity(intent);
        });
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                if(validatForm(em,pass)){
                    signIn(em,pass);
                }

            }
        });

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

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "" ;

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            //Autenticado
            //startActivity(new Intent(this, ActividadesSegunPreferenciasActivity.class));
            startActivity(new Intent(this, RoutesActivity.class));
        }else{
            email.setText("");
            password.setText("");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }
}