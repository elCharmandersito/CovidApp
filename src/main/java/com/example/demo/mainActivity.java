package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends AppCompatActivity {

    private Button popupBtn;
    Dialog mDialog;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupBtn = findViewById(R.id.aboutApp);
        mDialog = new Dialog(this);

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.setContentView(R.layout.popup);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1_estadisticasMundiales:
                intent = new Intent(this, EstadisticasMundiales.class);
                startActivity(intent);
                break;

            case R.id.btn2_estadisticasNacionales:
                intent = new Intent(this, EstadisticasNacionales.class);
                startActivity(intent);
                break;

            case R.id.btn3_estadisticasRegionales:
                intent = new Intent(this, EstadisticasRegionales.class);
                startActivity(intent);
                break;
        }
    }
}