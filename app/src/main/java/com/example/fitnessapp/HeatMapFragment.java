package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import android.util.TypedValue;
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

    // Factory
    public static HeatMapFragment newInstance(boolean isGroup) {
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
        gridLayout = view.findViewById(R.id.gridLayout2);

        boolean isGroup = false;
        if (getArguments() != null) {
            isGroup = getArguments().getBoolean("isGroup", false);
        }

        /**
         * Creates a new HeatMap instance and passes the fragment as the callback.
         * The HeatMap class will fetch the data and notify the fragement when it's ready
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
        // Formatters for pulling date out of DateIntensity objects from the grid
        // and converting it to something more readable for the user
        DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("MM/dd");

        for (int i = 0; i < gridData.length; i++) {
            for (int j = 0; j < gridData[i].length; j++) {
                TextView tv = new TextView(getContext());
                String dateStr = gridData[i][j].getDate();
                try {
                    LocalDate date = LocalDate.parse(dateStr, inFormatter);
                    String formattedDate = date.format(outFormatter);
                    tv.setText(formattedDate);
                } catch (DateTimeParseException e) {
                    Log.e("ERROR", e.toString());
                    String errorMsg = "null";
                    tv.setText(errorMsg);
                }

                // Get background color based on Intensity and set the text bg to it
                int bgColor = getBackgroundColorForIntensity(gridData[i][j].getIntensity());
                tv.setBackground(createBorderDrawable(bgColor));

                // Center text
                tv.setGravity(Gravity.CENTER);

                // Set text size
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                // Set text to italic bold
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);

                // Get text color based on Intensity and set text color to it (contrasting color)
                int textColor = getTextColor(gridData[i][j].getIntensity());
                tv.setTextColor(textColor);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 1;
                params.height = 1;
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                if (i != 3 && j == 0) {
                    params.setMargins(1, 1, 1, 0);
                } else if (i != 3 && j > 0) {
                    params.setMargins(1, 1, 1, 1);
                } else if (i == 0 && j == 0) {
                    params.setMargins(2, 2, 0, 0);
                } else {
                    params.setMargins(1, 0, 1, 1);
                }

                gridLayout.addView(tv, params);
            }
        }
    }

    // Helper method to return corresponding color for the intensity
    private int getBackgroundColorForIntensity(Intensity i) {
        switch (i) {
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

    private int getTextColor(Intensity i) {
        switch (i) {
            case NONE:
            case LOW:
            case MODERATE:
                return Color.GRAY;
            case HIGH:
            case EXTREME:
                return Color.WHITE;
            default:
                return Color.RED; // if default an error occurred so text made red
        }
    }

    private Drawable createBorderDrawable(int bgColor) {
        // Add grid lines
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.BLACK);
        border.getPaint().setStyle(Paint.Style.STROKE);
        border.getPaint().setStrokeWidth(1.5F);

        // Add background color
        ShapeDrawable bg = new ShapeDrawable(new RectShape());
        bg.getPaint().setColor(bgColor);

        Drawable[] layers = {bg, border};
        return new LayerDrawable(layers);
    }

}
