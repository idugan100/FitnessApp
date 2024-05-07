package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;


public class HeatMapFragment extends Fragment {
    private GridLayout gridLayout;
    private HeatMap heatMap;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heat_map, container, false);

        gridLayout = view.findViewById(R.id.gridLayout2);

        heatMap = new HeatMap();

        populateGrid(view);

        return view;
    }

    private void populateGrid (View view) {
        DateIntensity[][] gridData = heatMap.getGrid();

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

    private int getColorForIntensity (Intensity intensity) {
        switch (intensity) {
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
