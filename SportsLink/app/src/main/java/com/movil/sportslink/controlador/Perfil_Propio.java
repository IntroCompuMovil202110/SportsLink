package com.movil.sportslink.controlador;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Perfil_Propio extends Fragment {
    Button editarPerfil;


    @Nullable

    FirebaseAuth mAuth;

    Usuario usuarioPerfil;
    Button cerrarSesion;
    ImageView image;
    TextView nombre, desc;
    final int IMAGE_PICKER_REQUEST = 1;
    final int REQUEST_IMAGE_CAPTURE = 2;
    Button selectImagen;
    Button bCamara;
    Button continuar;
    String currentPhotoPath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_propio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //editarPerfil = view.findViewById(R.id.buttonEditarPerfil);
        Bundle bundle= getActivity().getIntent().getBundleExtra("bundle");

        image = view.findViewById(R.id.image);
        nombre = view.findViewById(R.id.textViewNombreUsuario);
        desc = view.findViewById(R.id.descripcion);

        selectImagen = view.findViewById(R.id.select);
        bCamara = view.findViewById(R.id.camara);

        /*try{


            usuarioPerfil = (Usuario) getActivity().getIntent().getSerializableExtra("user");

            if(usuarioPerfil.getFoto() != null){
                image.setImageBitmap(usuarioPerfil.getFoto());
            }
            nombre.setText(usuarioPerfil.getNombre());
            desc.setText(usuarioPerfil.getDescripcion());
        }catch(Exception e){
            usuarioPerfil = null;
        }*/

        cerrarSesion = view.findViewById(R.id.cerrarSesion);
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        nombre.setText(email);




        cerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        });

        selectImagen.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagen();
            } else {
                permisos();
            }

        });

        bCamara.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                permisosCamera();
            }

        });


    }



    private void setPic() {
        // Get the dimensions of the View
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        image.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);

        switch(requestCode) {
            case IMAGE_PICKER_REQUEST:
                if(resultCode== RESULT_OK){
                    try {
                        final Uri imageUri= data.getData();
                        final InputStream imageStream= getContext().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                        //terminar();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode== RESULT_OK){
                    System.out.println(data.getExtras());
                    File imgFile = new  File(currentPhotoPath);
                    if(imgFile.exists())            {
                        image.setImageURI(Uri.fromFile(imgFile));
                    }

                    galleryAddPic();
                    //terminar();
                }

                break;
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        getContext().sendBroadcast(mediaScanIntent);
    }



    private void permisos(){

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                // Show anexpanationto theuser*asynchronously*
            }
            // requestthe
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_PICKER_REQUEST);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS es una// constante definida en la aplicación, se debe usar// en el callbackpara identificar el permiso }
        }
    }

    private void permisosCamera(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            // Shouldweshow anexplanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){
                // Show anexpanationto theuser*asynchronously*
            }
            // requestthe
            ActivityCompat.requestPermissions(getActivity(),
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
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.movil.sportslink.android.fileprovider",
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