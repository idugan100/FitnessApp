package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class AuthRequestFactory {

    public static StringRequest createAuthRequest(String url, JSONObject body, Context c){
        return  new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //setup singleton user with token, id, and admin level
                        AppUser.initialize(response);
                        Intent i = new Intent(c, UserHome.class);
                        c.startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c.getApplicationContext(), "authentication error, please try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() {
                // Override this method to send the JSON body
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                // Specify content type as application/json
                return "application/json";
            }
        };
    }
}
