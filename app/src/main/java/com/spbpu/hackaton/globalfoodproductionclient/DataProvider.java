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

public class DataProvider {

    static private final String URL_GET_ALL_COUNTRIES = "allCountries";
    static private final String URL_GET_ALL_YEARS     = "years";
    static private final String URL = "http://10.20.0.95:8090/";
    //static private final String URL = "http://10.20.0.132:8090/";

    private static String[] allCountries = {};
    private static String[] allYears     = {"none"};

    private static final int COUNTRIES = 0;
    private static final int YEARS     = 1;

    static ArrayList<Pair<String, Float>> getPieChartData(String country, boolean firstTime) {
        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();

        if (firstTime) {
            chartData.add(new Pair<String, Float>("No data", 1f));
        } else { // TODO: get from server
            chartData.add(new Pair<String, Float>("One", 1f));
            chartData.add(new Pair<String, Float>("Two", 2f));
            chartData.add(new Pair<String, Float>("Five", 5f));
        }

        return chartData;
    }

    static void updateCountries(Context context) {
        update(context, URL_GET_ALL_COUNTRIES, "", COUNTRIES);
    }

    static private void update(final Context context,
                               String url_request, String params,
                               final int which) {
        final String reqString = URL + url_request + params;
        Toast.makeText(context, "URL: " + reqString, Toast.LENGTH_SHORT).show();

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                reqString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String tmp = response;
                        switch (which) {
                            case YEARS: {
                                allYears = tmp.replaceAll("\\[|\\]|\"", "")
                                        .split(",");
                                Toast.makeText(context, "YEAR GETTED", Toast.LENGTH_SHORT).show();
                            }
                            break;

                            case COUNTRIES: {
                                allCountries = tmp.replaceAll("\\[\"|\"\\]", "")
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
        ArrayList<String> c = new ArrayList<>(Arrays.asList("none"));
        c.addAll(Arrays.asList(allCountries));
        return c;
    }

    static ArrayList<String> getYears(Context context, String params) {
        //Toast.makeText(context, "Start years search", Toast.LENGTH_SHORT).show();
        update(context, URL_GET_ALL_YEARS, params, YEARS);
        return new ArrayList<String>(Arrays.asList(allYears));
    }

}
