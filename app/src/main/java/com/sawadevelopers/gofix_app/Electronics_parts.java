package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Electronics_parts extends AppCompatActivity {
    private List<Electronics> electronics;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    TextView tvback,username;
    ImageView settings;
    String userStored,sessionmail;
    private ProgressDialog progressDialog;


    private final String BASE_URL = "http://gofix.rw/android/getElectronics.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_electronics_parts);


        //hooks
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.spare_recyclerView);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        settings = findViewById(R.id.ivSettings);
        manager = new GridLayoutManager(Electronics_parts.this,2);
        recyclerView.setLayoutManager(manager);
        electronics = new ArrayList<>();

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
                Intent intent =  new Intent(Electronics_parts.this,SpareServices.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        getElectronics();
    }
    public void getElectronics(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait, retrieving electronics...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0;i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String name = jsonObject.getString("sparename");
                                double price = jsonObject.getDouble("spareprice");
                                String description = jsonObject.getString("sparedescription");
                                String image = jsonObject.getString("image");
                                String phone = jsonObject.getString("phone");
                                String address = jsonObject.getString("address");
                                double spareid = jsonObject.getDouble("spareid");
                                double shopid = jsonObject.getDouble("shopid");

                                Electronics electronic = new Electronics(spareid,name,price,image,shopid,description,phone,address);
                                electronics.add(electronic);




                            }

                        } catch (Exception e) {
                        }
                        mAdapter = new ElectronicsRecyclerAdapter(Electronics_parts.this, electronics);
                        recyclerView.setAdapter(mAdapter);
                        //System.out.println(electronics);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.dismiss();
                Toast.makeText(Electronics_parts.this,"Make sure you are connected to the internet",Toast.LENGTH_LONG).show();
            }
        });
        int MY_SOCKET_TIMEOUT_MS=5000000;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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