package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_stats);
        EdgeToEdge.enable(this);
        loadGroupData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            boolean isGroup = true;
            HeatMapFragment heatMapFragment = HeatMapFragment.newInstance(isGroup);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.heatmap_container, heatMapFragment);
            transaction.commit();
        }
    }

    private void loadGroupData(){
        String url ="http://18.226.82.203:8080/activities/stats/all";
        JsonObjectRequest j = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserStats stats= new UserStats();
                        ArrayList<DailyTotal> totalList = new ArrayList<DailyTotal>();
                        try{
                            //parse api response into data for stats
                            stats.totalDays = response.getInt("totalDays");
                            stats.totalHighMinutes = response.getInt("totalHigh");
                            stats.totalMediumMinutes = response.getInt("totalMedium");
                            stats.totalLowMinutes = response.getInt("totalLow");
                            JSONArray days=response.getJSONArray("dailyTotals");
                            for(int i =0; i<days.length();i++) {
                                //put daily totals data into array of DailyTotal objects
                                String day = days.getJSONObject(i).getString("date");
                                int duration = days.getJSONObject(i).getInt("totalMinutes");
                                DailyTotal d = new DailyTotal(day,duration);
                                totalList.add(d);
                            }
                            stats.dailyTotals=totalList;
                        }catch(Exception e){
                            Log.d("dubug",e.getMessage());
                        }
                        TextView t = findViewById(R.id.groupstats);
                        t.setText(stats.getDisplay());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("msg","not recieved"+error.getMessage());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ AppUser.getInstance().getToken());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(j);
    }

    public void backToAdmin(View view) {
        Intent i = new Intent(this, AdminPanel.class);
        startActivity(i);
    }
}