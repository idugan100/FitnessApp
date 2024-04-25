package com.example.fitnessapp;

import android.content.Intent;
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

            TextView isRead = new TextView(this);
            if(notificationList.get(i).read){
                isRead.setText("read");
            }else{
                isRead.setText("unread");
            }
            isRead.setTextSize(15);
            setTextViewMargins(isRead);

            row.addView(message);
            row.addView(isRead);

            Button read = new Button(this);
            read.setText("r");
            Button delete = new Button(this);
            delete.setText("d");

            row.addView(read);
            row.addView(delete);

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

}