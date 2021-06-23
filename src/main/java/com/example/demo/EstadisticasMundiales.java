package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstadisticasMundiales extends AppCompatActivity{

    private PieChart pieChart;

    private JSONObject result;

    private TextView tvShowTotalConfirmed;
    private TextView tvShowTotalDeaths;
    private TextView tvShowTotalRecovered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initUI();
        callWebServices();
    }
    public void onClick(View view) {
        finish();
    }

    private void callWebServices(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Global.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject array = null;
                        try{
                            array = new JSONObject(response);
                            result = array.getJSONObject(Global.JSONObject);
                            String totalConfirmed = result.getString("TotalConfirmed");
                            String totalDeaths = result.getString("TotalDeaths");
                            String totalRecovered = result.getString("TotalRecovered");

                            tvShowTotalConfirmed.setText(totalConfirmed);
                            tvShowTotalDeaths.setText(totalDeaths);
                            tvShowTotalRecovered.setText(totalRecovered);

                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();

                            ArrayList<PieEntry> data = new ArrayList<>();
                            data.add(new PieEntry(Float.parseFloat(totalConfirmed), "Confirmados"));
                            data.add(new PieEntry(Float.parseFloat(totalDeaths), "Muertos"));
                            data.add(new PieEntry(Float.parseFloat(totalRecovered), "Recuperados"));

                            PieDataSet pieDataSet = new PieDataSet(data, "");
                            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            pieDataSet.setValueTextColor(Color.BLACK);
                            pieDataSet.setValueTextSize(16f);

                            PieData pieData = new PieData(pieDataSet);

                            pieChart.setData(pieData);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setCenterText("Estadisticas Mundiales Covid-19");
                            pieChart.animate();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void initUI(){
        pieChart = findViewById(R.id.pieChart);
        tvShowTotalConfirmed = findViewById(R.id.tvShowTotalConfirmed);
        tvShowTotalDeaths = findViewById(R.id.tvShowTotalDeaths);
        tvShowTotalRecovered = findViewById(R.id.tvShowTotalRecovered);
    }
}