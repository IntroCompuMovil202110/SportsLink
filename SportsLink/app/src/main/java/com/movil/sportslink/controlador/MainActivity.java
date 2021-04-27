package com.movil.sportslink.controlador;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.movil.sportslink.R;

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
}
