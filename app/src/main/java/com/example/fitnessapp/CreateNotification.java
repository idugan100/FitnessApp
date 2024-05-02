package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    public void backToAdmin (View view) {
//
//    }

    public void inputNotification (View view) {
        /* NOTIFICATION CONTENT */
        EditText u = findViewById(R.id.notifBody);
        String input = u.getText().toString();

        /* CONSTRUCT JSON */
        JSONObject notification = new JSONObject();
        try {
            notification.put("message", input);
        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }

        /* POST */
        String url = "http://18.226.82.203:8080/notifications";

        StringRequest req = new StringRequest(
                Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
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
                params.put("Authorization", "Bearer "+ AppUser.getInstance().getToken());
                return params;
            }
            public byte[] getBody() {
                // Override this method to send the JSON body
                return notification.toString().getBytes();
            }};
        Volley.newRequestQueue(this).add(req);


    }

    public void backToAdmin(View view) {
        Intent i = new Intent(this, AdminPanel.class);
        startActivity(i);
    }
}