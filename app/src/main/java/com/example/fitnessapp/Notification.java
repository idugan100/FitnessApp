package com.example.fitnessapp;

public class Notification {
    public String message;
    public boolean read;

    public final int id;
    Notification(String message, boolean read, int id){
        this.message=message;
        this.read=read;
        this.id=id;
    }
}
