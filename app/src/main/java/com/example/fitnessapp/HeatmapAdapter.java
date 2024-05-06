package com.example.fitnessapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.graphics.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeatmapAdapter extends BaseAdapter {
    private final Context context;
    private final List<DateIntensity> dateIntensities = new ArrayList<>();
    private final LayoutInflater inflater;

    public HeatmapAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        initializeData();  // Initialize the data when the adapter is created
    }

    private void initializeData() {
        LocalDate startDate = LocalDate.now().minusDays(27);  // Start date 28 days ago
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        Random rand = new Random();

        for (int i = 0; i < 28; i++) {
            LocalDate date = startDate.plusDays(i);
            Intensity intensity = Intensity.values()[rand.nextInt(Intensity.values().length)];
            dateIntensities.add(new DateIntensity(date.format(formatter), intensity));
        }
    }

    @Override
    public int getCount() {
        return dateIntensities.size();
    }

    @Override
    public Object getItem(int position) {
        return dateIntensities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_cell, parent, false);
        }

        DateIntensity item = dateIntensities.get(position);
        View colorIndicator = convertView.findViewById(R.id.colorIndicator);
        colorIndicator.setBackgroundColor(getColorForIntensity(item.getIntensity()));

        return convertView;
    }

    private int getColorForIntensity(Intensity intensity) {
        switch (intensity) {
            case LOW:
                return Color.parseColor("#CCFFCC");
            case MODERATE:
                return Color.parseColor("#99FF99");
            case HIGH:
                return Color.parseColor("#66FF66");
            case EXTREME:
                return Color.parseColor("#33CC33");
            default:
                return Color.TRANSPARENT;
        }
    }
}