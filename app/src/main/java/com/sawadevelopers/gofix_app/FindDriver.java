package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import java.util.Locale;

public class FindDriver extends AppCompatActivity {
    TextView tvback, findSpare, username;
    ImageView settings, imageview;
    String sessionmail, userStored;
    TextView mTitle, mDesc, categoriesss,joinedd, dlnumberr, mPrice, phonee, addresss, contact;
    Button btnOrder;
    int defSpareid, defShopid;
    String BASE_URL = "http://gofix.rw/android/requestDriver.php";
    private RequestQueue rQueue;
    ProgressBar proSpare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_driver);


        tvback = findViewById(R.id.back);
        settings = findViewById(R.id.ivSettings);
        username = findViewById(R.id.tvUsername);
        imageview = findViewById(R.id.driverimage);
        mTitle = findViewById(R.id.drivername);
        mDesc = findViewById(R.id.description);
        categoriesss = findViewById(R.id.categoriess);
        btnOrder = findViewById(R.id.btnOrder);
        proSpare = findViewById(R.id.proSpare);
        addresss = findViewById(R.id.address);
        phonee = findViewById(R.id.phone);
        dlnumberr = findViewById(R.id.dlnumber);
        joinedd = findViewById(R.id.joined);

        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);

        //shared preferences
        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username", null);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), settings.class));
                finish();
            }
        });

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindDriver.this, DriverList.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        //catching coming intent
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        String dlnumber = intent.getStringExtra("dlnumber");
        String address = intent.getStringExtra("address");
        String joined = intent.getStringExtra("created");
        String driver_id = intent.getStringExtra("id");
        String phone = intent.getStringExtra("phone");
        String categories = intent.getStringExtra("categories");

        if (intent != null) {
            mTitle.setText(name);
            categoriesss.setText(categories);
            phonee.setText("Phone number: "+phone);
            dlnumberr.setText("DL number: "+dlnumber);
            addresss.setText("Address: "+address);
            joinedd.setText("Joined: "+joined);
            //price_daily.setText("Daily: "+daily+" Rwf");
            //price_monthly.setText("Monthly: "+monthly +" Rwf");
            //userid = user;
            //rentid = rent_id;

            Glide.with(FindDriver.this).load(image).into(imageview);


        }
    }
    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
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
