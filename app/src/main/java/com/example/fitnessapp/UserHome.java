package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        TextView admin = findViewById(R.id.adminButton); // Assuming your button has id "admin_button" in your XML layout file

        //show admin link only if user is an admin
        if (AppUser.getInstance().isAdmin()) {
            admin.setVisibility(View.VISIBLE); // If user is admin, show the button
        } else {
            admin.setVisibility(View.GONE); // If user is not admin, hide the button
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void goToHeatMap(View view) {
        Intent i = new Intent(this, HeatMapActivity.class);
        startActivity(i);
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