package com.example.fitnessapp;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllUsers extends AppCompatActivity {

    private ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getAllUsers();
        setContentView(R.layout.activity_all_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void backToAdmin(View view) {
        Intent i = new Intent(this, AdminPanel.class);
        startActivity(i);
    }

    private void getAllUsers(){
        String url ="http://18.226.82.203:8080/allusers";
        JsonArrayRequest j = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                params.put("Authorization", "Bearer "+ AppUser.getInstance().getToken());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(j);
    }


    private void setUpTable(){
        TableLayout t = findViewById(R.id.table);
        for(int i=0; i<users.toArray().length;i++){
            TableRow row = new TableRow(this);

            TextView name = new TextView(this);
            name.setText(users.get(i).username);
            name.setTextSize(20);
            setTextViewMargins(name);

            row.addView(name);

            t.addView(row);
        }
    }

    private void setTextViewMargins(TextView textView) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        int marginInDp = 10; // Margin in dp
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f); // Convert dp to pixels
        params.setMargins(marginInPx, 0, marginInPx, 0);
        textView.setLayoutParams(params);
    }
}