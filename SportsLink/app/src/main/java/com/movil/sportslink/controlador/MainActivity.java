package com.movil.sportslink.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.movil.sportslink.R;
import com.movil.sportslink.modelo.ApiRest;
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
                consumeRESTVolley();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ActividadesSegunPreferenciasFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_search) {
                consumeRESTVolley();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new EncuentrosUsuarioFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_chat) {
                consumeRESTVolley();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new ConversacionesFragment(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            } else if (itemId == R.id.navigation_profile) {
                consumeRESTVolley();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new Perfil_Propio(), null)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            }
            return true;
        });
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    public void consumeRESTVolley() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.boredapi.com/api/";
        //String path = "currency/cop";
        String query = "activity?type=recreational";
        StringRequest req = new StringRequest(Request.Method.GET, url  + query,
                new Response.Listener() {
                    public void onResponse(Object response) {
                        String data = (String) response;
                        Gson gson = new Gson();
                        ApiRest api;
                        api = gson.fromJson((String)response,ApiRest.class);
                        Toast.makeText(MainActivity.this,api.getActivity() , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Error rest"+error.getCause());
                    }
                });
        queue.add(req);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMeetingStartedListenerService();
    }

    private void startMeetingStartedListenerService() {
        Intent intent = new Intent(MainActivity.this, MeetingStartedService.class);
        MeetingStartedService.enqueueWork(MainActivity.this, intent);
    }

}
