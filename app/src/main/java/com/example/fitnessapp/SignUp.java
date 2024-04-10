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
        //call backend with for values to signup
        //if signed up successfully

        Intent i = new Intent(this, UserHome.class);
        startActivity(i);
    }
}