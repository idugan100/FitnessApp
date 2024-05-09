package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* DATA FETCHING VERSION */


/**
 * Observer pattern.
 * HeatMapFragment = Observer
 * HeatMap = Subject
 *
 * HeatMapFragment responsible for displaying the heatmap grid to the view
 * and populating it with data received from HeatMap().
 *
 * Implements HeatMap.DataReadyCallback interface to ensure the data is received
 * asynchronously and not accessed before it exists.
 */
public class HeatMapFragment extends Fragment implements HeatMap.DataReadyCallback {
    private GridLayout gridLayout;
    private HeatMap heatMap;

    public static HeatMapFragment newInstance (boolean isGroup) {
        HeatMapFragment fragment = new HeatMapFragment();
        Bundle args = new Bundle();
        args.putBoolean("isGroup", isGroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heat_map, container, false);

        boolean isGroup = false;
        if (getArguments() != null) {
            isGroup = getArguments().getBoolean("isGroup", false);
        }

        gridLayout = view.findViewById(R.id.gridLayout2);
        /**
         * Creates a new HeatMap instance and passes the fragment as the callback.
         * The HeatMap class will fetch the data and notify the fragment when it's ready
         * to be consumed.
         */
        heatMap = new HeatMap(getContext(), this, isGroup);
        return view;
    }

    // Callback that's called when the data is received and deemed ready to consume
    @Override
    public void onDataReady(DateIntensity[][] gridData) {
        populateGrid(gridData);
    }

    // Populates grid with the DateIntensity data
    private void populateGrid(DateIntensity[][] gridData) {
        for (int i = 0; i < gridData.length; i++) {
            for (int j = 0; j < gridData[i].length; j++) {
                TextView tv = new TextView(getContext());
                tv.setText(gridData[i][j].toString());
                tv.setBackgroundColor(getColorForIntensity(gridData[i][j].getIntensity()));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                gridLayout.addView(tv, params);
            }
        }
    }

    // Helper method to return corresponding color for the intensity
    private int getColorForIntensity(Intensity intensity) {
        switch (intensity) {
            case NONE:
                return getResources().getColor(R.color.colorNone);
            case LOW:
                return getResources().getColor(R.color.colorLow);
            case MODERATE:
                return getResources().getColor(R.color.colorModerate);
            case HIGH:
                return getResources().getColor(R.color.colorHigh);
            case EXTREME:
                return getResources().getColor(R.color.colorExtreme);
            default:
                return Color.RED;
        }
    }
}
