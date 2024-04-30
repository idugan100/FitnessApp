package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllNotifications extends AppCompatActivity {

    private ArrayList<Notification> notificationList = new ArrayList<Notification>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loadUserNotifications();
        setContentView(R.layout.activity_all_notifications);
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

    private void loadUserNotifications(){
        String url ="http://18.226.82.203:8080/notifications/"+User.getInstance().getId();
        JsonArrayRequest j = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i =0; i<response.length();i++){
                            try{
                                String name=response.getJSONObject(i).getString("message");
                                boolean read = response.getJSONObject(i).getBoolean("read");
                                int id= response.getJSONObject(i).getInt("id");
                                Notification n = new Notification(name,read,id);
                                notificationList.add(n);
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
        TableLayout t = findViewById(R.id.table);
        for(int i=0; i<notificationList.toArray().length;i++){
            TableRow row = new TableRow(this);

            TextView message = new TextView(this);
            message.setText(notificationList.get(i).message);
            message.setTextSize(15);
            setTextViewMargins(message);
            Button action = new Button(this);


            if(notificationList.get(i).read){
                action.setText("delete");
                int finalI1 = i;
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(notificationList.get(finalI1).id);
                    }
                });
            }else{
                message.setTextColor(Color.BLUE);
                action.setText("read");
                int finalI = i;
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        read(notificationList.get(finalI).id);
                    }
                });
            }

            row.addView(message);

            row.addView(action);

            t.addView(row);
        }
    }
    private void setTextViewMargins(TextView textView) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        int marginInDp = 7; // Margin in dp
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f); // Convert dp to pixels
        params.setMargins(marginInPx, 0, marginInPx, 0);
        textView.setLayoutParams(params);
    }

    private void delete(int id){
        String url = "http://18.226.82.203:8080/notifications/delete/"+User.getInstance().getId()+"/"+id;
       StringRequest s = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        recreate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), " error, please try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(s);
    }

    private void read(int id){

        String url = "http://18.226.82.203:8080/notifications/read/"+User.getInstance().getId()+"/"+id;
        StringRequest s = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        recreate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), " error, please try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(s);

    }

}