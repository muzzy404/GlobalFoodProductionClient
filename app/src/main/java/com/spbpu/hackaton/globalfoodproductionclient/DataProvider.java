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
    static private final String URL_GET_ALL_YEARS     = "years";
    static private final String URL_GET_PIE           = "pie";
    static private final String URL = "http://10.20.0.95:8090/";
    //static private final String URL = "http://10.20.0.132:8090/";

    private static String[] allCountries = {};
    //Nika's code
    private static String[] allYears     = {};
    //end N
    private static String[] pieData;
    private static Map<String, String> pieDataMap;// = new HashMap<>();

    private static final int COUNTRIES = 0;
    private static final int YEARS     = 1;
    private static final int PIE       = 2;

    static ArrayList<Pair<String, Float>> getPieChartData(String country, String year,
                                                          boolean firstTime, Context context) {
        ArrayList<Pair<String, Float>> chartData = new ArrayList<>();

        Log.d("<DASHA>", "getPieChartData call");
        if (firstTime) {
            chartData.add(new Pair<String, Float>("No data", 1f));
            Log.d("<DASHA>", "bublik bardo");
        } else { // TODO: get from server
            String params = "?country=" + country + "&year=" + year;
            //Map<String, String> pieDataMap =
            //getPieDataMap(context, params);
            update(context, URL_GET_PIE, params, PIE);

            //
            if (pieData == null) {
                return chartData;
            }

            for(String item : pieData){
                String str[] = item.split("\":\"");
                chartData.add(new Pair<String, Float>(str[0],Float.valueOf(str[1])));
                Log.d("<NIKA>", str[0] + " " + str[1]);
                //pieDataMap.put(str[0], str[1]);
            }

            //

            /*for(Map.Entry<String, String> entry : pieDataMap.entrySet()) {
                Log.d("<DASHA>", entry.getKey() + " = " + entry.getValue());
                chartData.add(new Pair<String, Float>(entry.getKey(),
                        Float.valueOf(entry.getValue())));
            }*/

            Log.d("<DASHA>", String.valueOf(pieDataMap));

            //chartData.add(new Pair<String, Float>("One", 1f));
            //chartData.add(new Pair<String, Float>("Two", 2f));
            //chartData.add(new Pair<String, Float>("Five", 5f));
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
        //Toast.makeText(context, "URL: " + reqString, Toast.LENGTH_SHORT).show();

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

    static Map<String, String> getPieDataMap(Context context, String params) {
        pieDataMap = new HashMap<>();

        StringRequest getRequest = new StringRequest(
                Request.Method.GET,
                URL + URL_GET_PIE + params, //"?country=Angola&year=1900",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<N> : ", "request");
                        pieData = response.replaceAll("\\{\"|\"\\}", "")
                                .split("\",\"");
//                        for(String item : pieData){
//                            String str[] = item.split("\":\"");
//                            pieDataMap.put(str[0], str[1]);
//                        }
//                        Log.d("<NIKA>", String.valueOf(pieDataMap));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //txtMessage.setText("ERROR: " + error.toString());
                        Log.d("<NIKA>", "error");
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(getRequest);

        return pieDataMap;
    }

}
