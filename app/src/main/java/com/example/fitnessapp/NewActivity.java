package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewActivity extends AppCompatActivity {
    CalendarView cal;
    Spinner activity;
    EditText duration;
    Spinner intensity;
    Date workoutDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new);
        cal = findViewById(R.id.calender);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                workoutDate = calendar.getTime();
            }
        });
        activity = findViewById(R.id.activity);
        duration = findViewById(R.id.duration);
        intensity = findViewById(R.id.intensity);
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

    public void createActivity(View view) {
        String activityName = activity.getSelectedItem().toString();
        String intensityString = intensity.getSelectedItem().toString();
        int durationValue = Integer.parseInt(duration.getText().toString());

        //create json
        JSONObject body = new JSONObject();
        try {
            body.put("name",activityName);
            body.put("intensity",intensityString);
            body.put("duration",durationValue);
            body.put("date",workoutDate.toInstant().toString());
        }catch (Exception error){
            Toast.makeText(getApplicationContext(), "json error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //pass json and token to a request factory
        String url = "http://18.226.82.203:8080/activities/"+User.getInstance().getId();
        Context c = this;

        //send request

        StringRequest req = new StringRequest(
                Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(NewActivity.this,AllActivities.class);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error:"+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ User.getInstance().getToken());
                return params;
            }
            public byte[] getBody() {
                // Override this method to send the JSON body
                return body.toString().getBytes();
            }};
        Volley.newRequestQueue(this).add(req);

    }
}