package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllActivities extends AppCompatActivity {

    private ArrayList<ExerciseActivity> all = new ArrayList<ExerciseActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Log.d("msg","loaded");
        loadUserActivities();
        setContentView(R.layout.activity_all_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void backToHome(View view) {
        Intent i = new Intent(this, UserHome.class);
        startActivity(i);
    }

    private void loadUserActivities(){
        String url ="http://18.226.82.203:8080/activities/"+User.getInstance().getId();
        JsonArrayRequest j = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i =0; i<response.length();i++){
                            try{
                                String name=response.getJSONObject(i).getString("name");
                                Log.d("name",name);
                                String intensity = response.getJSONObject(i).getString("intensity");
                                int duration = response.getJSONObject(i).getInt("duration");
                                String day = response.getJSONObject(i).getString("date");
                                ExerciseActivity e = new ExerciseActivity(name,duration,day,intensity);
                                all.add(e);
                            }catch(Exception e){
                                Log.d("dubug",e.getMessage());
                            }
                        }
                        setUpTable();
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("msg","not recieved");
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ User.getInstance().getToken());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(j);
    }
    private void setUpTable(){
        TableLayout t = findViewById(R.id.activityTable);
        for(int i=0; i<all.toArray().length;i++){
            TableRow row = new TableRow(this);

            TextView date = new TextView(this);
            date.setText(all.get(i).day.split("T")[0]);
            date.setTextSize(15);

            TextView activity = new TextView(this);
            activity.setText(all.get(i).name);
            activity.setTextSize(15);

            TextView duration = new TextView(this);
            duration.setText(String.valueOf(all.get(i).duration));
            duration.setTextSize(15);

            TextView intensity = new TextView(this);
            intensity.setText(all.get(i).intensity);
            intensity.setTextSize(15);

            row.addView(date);
            row.addView(activity);
            row.addView(duration);
            row.addView(intensity);
            t.addView(row);
        }
    }
}