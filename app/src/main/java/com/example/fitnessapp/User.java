package com.example.fitnessapp;

public class User {

    public int id;
    public String username;
    public boolean isAdmin;

    User(int id, String username, Boolean isAdmin){
        this.id=id;
        this.username=username;
        this.isAdmin=isAdmin;
    }
}
