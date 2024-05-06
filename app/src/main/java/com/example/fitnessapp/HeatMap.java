package com.example.fitnessapp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class HeatMap {
    private List<DateIntensity> heatmapData = new ArrayList<>();
    private static final int DAYS = 28; // Assuming a month view

    public HeatMap() {
        initializeHeatMap();
    }

    private void initializeHeatMap() {
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        Random rand = new Random();

        for (int i = 0; i < DAYS; i++) {
            LocalDate date = startDate.plusDays(i);
            Intensity intensity = Intensity.values()[rand.nextInt(Intensity.values().length)];
            heatmapData.add(new DateIntensity(date.format(formatter), intensity));
        }
    }

    public List<DateIntensity> getHeatmapData() {
        return heatmapData;
    }
}

//public class HeatMap {
//    DateIntensity [][] grid;
//    private static final int ROWS = 7;
//    private static final int COLS = 4;
//
//    ArrayList<LocalDate> dates = new ArrayList<>();
//    public void addDates() {
//        LocalDate startDate = LocalDate.of(2024, 3, 1);
//        for (int i = 0; i < 28; i++) {
//            dates.add(startDate.plusDays(i));
//        }
//    }
//
//    public HeatMap () {
//        this.grid = new DateIntensity[ROWS][COLS];
//        this.addDates();
//        this.initializeHeatMap();
//    }
//
//    public void initializeHeatMap () {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
//        Random rand = new Random();
//
//        int index = 0; // Initialize an index for the dates ArrayList
//        for (int i = 0; i < ROWS; i++) {
//            for (int j = 0; j < COLS; j++) {
//                LocalDate date = dates.get(index); // Use the index to get the date
//                Intensity intensity = Intensity.values()[rand.nextInt(Intensity.values().length)];
//                grid[i][j] = new DateIntensity(date.format(formatter), intensity);
//                index++; // Increment the index for the next date
//            }
//        }
//    }
//
//    public void displayHeatMap () {
//        for (int i = 0; i < ROWS; i++) {
//            for (int j = 0; j < COLS; j++) {
//                System.out.println("Element [" + i + "][" + j + "]: " + grid[i][j]);
//            }
//        }
//    }
//
//    public static void main (String [] args) {
//        HeatMap heatMap = new HeatMap();
//        heatMap.displayHeatMap();
//    }
//}
