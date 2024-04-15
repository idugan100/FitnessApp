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

        }catch (Exception ignored){}

        String url = "http://18.226.82.203:8080/login";

        StringRequest stringRequest = AuthRequestFactory.createAuthRequest(url,body,this);
        Volley.newRequestQueue(this).add(stringRequest);
    }
}