package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
public class EstadisticasRegionales extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private JSONArray result;

    private ArrayList<String> regions;
    private ArrayList<BarEntry> data;

    private Spinner spinnerRegions;

    private TextView tvRegionName;
    private TextView tvRegionConfirmed;
    private TextView tvRegionDeaths;
    private TextView tvRegionRecovered;
    private TextView tvRegionActive;

    private BarChart barChart;
    private BarDataSet barDataSet;

    public EstadisticasRegionales() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initUI();
        CallWebServices();
    }

    private void initUI(){
        regions = new ArrayList<>();

        spinnerRegions = findViewById(R.id.spinnerRegiones);
        spinnerRegions.setOnItemSelectedListener(this);

        tvRegionName = findViewById(R.id.tv_RegionName);
        tvRegionConfirmed = findViewById(R.id.tv_RegionConfirmed);
        tvRegionDeaths = findViewById(R.id.tv_RegionDeaths);
        tvRegionRecovered = findViewById(R.id.tv_RegionRecovered);
        tvRegionActive = findViewById(R.id.tv_RegionActive);

        barChart = findViewById(R.id.barChart);
    }

    public void onClick(View view) {
        finish();
    }

    private void CallWebServices(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Regiones.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray array = null;
                        try {
                            array = new JSONArray(response);
                            getRegion(array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EstadisticasRegionales.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void getRegion(JSONArray jsonArray){
        result = new JSONArray();
        for (int i = 0 ; i < jsonArray.length() ; i++){
            try {
                JSONObject region = jsonArray.getJSONObject(i);
                result.put(region);
                regions.add(region.getString(Regiones.PROVINCE_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinnerRegions.setAdapter(new ArrayAdapter<String>(
                EstadisticasRegionales.this,
                android.R.layout.simple_spinner_dropdown_item,
                regions
        ));
    }

    private String getRegionName(int position){
        String name = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            name = jsonObject.getString(Regiones.PROVINCE_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getTotalConfirmed(int position){
        String confirmed = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            confirmed = jsonObject.getString(Regiones.CONFIRMED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return confirmed;
    }

    private String getTotalDeaths(int position){
        String death = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            death = jsonObject.getString(Regiones.DEATHS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return death;
    }

    private String getTotalRecovered(int position){
        String recovered = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            recovered = jsonObject.getString(Regiones.RECOVERED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recovered;
    }

    private String getTotalActive(int position){
        String active = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            active = jsonObject.getString(Regiones.ACTIVE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return active;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        tvRegionName.setText(getRegionName(position));
        tvRegionConfirmed.setText(getTotalConfirmed(position));
        tvRegionDeaths.setText(getTotalDeaths(position));
        tvRegionRecovered.setText(getTotalRecovered(position));
        tvRegionActive.setText(getTotalActive(position));

        data = new ArrayList<>();
        data.add(new BarEntry(2, Float.parseFloat(getTotalConfirmed(position))));
        data.add(new BarEntry(4, Float.parseFloat(getTotalDeaths(position))));
        data.add(new BarEntry(6, Float.parseFloat(getTotalRecovered(position))));
        data.add(new BarEntry(8, Float.parseFloat(getTotalActive(position))));

        barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(16f);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);

       barChart.setFitBars(true);
       barChart.setData(barData);
       barChart.getDescription().setText("Estadisticas por region");
       barChart.animateY(2000);
       barChart.notifyDataSetChanged();
       barChart.invalidate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}