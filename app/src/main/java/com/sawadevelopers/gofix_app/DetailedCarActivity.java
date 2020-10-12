package com.sawadevelopers.gofix_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailedCarActivity extends AppCompatActivity {
    private ImageView image11,image12,image13;
    private TextView mTitle,price_daily,price_monthly,enginee,tanke,hour_price;
    int userid;
    int rentid;
    Button btnBook;
    TextView tvback,username;
    ImageView settings;
    String userStored,sessionmail;
    TextView phonee,addresss;
    private ProgressDialog progressDialog;
    private String car_id ;
    String booking_url = "http://gofix.rw/android/Carbooking.php";
    private RequestQueue rQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
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
        hour_price = findViewById(R.id.price_hourly);


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
        String hour = intent.getStringExtra("hourprice");
        car_id = intent.getStringExtra("car_id");
       // Picasso.get().load("image1").into(image11);
        //Picasso.get().load("image2").into(image12);

        if (intent != null){
            mTitle.setText(titleCar);
            enginee.setText(engine);
            tanke.setText(tank);
            price_daily.setText("Daily: "+daily+" Rwf");
            price_monthly.setText("Monthly: "+monthly +" Rwf");
            hour_price.setText("hourly: "+hour +" Rwf");
            userid = (int)Math.round(user);
            rentid = (int)Math.round(rent_id);
            phonee.setText(phone);
            addresss.setText(address);


            Glide.with(DetailedCarActivity.this).load(image3).into(image11);
            Glide.with(DetailedCarActivity.this).load(image1).into(image12);
            Glide.with(DetailedCarActivity.this).load(image2).into(image13);

           // System.out.println("userid "+ userid );


        }

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 RegisterBooking();

            }
        });




    }
    public void RegisterBooking(){
        final String carId = String.valueOf(this.car_id);
        final String username = this.sessionmail;


        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait, sending request..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, booking_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(DetailedCarActivity.this, "car booking request sent successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), BooksSuccess.class));
                                finish();
                            } else {
                                Toast.makeText(DetailedCarActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.dismiss();
                Toast.makeText(DetailedCarActivity.this, "Make sure you have a working internet connection", Toast.LENGTH_SHORT).show();


            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("carid", carId);
                params.put("username", username);
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(DetailedCarActivity.this);
        rQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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