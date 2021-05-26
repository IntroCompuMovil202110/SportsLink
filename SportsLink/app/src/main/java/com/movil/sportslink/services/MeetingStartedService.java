package com.movil.sportslink.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.movil.sportslink.R;
import com.movil.sportslink.controlador.MainActivity;
import com.movil.sportslink.modelo.Encuentro;

import java.util.ArrayList;
import java.util.List;


public class MeetingStartedService extends JobIntentService {
    private static final int JOB_ID = 12;
    private static final String TAG = "N";
    private static final String PATH_MEETINGS = "encuentros/";
    private static final String CHANNEL_ID = "sportslink";

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ValueEventListener val;


    ArrayList<Encuentro> encuentros;



    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, MeetingStartedService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        encuentros = new ArrayList<>();
        //Boolean estado = false;
        myRef = database.getReference("estadoEventos/");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String e = snapshot.getValue().toString();
                Boolean estado = snapshot.getValue(Boolean.class);
                System.out.println(e);
                if(estado){
                    buildAndShowNotification(e,"OKI");
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannelclass is new and not in the support library

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            CharSequence cname = "channel";

            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //IMPORTANCE_MAX MUESTRA LA NOTIFICACIÓN ANIMADA

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, cname, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildAndShowNotification(String nombre, String id){
        createNotificationChannel();


        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.home_icon);
        mBuilder.setContentTitle("Nuevo usuario disponible!");
        mBuilder.setContentText(nombre + " ahora esta disponible, mira su ubicación");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Intent intent = new Intent(this, Seguimiento.class);
        //intent.putExtra("ID",id);
        System.out.println("NOTICIACION A "+ id);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);
        //mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        int notificationId= 001;
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);
        // notificationIdesun enterounicodefinidopara cadanotificacionque se lanzanotification
        notificationManager.notify(notificationId, mBuilder.build());

    }




}
