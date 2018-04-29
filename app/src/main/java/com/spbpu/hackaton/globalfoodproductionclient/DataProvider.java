package com.spbpu.hackaton.globalfoodproductionclient;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DataProvider {

    static private final String URL_GET_ALL_COUNTRIES = "allCountries";
    static private final String URL_GET_ALL_YEARS = "years";
    static private final String URL_GET_PIE = "pie";
    static private final String URL_GET_GRAPH = "graph";
    static private final String URL = "http://10.20.0.95:8090/";
    //static private final String URL = "http://10.20.0.132:8090/";

    private static String[] allCountries = {};
    private static String[] allYears = {};
    private static String[] pieData = {};
    private static String[] graphData = {};

    private static final int COUNTRIES = 0;
    private static final int YEARS = 1;
    private static final int PIE = 2;
    private static final int GRAPH = 3;
    private static final String DEFAULT_COUNTRY = "?country=" + "Afghanistan";
    private static final String DEFAULT_YEAR = "&year=" + "1961";

    static void setDefault(Context context) {
        updateCountries(context);
        updateYears(context);
        updateGraphData(context);
        updatePieData(context);
    }

    static void updateCountries(Context context) {
        update(context, URL_GET_ALL_COUNTRIES, "", COUNTRIES);
    }

    static void updateYears(Context context) {
        String params = DEFAULT_COUNTRY;
        update(context, URL_GET_ALL_YEARS, params, YEARS);
    }

    static void updateGraphData(Context context) {
        String params = DEFAULT_COUNTRY;
        update(context, URL_GET_GRAPH, params, GRAPH);
    }

    static void updatePieData(Context context) {
        String params = DEFAULT_COUNTRY + DEFAULT_YEAR;
        update(context, URL_GET_PIE, params, PIE);
    }

    static private void update(final Context context,
                               String url_request, String params,
                               final int which) {

        final String myUrl = URL + url_request + params;
        String result = "";
        HttpGetRequest getRequest_ = new HttpGetRequest();

        try {
            result = getRequest_.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        switch (which) {
            case YEARS: {
                allYears = result.replaceAll("\\[|\\]|\"", "")
                        .split(",");
            }
            break;

            case COUNTRIES: {
                allCountries = result.replaceAll("\\[\"|\"\\]", "")
                        .split("\",\"");
            }
            break;

            case PIE: {
                Log.d("<ALENA> : ", result);
                pieData = result.replaceAll("\\{\"|\"\\}", "")
                        .split("\",\"");
                Log.d("<ALENA> : ", pieData[0]);
            }
            break;
            case GRAPH: {
                graphData = result.replaceAll("\\[\"|\"\\]", "")
                        .split("\",\"");
            }
            break;
        }
    }

    static ArrayList<String> getCountries() {
        ArrayList<String> c = new ArrayList<>();
        c.addAll(Arrays.asList(allCountries));
        return c;
    }

    static ArrayList<String> getYears(Context context, String params) {
        update(context, URL_GET_ALL_YEARS, params, YEARS);
        return new ArrayList<String>(Arrays.asList(allYears));
    }

    static ArrayList<Pair<String, Float>> getPieChartData(String country, String year, Context context) {

        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();
        String params = "?country=" + country.replaceAll(" ", "_") + "&year=" + year;
        update(context, URL_GET_PIE, params, PIE);

        Log.d("<---ALENA---> : ", "");
        for (String item : pieData) {
            String str[] = item.split("\":\"");
            chartData.add(new Pair<String, Float>(str[0], Float.valueOf(str[1])));
        }
        return chartData;
    }

    public static ArrayList<String> getGraphData(String country, Context context) {
        String params = "?country=" + country.replaceAll(" ", "_");
        update(context, URL_GET_GRAPH, params, GRAPH);
        return new ArrayList<String>(Arrays.asList(graphData));
    }
}
