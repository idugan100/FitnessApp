package com.example.fitnessapp;

import static java.util.Base64.getDecoder;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import android.util.Base64;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void clickLoginLink(View view) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void handleSignup(View view) {
        EditText u = findViewById(R.id.username);
        String userName = u.getText().toString();
        EditText p = findViewById(R.id.password);
        String password = p.getText().toString();
        EditText p2 = findViewById(R.id.confirm_password);
        String password2 = p2.getText().toString();

        if(password.isEmpty() || userName.isEmpty() || password2.isEmpty()){
            Toast.makeText(getApplicationContext(), "all fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password2.equals(password)){
            Toast.makeText(getApplicationContext(), "passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("username",userName);
            body.put("password",password);
        }catch (Exception ignored){}

        String url = "http://18.226.82.203:8080/signup";

        StringRequest stringRequest = AuthRequestFactory.createAuthRequest(url,body,this);

        Volley.newRequestQueue(this).add(stringRequest);
    }
}