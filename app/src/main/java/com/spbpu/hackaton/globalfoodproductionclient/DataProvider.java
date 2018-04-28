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

    static private final String URL_GET_ALL = "allCountries";
    static private final String URL = "http://10.20.0.95:8090/";

    private static String[] allCountries = {};


    static ArrayList<Pair<String, Float>> getPieChartData(String counry) {
        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();

        // TODO: get from server
        chartData.add(new Pair<String, Float>("Rise", 500f));
        chartData.add(new Pair<String, Float>("Ice cream", 150f));
        chartData.add(new Pair<String, Float>("Lollipop", 350f));
        chartData.add(new Pair<String, Float>("Others", 10f));

        return chartData;
    }

    static void updateCountries(Context context) {
        ArrayList<String> countries = new ArrayList<>();

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                URL + URL_GET_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String tmp = response;
                        allCountries = tmp.replaceAll("\\[|\\]|\"","")
                                .split(",");
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
        return new ArrayList<String>(Arrays.asList(allCountries));
    }

}
