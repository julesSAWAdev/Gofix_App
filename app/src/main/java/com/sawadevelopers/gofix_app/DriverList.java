package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

public class DriverList extends AppCompatActivity {
    private List<Driver> driver;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    TextView tvback,username;
    ImageView settings;
    String userStored,sessionmail;
    private final String BASE_URL = "http://gofix.rw/android/getDrivers.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        //hooks
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.driver_recyclerView);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        settings = findViewById(R.id.ivSettings);
        manager = new GridLayoutManager(DriverList.this,2);
        recyclerView.setLayoutManager(manager);
        driver = new ArrayList<>();

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
                Intent intent =  new Intent(DriverList.this,driverHire.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        getDriver();

    }

    private void getDriver(){
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0;i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("driver_id");
                                String name = jsonObject.getString("driver_name");
                                String image = jsonObject.getString("image");
                                String phone = jsonObject.getString("phone");
                                String address = jsonObject.getString("address");
                                String idnumber = jsonObject.getString("NID");
                                String license = jsonObject.getString("license_number");
                                String category = jsonObject.getString("categories");
                                String created = jsonObject.getString("created");

                                Driver drivers = new Driver(id,name,idnumber,license,phone,category,address,created,image);
                                driver.add(drivers);




                            }

                        } catch (Exception e) {
                        }
                        mAdapter = new DriverRecyclerAdapter(DriverList.this, driver);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DriverList.this,error.toString(),Toast.LENGTH_LONG).show();
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