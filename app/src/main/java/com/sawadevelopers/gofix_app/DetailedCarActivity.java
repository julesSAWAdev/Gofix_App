package com.sawadevelopers.gofix_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class DetailedCarActivity extends AppCompatActivity {
    private ImageView image11,image12,image13;
    private TextView mTitle,price_daily,price_monthly,enginee,tanke;
    int userid;
    int rentid;
    Button btnBook;
    TextView tvback,username;
    ImageView settings;
    String userStored,sessionmail;
    TextView phonee,addresss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_detailed_car);

        //hooks

        image11 = findViewById(R.id.image1);
        image12 = findViewById(R.id.image2);
        image13 = findViewById(R.id.image3);
        mTitle = findViewById(R.id.title);
        price_daily = findViewById(R.id.price_daily);
        price_monthly = findViewById(R.id.price_monthly);
        enginee = findViewById(R.id.engine);
        tanke = findViewById(R.id.tank);
        btnBook = findViewById(R.id.btnBook);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        settings = findViewById(R.id.ivSettings);
        phonee = findViewById(R.id.phone);
        addresss = findViewById(R.id.address);


        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);

        //shared preferences
        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);

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
                Intent intent =  new Intent(DetailedCarActivity.this,CarRentals.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        //catching coming intent
        Intent intent = getIntent();

        String titleCar = intent.getStringExtra("title");
        String image1 = intent.getStringExtra("image1");
        String image2 = intent.getStringExtra("image2");
        String image3 = intent.getStringExtra("image3");
        String engine = intent.getStringExtra("engine");
        String tank = intent.getStringExtra("tank");
        Double daily = intent.getDoubleExtra("daily",0);
        Double monthly = intent.getDoubleExtra("monthly",0);
        Double user = intent.getDoubleExtra("user",0);
        Double rent_id = intent.getDoubleExtra("rent_id",0);
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
       // Picasso.get().load("image1").into(image11);
        //Picasso.get().load("image2").into(image12);

        if (intent != null){
            mTitle.setText(titleCar);
            enginee.setText(engine);
            tanke.setText(tank);
            price_daily.setText("Daily: "+daily+" Rwf");
            price_monthly.setText("Monthly: "+monthly +" Rwf");
            userid = (int)Math.round(user);
            rentid = (int)Math.round(rent_id);
            phonee.setText(phone);
            addresss.setText(address);


            Glide.with(DetailedCarActivity.this).load(image3).into(image11);
            Glide.with(DetailedCarActivity.this).load(image1).into(image12);
            Glide.with(DetailedCarActivity.this).load(image2).into(image13);

           // System.out.println("userid "+ userid );


        }






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