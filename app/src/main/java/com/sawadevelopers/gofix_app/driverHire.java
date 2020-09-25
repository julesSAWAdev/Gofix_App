package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class driverHire extends AppCompatActivity {
    ImageView settings;
    TextView tvback,username,findDriver;
    String sessionmail,userStored;
    Button regDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_hire);

        //hooks
        settings = findViewById(R.id.ivSettings);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        regDriver = findViewById(R.id.registerdriver);
        findDriver= findViewById(R.id.findriver);


        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);

        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), settings.class));
                finish();
            }
        });
        regDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), register_driver.class));
                finish();
            }
        });

        findDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DriverList.class));
                finish();
            }
        });


        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(driverHire.this,Dashboard.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    private void setLocale(String lang) {

        Locale locale =new Locale(lang);
        Locale.setDefault(locale);

        Configuration config  = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang",lang);
        editor.apply();
    }

    //load language saved in shared preferences
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");
        setLocale(language);
    }
}