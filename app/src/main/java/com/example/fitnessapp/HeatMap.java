package com.example.fitnessapp;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.Lambda;

/**
 * HeatMap class responsible for fetching and processing activity data
 * Known Issues:
 *      - Currently does not work with duplicate days: If you have activities on the same day
 *        it's not weighing them all together to determine the intensity.
 *      - The grid goes in order of the fetches, it doesn't place them on the correct box for the day.
 *        In simple terms if there's two entries one for 4/10/2024 and one for 4/25/2024 they
 *        show up directly next to one another, not in the correct box for the date.
 *
 *      I will circle back around to fix both these issues once I've implemented the other stats.
 *
 * How it's working:
 *      Volley fetches are asynchronous by default. I made a callback interface in HeatMap() and a
 *      callback implementation in HeatMapFragment(). When HeatMapFragment() creates this HeatMap object
 *      it passes the context and itself as the callback.
 *
 *      In the try block after a successful fetch it parses the response and then initializes the heatmap.
 *      Once the initialization method has completed it checks that callback isn't null and if it isn't it
 *      makes the callback to the onDataReady() method from HeatMapFragment.
 *
 *      The onDataReady() method from HeatMapFragment then calls populate grid. This way it only populates t
 *      he grid with data once the callback has happened signifying the data is ready.
 */
public class HeatMap {

    // Callback interface. Notifies the Observer when the data is ready.
    interface DataReadyCallback {
        void onDataReady(DateIntensity[][] grid);
    }

    private Context context;
    private DataReadyCallback callback;
    private DateIntensity[][] grid;
    private static final int ROWS = 4;
    private static final int COLS = 7;
    private ArrayList<DailyTotal> totalList = new ArrayList<>();
    boolean isGroup = false;

    /**
     * Constructor to create a new HeatMap object
     * @param context -> Context of the app
     * @param callback -> Callback to notifiy data is ready
     */
    public HeatMap(Context context, DataReadyCallback callback, boolean isGroup) {
        this.context = context;
        this.callback = callback;
        this.isGroup = isGroup;
        fetchData(this.isGroup);
    }

    // Fetches data with Volley
    private void fetchData(boolean isGroup) {
        String url = "";
        if (isGroup) {
            url = "http://18.226.82.203:8080/activities/stats/all";
        } else {
            url = "http://18.226.82.203:8080/activities/stats/" + AppUser.getInstance().getId();
        }

        /**
         * Found this Lambda syntax online. Basically just inline logic, thought it looked a little
         * cleaner and more readable for me.
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        // Parse response and extract data
                        parseResponse(response);

                        // initialize heat map once data has been extracted
                        initializeHeatMap();

                        // Ensure a callback was provided and then make the callback
                        if (callback != null) {
                            callback.onDataReady(grid);
                        }
                    } catch (Exception e) {
                        Log.d("debug", "Error parsing response: " + e.getMessage());
                    }
                },
                error -> Log.d("debug", "Error fetching data: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + AppUser.getInstance().getToken());
                return params;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    /**
     * Parses the JSON response and extracts the data into daily total objects.
     * @param response -> JSON response
     * @throws Exception -> If error occurs while parsing
     */
    private void parseResponse(JSONObject response) throws Exception {
        JSONArray days = response.getJSONArray("dailyTotals");
        for (int i = 0; i < days.length(); i++) {
            JSONObject dayObject = days.getJSONObject(i);
            String dateString = dayObject.getString("date");
            int minutes = dayObject.getInt("totalMinutes");
            totalList.add(new DailyTotal(dateString, minutes));
        }
        Log.d("debug", "TOTAL ACTIVITIES FETCHED: " + totalList.size());
    }

    // Initializes heatmap grid with DateIntensity objects
    private void initializeHeatMap() {
        // Plot the grid with dates from current date back 28 days
        LocalDate startDate = LocalDate.now().minusDays(27);
        this.grid = new DateIntensity[ROWS][COLS];
        Map<LocalDate, DailyTotal> dateMap = new HashMap<>();

        // Populate dateMap Map
        for (DailyTotal total : totalList) {
            LocalDate date = LocalDate.parse(total.day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateMap.put(date, total);
        }

        // Populate grid based on dateMap Map
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Need to access the right day in startDate for the 2d grid location
                // i = 1 j = 1 gives grid[1][1] which would be the 9th day on the grid
                // COLS = 7 for 7 days per week.
                // i * COLS * j => 1 * 7 + 1 = 8
                // startDate(8) is the 9th element of the array which = 9th day
                LocalDate currentDate = startDate.plusDays(i * COLS + j);

                if (dateMap.containsKey(currentDate)) {
                    DailyTotal total = dateMap.get(currentDate);
                    Intensity intensity = setIntensity(total.minutes);
                    grid[i][j] = new DateIntensity(currentDate.toString(), intensity);
                    continue;
                }
                grid[i][j] = new DateIntensity(currentDate.toString(), Intensity.NONE);
            }
        }
    }

    /**
     * Determines intensity level based on number of minutes.
     *
     * TODO: Weigh the entire daily totals from all activities that day.
     * TODO: Investigate the issue. The server only responded with 3 objects when Isaac had 5,
     * TODO: so it's possible it isn't returning them all?
     *
     * @param minutes -> Number of minutes
     * @return -> Corresponding Intensity enum for the minutes
     */
    private Intensity setIntensity(int minutes) {
        if (minutes < 30) return Intensity.LOW;
        else if (minutes < 60) return Intensity.MODERATE;
        else if (minutes < 120) return Intensity.HIGH;
        else return Intensity.EXTREME;
    }
}



