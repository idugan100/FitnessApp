package com.example.fitnessapp;

import android.util.Base64;

import org.json.JSONObject;

public class User {
    private String token;
    private int id;
    private boolean isAdmin;

    static private User instance;

    private User(String token){
        this.token=token;
        //only decode the body
        byte[] decodedBytes = Base64.decode(token.split("\\.")[1], Base64.DEFAULT);

        String decodedString = new String(decodedBytes);

        try{
            JSONObject user = new JSONObject(decodedString);
            this.id = Integer.parseInt(user.get("userID").toString());

            this.isAdmin = Boolean.parseBoolean(user.get("isAdmin").toString());

        }catch (Exception ignored){}
    }

    public static User getInstance() {
        if (instance == null) {
            throw new IllegalStateException("User instance has not been initialized with a token.");
        }
        return instance;
    }

    public static User initialize(String token) {
        if (instance == null) {
            instance = new User(token);
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    // Getter for token
    public String getToken() {
        return token;
    }

}
