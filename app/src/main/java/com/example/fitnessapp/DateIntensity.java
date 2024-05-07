package com.example.fitnessapp;

public class DateIntensity {
    Intensity i;
    String date;

    public DateIntensity (String date, Intensity i) {
        this.date = date;
        this.i = i;
    }

    public Intensity getIntensity () {
        return i;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Intensity: " + i;
    }
}
