package com.example.fitnessapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HeatMapActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private HeatMap heatMap;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_heat_map);

        gridLayout = findViewById(R.id.gridLayout);
        heatMap = new HeatMap();

        ViewCompat.setOnApplyWindowInsetsListener(gridLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        populateGrid();
    }

    private void populateGrid () {
        DateIntensity[][] gridData = heatMap.getGrid();

        for (int i = 0; i < gridData.length; i++) {
            for (int j = 0; j < gridData[i].length; j++) {
                TextView textView = new TextView(this);
                textView.setText(gridData[i][j].toString());
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(getColorForIntensity(gridData[i][j].getIntensity()));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                gridLayout.addView(textView, params);
            }
        }
    }

    private int getColorForIntensity(Intensity intensity) {
        switch (intensity) {
            case LOW:
                return getColor(R.color.colorLow);
            case MODERATE:
                return getColor(R.color.colorModerate);
            case HIGH:
                return getColor(R.color.colorHigh);
            case EXTREME:
                return getColor(R.color.colorExtreme);
            default:
                return Color.RED;
        }
    }
}