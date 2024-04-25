package com.example.fitnessapp;

import java.util.Date;

public class ExerciseActivity {
    public String name;
    public int duration;
    public String day;
    public String intensity;

    ExerciseActivity(String name, int duration, String day, String intensity){
        this.name=name;
        this.duration=duration;
        this.day=day;
        this.intensity=intensity;
    }
}
