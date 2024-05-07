package com.example.fitnessapp;

import java.util.ArrayList;

public class UserStats {
    public int totalDays=0;
    public int totalHighMinutes=0;
    public int totalMediumMinutes=0;
    public int totalLowMinutes=0;
    public ArrayList<DailyTotal> dailyTotals=new ArrayList<DailyTotal>();

    public String getDisplay(){
        double totalMinutes=totalLowMinutes+totalMediumMinutes+totalHighMinutes;
        String first = dailyTotals.isEmpty() ?"none":dailyTotals.get(0).day;
        String last = dailyTotals.isEmpty() ?"none":dailyTotals.get(dailyTotals.size()-1).day;
        return
                "First Workout: "+ first+
                        "\nMost Recent Workout: "+last+
                        "\nTotal Days Exercised: "+totalDays+
                        "\nTotal Minutes Exercised: "+totalMinutes+
                        "\n Percentage High Intensity: "+String.format("%.1f", pctHigh())+ "%" +
                        "\n Percentage Medium Intensity: "+String.format("%.1f", pctMed())+"%" +
                        "\n Percentage Low Intensity: "+String.format("%.1f", pctLow())+"%" ;
    }

    double pctHigh(){
        double high = totalHighMinutes;
        double medium = totalMediumMinutes;
        double low = totalLowMinutes;
        return (high/(high+low+medium+.001))*100.0;
    }

    double pctMed(){
        double high = totalHighMinutes;
        double medium = totalMediumMinutes;
        double low = totalLowMinutes;
        return (medium/(high+low+medium+.001))*100.0;
    }

    double pctLow(){
        double high = totalHighMinutes;
        double medium = totalMediumMinutes;
        double low = totalLowMinutes;
        return (low/(high+low+medium+.001))*100.0;
    }
}
