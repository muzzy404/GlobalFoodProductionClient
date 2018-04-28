package com.spbpu.hackaton.globalfoodproductionclient;

import android.util.Pair;

import java.util.ArrayList;

public class DataProvider {

    static ArrayList<Pair<String, Float>> getPieChartData(String counry) {
        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();

        // TODO: get from server
        chartData.add(new Pair<String, Float>("Rise", 500f));
        chartData.add(new Pair<String, Float>("Ice cream", 150f));
        chartData.add(new Pair<String, Float>("Lollipop", 350f));
        chartData.add(new Pair<String, Float>("Others", 10f));

        return chartData;
    }

}
