package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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

public class FindElectrinic extends AppCompatActivity {
    TextView tvback,findSpare,username;
    ImageView settings,imageview;
    String sessionmail,userStored;
    TextView mTitle,mDesc,mDestitle,desprice,mPrice,phonee,addresss,contact;
    Button btnOrder;
    int defSpareid,defShopid;
    String BASE_URL = "http://gofix.rw/android/requestElo.php";
    private RequestQueue rQueue;
    ProgressBar proSpare;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retrieveUser();
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_find_electrinic);

        tvback = findViewById(R.id.back);
        settings = findViewById(R.id.ivSettings);
        username = findViewById(R.id.tvUsername);
        imageview = findViewById(R.id.image1);
        mTitle = findViewById(R.id.spareName);
        mDesc = findViewById(R.id.description);
        mDestitle = findViewById(R.id.destitle);
        desprice = findViewById(R.id.desprice);
        mPrice = findViewById(R.id.price);
        btnOrder = findViewById(R.id.btnOrder);
        proSpare = findViewById(R.id.proSpare);
        addresss = findViewById(R.id.address);
        phonee = findViewById(R.id.phone);
        contact = findViewById(R.id.contact);

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
                Intent intent =  new Intent(FindElectrinic.this,Electronics_parts.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });



        //catching coming intent
        Intent intent = getIntent();

        String name = intent.getStringExtra("title");
        String image = intent.getStringExtra("image");
        String description = intent.getStringExtra("description");
        Double price = intent.getDoubleExtra("price",0);
        Double spareid = intent.getDoubleExtra("spareid",0);
        Double shopid = intent.getDoubleExtra("userid",0);
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        // Picasso.get().load("image1").into(image11);
        //Picasso.get().load("image2").into(image12);
        int defPrice = (int)Math.round(price);
        defSpareid = (int)Math.round(spareid);
        defShopid = (int)Math.round(shopid);

        if (intent != null){
            mTitle.setText(name);
            mDestitle.setPaintFlags(mDestitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            desprice.setPaintFlags(desprice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            contact.setPaintFlags(contact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mDesc.setText(description);
            mPrice.setText(defPrice+" Rwf");
            addresss.setText(address);
            phonee.setText(phone);
            //price_daily.setText("Daily: "+daily+" Rwf");
            //price_monthly.setText("Monthly: "+monthly +" Rwf");
            //userid = user;
            //rentid = rent_id;

            Glide.with(FindElectrinic.this).load(image).into(imageview);


            //System.out.println(image2);


        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spareRequest();


            }
        });


    }

    public void spareRequest(){
        final String spareID = String.valueOf(this.defSpareid);
        final String shopID = String.valueOf(this.defShopid);
        final String username = this.sessionmail;


        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait, sending request..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(FindElectrinic.this, "Device ordered successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), SpareSuccess.class));
                                finish();
                            } else {
                                Toast.makeText(FindElectrinic.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FindElectrinic.this, "Make sure you have a working internet connection", Toast.LENGTH_SHORT).show();


            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("spareID", spareID);
                params.put("shopID", shopID);
                params.put("username", username);
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(FindElectrinic.this);
        rQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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