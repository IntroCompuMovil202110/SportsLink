package com.movil.sportslink.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.movil.sportslink.controlador.MainActivity;
import com.movil.sportslink.modelo.ApiRest;

public class RecommendationService {

    public static void consumeRESTVolley(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.boredapi.com/api/";
        //String path = "currency/cop";
        String query = "activity?type=social";
        StringRequest req = new StringRequest(Request.Method.GET, url  + query,
                new Response.Listener() {
                    public void onResponse(Object response) {
                        String data = (String) response;
                        Gson gson = new Gson();
                        ApiRest api;
                        api = gson.fromJson((String)response,ApiRest.class);
                            Toast.makeText(context,api.getActivity() , Toast.LENGTH_LONG).show();
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
}
