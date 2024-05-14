package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getStats();
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

        if (savedInstanceState == null) {
            boolean isGroup = false;
            HeatMapFragment heatMapFragment = HeatMapFragment.newInstance(isGroup);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.heatmap_container, heatMapFragment);
            transaction.commit();
        }
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

    private void getStats(){
        String url = "http://18.226.82.203:8080/activities/stats/" + AppUser.getInstance().getId();

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
            response -> {
                try {
                    int totalDays = response.getInt("totalDays");
                    int totalHighMinutes = response.getInt("totalHigh");
                    int totalMediumMinutes = response.getInt("totalMedium");
                    int totalLowMinutes = response.getInt("totalLow");
                    int totalAllMinutes = totalLowMinutes+totalHighMinutes+totalMediumMinutes;
                    TextView totalDaysView = findViewById(R.id.totalmin);
                    totalDaysView.setText(Integer.toString(totalDays) + " workouts");
                    TextView totalMinutes = findViewById(R.id.totaldays);
                    totalMinutes.setText(Integer.toString(totalAllMinutes) + " minutes");
                } catch (Exception e) {
                    Log.d("debug", "Error parsing response: " + e.getMessage());
                }
            },
            error -> Log.d("debug", "Error fetching data: " + error.getMessage())
    ) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("Authorization", "Bearer " + AppUser.getInstance().getToken());
            return params;
        }
    };

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
}