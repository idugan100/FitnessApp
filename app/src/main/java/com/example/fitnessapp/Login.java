package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void signUpButtonLink(View view) {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    public void handleLogin(View view) {
        EditText u = findViewById(R.id.username);
        String username = u.getText().toString();
        EditText p = findViewById(R.id.password);
        String password = p.getText().toString();
        //put into json
        JSONObject body = new JSONObject();
        try {
            body.put("username",username);
            body.put("password",password);

        }catch (Exception e){}

        String url = "http://18.226.82.203:8080/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //setup singleton user with token, id, and admin level
                        User.initialize(response);
                        Intent i = new Intent(Login.this, UserHome.class);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "incorrect username or password", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(this).add(stringRequest);
    }
}