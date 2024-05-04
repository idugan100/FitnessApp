package com.example.fitnessapp;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import java.util.HashMap;
import java.util.Map;

public class AllUsers extends AppCompatActivity {

    private ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //function to dynamically load users from api
        getAllUsers();

        setContentView(R.layout.activity_all_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //takes you back to admin panel on back button click
    public void backToAdmin(View view) {
        Intent i = new Intent(this, AdminPanel.class);
        startActivity(i);
    }

    private void getAllUsers(){
        //API request to get all the users
        String url ="http://18.226.82.203:8080/allusers";
        JsonArrayRequest j = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //parse the json response into an array of user objects
                        for(int i =0; i<response.length();i++){
                            try{
                                String username=response.getJSONObject(i).getString("username");
                                boolean isAdmin = response.getJSONObject(i).getBoolean("isAdmin");
                                int id= response.getJSONObject(i).getInt("id");
                                User u = new User(id, username,isAdmin);
                                users.add(u);
                            }catch(Exception e){
                                Log.d("dubug",e.getMessage());
                            }
                        }
                        //take users and create the table in view
                        setUpTable();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("msg","not recieved: "+ error.getMessage());
                    }
                }
        ){
            //adding auth token for authentication
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ AppUser.getInstance().getToken());
                return params;
            }
        };
        //executing api call
        Volley.newRequestQueue(this).add(j);
    }


    private void setUpTable(){
        //get table layout
        TableLayout t = findViewById(R.id.table);

        //for each user make a row
        for(int i=0; i<users.toArray().length;i++){
            TableRow row = new TableRow(this);

            //add user name field
            TextView name = new TextView(this);
            name.setText(users.get(i).username);
            name.setTextSize(20);
            setTextViewMargins(name);

            //add button
            Button b = new Button(this);

            Context c = this;
            int finalI = i;
            // add button click handler to setup stats popup
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //API request for user stats
                    String url ="http://18.226.82.203:8080/activities/stats/"+users.get(finalI).id;
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
                                    //create a popup with the stats
                                    new AlertDialog.Builder(c)
                                            .setTitle("Stats for " + users.get(finalI).username)
                                            .setMessage(stats.getDisplay())
                                            .setPositiveButton(android.R.string.yes, null)
                                            .show();
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
                    Volley.newRequestQueue(c).add(j);
                }
            });

            b.setText("->");

            //add data to rows
            row.addView(name);
            row.addView(b);
            t.addView(row);
        }
    }

    // I couldn't figure out how to add margin to layouts from the activity class and this is what GPT spit out lol
    private void setTextViewMargins(TextView textView) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        int marginInDp = 10; // Margin in dp
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f); // Convert dp to pixels
        params.setMargins(marginInPx, 0, marginInPx, 0);
        textView.setLayoutParams(params);
    }
}