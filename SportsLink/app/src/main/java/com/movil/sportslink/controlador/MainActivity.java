package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.movil.sportslink.R;
import com.movil.sportslink.services.MeetingStartedService;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ActividadesSegunPreferenciasFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_search) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new EncuentrosUsuarioFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_chat) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ConversacionesFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_profile) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new Perfil_Propio(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            }
            return true;
        });
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onStart(){
        super.onStart();
        startMeetingStartedListenerService();
    }

    private void startMeetingStartedListenerService() {
        Intent intent = new Intent(MainActivity.this, MeetingStartedService.class);
        MeetingStartedService.enqueueWork(MainActivity.this, intent);
    }

}
