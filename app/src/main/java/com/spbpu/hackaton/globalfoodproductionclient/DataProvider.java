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

    static void setDefault(Context context) {
        updateCountries(context);
        updateYears(context);
        updateGraphData(context);
    }

    static void updateCountries(Context context) {
        update(context, URL_GET_ALL_COUNTRIES, "", COUNTRIES);
    }

    static void updateYears(Context context) {
        String params = "?country=" + "Afghanistan";
        update(context, URL_GET_ALL_YEARS, params, YEARS);
    }

    static void updateGraphData(Context context) {
        String params = "?country=" + "Afghanistan";
        update(context, URL_GET_GRAPH, params, GRAPH);
    }

    static private void update(final Context context,
                               String url_request, String params,
                               final int which) {
        //final ResponseCallback responseCallback) {
        final String reqString = URL + url_request + params;
        //Toast.makeText(context, "URL: " + reqString, Toast.LENGTH_SHORT).show();

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                reqString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if (responseCallback != null)
                        String tmp = response;
                        switch (which) {
                            case YEARS: {
                                allYears = tmp.replaceAll("\\[|\\]|\"", "")
                                        .split(",");
                            }
                            break;

                            case COUNTRIES: {
                                allCountries = tmp.replaceAll("\\[\"|\"\\]", "")
                                        .split("\",\"");
                            }
                            break;

                            case PIE: {
                                pieData = response.replaceAll("\\{\"|\"\\}", "")
                                        .split("\",\"");
                            }
                            break;
                            case GRAPH: {
                                graphData = response.replaceAll("\\[\"|\"\\]", "")
                                        .split("\",\"");
                            }
                            break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REQUEST ERROR: ", error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
    }

    static ArrayList<String> getCountries() {
        ArrayList<String> c = new ArrayList<>(/*Arrays.asList("none")*/);
        c.addAll(Arrays.asList(allCountries));
        return c;
    }

    static ArrayList<String> getYears(Context context, String params) {
        //Toast.makeText(context, "Start years search", Toast.LENGTH_SHORT).show();
        update(context, URL_GET_ALL_YEARS, params, YEARS);
        return new ArrayList<String>(Arrays.asList(allYears));
    }

    static ArrayList<Pair<String, Float>> getPieChartData(String country, String year,
                                                          boolean firstTime, Context context) {
        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();

        if (firstTime) {
            chartData.add(new Pair<String, Float>("No data", 1f));
        } else { // TODO: get from server
            String params = "?country=" + country.replaceAll(" ", "_")
                    + "&year=" + year;
            update(context, URL_GET_PIE, params, PIE);

            for (String item : pieData) {
                String str[] = item.split("\":\"");
                chartData.add(new Pair<String, Float>(str[0], Float.valueOf(str[1])));
            }
        }

        return chartData;
    }

    public static ArrayList<String> getGraphData(String country, Context context) {
        String params = "?country=" + country.replaceAll(" ", "_");
        update(context, URL_GET_GRAPH, params, GRAPH);
        return new ArrayList<String>(Arrays.asList(graphData));
    }
}
