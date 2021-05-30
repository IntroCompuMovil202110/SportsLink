package com.movil.sportslink.controlador;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.LocationPermissionsRequestor;
import com.movil.sportslink.modelo.PlatformPositioningProvider;
import com.movil.sportslink.modelo.Ubicacion;
import com.movil.sportslink.modelo.Usuario;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    //Permission
    private String locationPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_ID=1;
    private boolean locationEnable=false;
    private LocationPermissionsRequestor permissionsRequestor;
    //My Location
    private android.location.Location myLocation;
    private PlatformPositioningProvider platformPositioningProvider = null;

    public static final String PATH_USERS="users/";
    public static final String PATH_LOCATION="location/";

    EditText email, password, username, descp, lastname, cell;
    FirebaseAuth mAuth;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView signInTextView = findViewById(R.id.signInTextView);
        Button continueButton = findViewById(R.id.signUpContinueButton);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.nombreUsuario);
        descp = findViewById(R.id.descripcion);
        lastname = findViewById(R.id.apellido);
        cell = findViewById(R.id.celular);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();


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

        //Permissions
        handleAndroidPermissions();

        //Map
        initMyLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(locationEnable){
            platformPositioningProvider.stopLocating();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(locationEnable){
            starLocating();
        }else{
            initMyLocation();
        }
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
                        guardarEnFirebase(user);
                        updateUI(user);
                    }
                }
            }
        });
    }


    private void updateUI(FirebaseUser user){
        if(user != null){
            //Autenticado


            Intent intent = new Intent(this, MainActivity.class);


            //System.out.println(userN.toString());
            //intent.putExtra("user", userN.toString());
            startActivity(intent);
        }else{
            email.setText("");
            password.setText("");

        }
    }

    private void guardarEnFirebase(FirebaseUser user){
        Usuario userN = new Usuario();

        userN.setId(user.getUid());
        userN.setName(username.getText().toString());
        userN.setCorreo(user.getEmail());
        userN.setDescripcion(descp.getText().toString());
        userN.setLastName(lastname.getText().toString());
        userN.setNumeroCelular(cell.getText().toString());
        userN.setEncuentros(new ArrayList<>());
        myRef = database.getReference(PATH_USERS + user.getUid());
        myRef.setValue(userN);

        if(myLocation != null){
            userN.setUbicacion(new Ubicacion(myLocation.getLatitude(),myLocation.getLongitude(),true));
            //DatabaseReference postsRef = myRef.child("/ubicacion/");
            //DatabaseReference newPostRef = postsRef.push();
            //postsRef.setValue(new Ubicacion(myLocation.getLatitude(),myLocation.getLongitude(),true));
        }else{
            userN.setUbicacion(new Ubicacion(50,50,false));
        }
        myRef.setValue(userN);


        //uploadImage(user);
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

    //Permissions
    private void handleAndroidPermissions() {
        permissionsRequestor = new LocationPermissionsRequestor(this);
        permissionsRequestor.request(new LocationPermissionsRequestor.ResultListener(){

            @Override
            public void permissionsGranted() {
                initMyLocation();
            }

            @Override
            public void permissionsDenied() {
                Log.e("PermissionRequestor", "Permissions denied by user.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    //My Location Updates
    private void initMyLocation() {
        if(locationEnable)
            return;
        if(ContextCompat.checkSelfPermission(this, locationPerm)== PackageManager.PERMISSION_GRANTED) {
            locationEnable=true;
            platformPositioningProvider = new PlatformPositioningProvider(SignUpActivity.this);
            starLocating();
        }

    }

    private void starLocating() {
        if(platformPositioningProvider==null){
            //TODO: Handle error
            return;
        }
        platformPositioningProvider.startLocating(new PlatformPositioningProvider.PlatformLocationListener() {
            @Override
            public void onLocationUpdated(android.location.Location location) {
                //updateUserLocation(location.getLatitude(),location.getLongitude());
                myLocation=location;

            }
        });
    }

}