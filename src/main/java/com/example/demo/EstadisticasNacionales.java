package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class EstadisticasNacionales extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Declaring an Spinner
    private Spinner spinnerCountries;

    //An ArrayList for Spinner Items
    private ArrayList<String> countries;
    private ArrayList<BarEntry> data;

    //JSON Array
    private JSONArray result;

    //TextView to display details
    private TextView tvTotalConfirmed;
    private TextView tvTotalDeaths;
    private TextView tvTotalRecovered;

    private BarChart barChart;
    private BarDataSet barDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initUI();
        CallWebServices();
    }

     public void onClick(View view) {
        finish();
    }

    private void CallWebServices(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Paises.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject array = null;
                        try {
                            array = new JSONObject(response);
                            result = array.getJSONArray(Paises.JSONArray);
                            getCountries(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EstadisticasNacionales.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void initUI(){
        countries = new ArrayList<>();

        spinnerCountries = findViewById(R.id.spinnerCountries);
        spinnerCountries.setOnItemSelectedListener(this);

        tvTotalConfirmed =  findViewById(R.id.tv_ShowTotalConfirmed);
        tvTotalDeaths = findViewById(R.id.tv_ShowTotalDeaths);
        tvTotalRecovered = findViewById(R.id.tv_ShowTotalRecovered);

        barChart = findViewById(R.id.barChart);
    }

    private void getCountries(JSONArray jsonArray){
        for(int i = 0 ; i < jsonArray.length() ; i++){
            try {
                JSONObject pais = jsonArray.getJSONObject(i);
                countries.add(pais.getString(Paises.COUNTRY_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinnerCountries.setAdapter(new ArrayAdapter<String>(
                EstadisticasNacionales.this,
                android.R.layout.simple_spinner_dropdown_item,
                countries
        ));
    }

    private String getCountryName(int position){
        String nombre = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            nombre = jsonObject.getString(Paises.COUNTRY_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nombre;
    }

    private String getTotalConfirmed(int position){
        String totalConfirmed = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            totalConfirmed = jsonObject.getString(Paises.TOTAL_CONFIRMED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalConfirmed;
    }

    private String getTotalDeaths(int position){
        String totalDeaths = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            totalDeaths = jsonObject.getString(Paises.TOTAL_DEATHS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalDeaths;
    }

    private String getTotalRecovered(int position){
        String totalRecovered = "";
        try{
            JSONObject jsonObject = result.getJSONObject(position);
            totalRecovered = jsonObject.getString(Paises.TOTAL_RECOVERED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalRecovered;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvTotalConfirmed.setText(getTotalConfirmed(position));
        tvTotalDeaths.setText(getTotalDeaths(position));
        tvTotalRecovered.setText(getTotalRecovered(position));

        data = new ArrayList<>();

        data = new ArrayList<>();
        data.add(new BarEntry(2, Float.parseFloat(getTotalConfirmed(position))));
        data.add(new BarEntry(4, Float.parseFloat(getTotalDeaths(position))));
        data.add(new BarEntry(6, Float.parseFloat(getTotalRecovered(position))));

        barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(16f);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);
        
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Estadisticas por Pais");
        barChart.animateY(2000);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}