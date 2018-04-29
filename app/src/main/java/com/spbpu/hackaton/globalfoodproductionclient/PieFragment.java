package com.spbpu.hackaton.globalfoodproductionclient;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private PieChart pieChart;

    private Spinner countrySpinner;
    private Spinner yearsSpinner;

    ArrayAdapter<String> countriesAdapter;
    ArrayAdapter<String> yearsAdapter;

    private final static String NULL_STRING = "none";

    private String selectedCountry = "Select country and year";
    private String selectedYear = NULL_STRING;

    public PieFragment() {
        // Required empty public constructor
    }

    public static PieFragment newInstance(String param1, String param2) {
        PieFragment fragment = new PieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateChart(boolean first) {
        // TODO: get from spinner
        Log.d("Dasha", "update chart");
        String country = selectedCountry;

        ArrayList<Pair<String, Float>> dataRaw = DataProvider.getPieChartData(country, first);

        ArrayList<PieEntry> entries = new ArrayList<>();

        int i = 0;
        for(Pair<String, Float> item : dataRaw) {
            entries.add(new PieEntry(item.second, item.first));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        //colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);
        data.setValueTextColor(Color.WHITE);

        Log.d("Dasha", (pieChart == null ? "NULL" : "OK"));
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(country);
        pieChart.setCenterTextColor(Color.BLACK);

        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.animateY(1_000, Easing.EasingOption.EaseInOutCubic);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void updateCountries() {
        countriesAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                DataProvider.getCountries());
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countriesAdapter);
    }

    private void updateYears() {
        yearsAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                DataProvider.getYears(getContext(), "?country=" + selectedCountry));
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(yearsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie, container, false);

        pieChart = view.findViewById(R.id.pie_chart_view);
        //pieChart.clear();
        //updateChart(true);

        countrySpinner = view.findViewById(R.id.countries_spinner_pie);
        yearsSpinner = view.findViewById(R.id.years_spinner_pie);

        updateCountries();

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = adapterView.getItemAtPosition(i).toString();
                updateYears();
                Toast.makeText(getContext(), adapterView.getItemAtPosition(i).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = adapterView.getItemAtPosition(i).toString();
                if (!selectedCountry.equals(NULL_STRING) ||
                        !selectedYear.equals(NULL_STRING) ||
                        !selectedYear.equals(""))
                    updateChart(false);
                // Nika's code
                if (selectedYear.equals(NULL_STRING) || selectedCountry.equals(NULL_STRING))
                    pieChart.clear();

                // end Nika's code
                Toast.makeText(getContext(), selectedCountry + " " + selectedYear,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
            //Toast.makeText(context, "Pie fragment on attach", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
