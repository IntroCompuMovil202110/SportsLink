package com.movil.sportslink.controlador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpParte2Activity extends AppCompatActivity {

    final int IMAGE_PICKER_REQUEST = 1;
    final int REQUEST_IMAGE_CAPTURE = 2;
    ImageView image;
    Button selectImagen;
    Button bCamara;
    Button continuar;
    String currentPhotoPath;
    Usuario usuarioRegistrar;
    Bitmap fotoPasar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_parte2);

        image = findViewById(R.id.imagen);
        selectImagen = findViewById(R.id.selectImagen);
        bCamara = findViewById(R.id.camara);
        continuar = findViewById(R.id.continuar);
        Bundle bundle= getIntent().getBundleExtra("bundle");
        fotoPasar = null;

        if(getIntent().getExtras() != null) {
            Usuario usuario = (Usuario) getIntent().getSerializableExtra("user");
        }else{
            System.out.println("Usuario es null");
        }



        selectImagen.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagen();
            } else {
                permisos();
            }

        });

        bCamara.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                permisosCamera();
            }

        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUI();

            }

        });
    }

    private void updateUI(){
            Intent intent = new Intent(this, Perfil_Propio.class);
            intent.putExtra("user", usuarioRegistrar);
            startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);

        switch(requestCode) {
            case IMAGE_PICKER_REQUEST:
                if(resultCode== RESULT_OK){
                    try {
                        final Uri imageUri= data.getData();
                        final InputStream imageStream= getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                System.out.println(data.getExtras());

                File imgFile = new  File(currentPhotoPath);
                if(imgFile.exists())            {
                    image.setImageURI(Uri.fromFile(imgFile));
                }



                galleryAddPic();
                break;
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



    private void permisos(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                // Show anexpanationto theuser*asynchronously*
            }
            // requestthe
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_PICKER_REQUEST);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS es una// constante definida en la aplicación, se debe usar// en el callbackpara identificar el permiso }
        }
    }

    private void permisosCamera(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                // Show anexpanationto theuser*asynchronously*
            }
            // requestthe
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS es una// constante definida en la aplicación, se debe usar// en el callbackpara identificar el permiso }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[]grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IMAGE_PICKER_REQUEST: {
                // Ifrequestiscancelled, theresultarraysare empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissionwasgranted, continuewithtaskrelatedtopermission
                    seleccionarImagen();
                } else {
                    // permissiondenied, disablefunctionalitythatdependsonthispermission.
                }
                return;
            }// other'case' linestocheckforother// permissionsthisapp mightrequest}}
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissionwasgranted, continuewithtaskrelatedtopermission
                    tomarFoto();
                } else {
                    // permissiondenied, disablefunctionalitythatdependsonthispermission.
                }
                return;
            }

        }
    }

    private void tomarFoto() {


        if(true){

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.movil.taller2.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

            //startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

        }

    }

    private void seleccionarImagen() {
        Intent pickImage= new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
    }
}