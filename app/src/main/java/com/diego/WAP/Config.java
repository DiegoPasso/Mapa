package com.diego.WAP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Config extends AppCompatActivity {
    private RadioButton RadioVT, RadioIS;
    SharedPreferences sharedPreferences;
    private Switch btnSW;
    private ImageView voltar;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().hide();
        setTitle("CONFIGURAÇÃO");

        btnSalvar = findViewById(R.id.btnSalvar);
        voltar = findViewById(R.id.voltar2);

        sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);

        int tipo = sharedPreferences.getInt("Tipo", 2);
        int tra = sharedPreferences.getInt("Trafego", 1);


        RadioVT = findViewById(R.id.RadioVT);
        RadioIS = findViewById(R.id.RadioIS);
        switch (tipo) {
            case 1:
                RadioVT.setChecked(true);
                break;
            case 2:
                RadioIS.setChecked(true);
                break;
        }

        btnSW = findViewById(R.id.btnSW);
        switch (tra) {
            case 1:
                btnSW.setChecked(false);
                break;
            case 2:
                btnSW.setChecked(true);
                break;
        }

        RadioGroup groupTipo = findViewById(R.id.GroupMP);
        groupTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup groupTipo, int checkedId) {
                if (checkedId == R.id.RadioVT) {
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putInt("Tipo", 1);
                    sharedPreferencesEditor.commit();
                } else if (checkedId == R.id.RadioIS) {
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putInt("Tipo", 2);
                    sharedPreferencesEditor.commit();
                }
            }
        });
        Switch switchTra = findViewById(R.id.btnSW);
        switchTra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == false) {
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putInt("Trafego", 1);
                    sharedPreferencesEditor.commit();
                } else if (isChecked) {
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putInt("Trafego", 2);
                    sharedPreferencesEditor.commit();
                }
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapScreen = new Intent(getApplicationContext(), Maps.class);
                startActivity(MapScreen);
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Config.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}