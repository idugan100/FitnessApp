package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToNewActivity(View view) {
        Intent i = new Intent(this, NewActivity.class);
        startActivity(i);
    }

    public void goToAllActivities(View view) {
        Intent i = new Intent(this, AllActivities.class);
        startActivity(i);
    }

    public void goToAllNotifications(View view) {
        Intent i = new Intent(this, AllNotifications.class);
        startActivity(i);
    }

    public void goToAdminPanel(View view) {
        Intent i = new Intent(this, AdminPanel.class);
        startActivity(i);
    }
}