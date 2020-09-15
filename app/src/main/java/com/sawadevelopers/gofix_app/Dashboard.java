package com.sawadevelopers.gofix_app;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    ImageView settings;
    String sessionmail,userStored;
    TextView autoser,mechanician,spare,username,rental;
    Button service;
    private RequestQueue rQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieveUser();
        loadLocale();
       // retrieveUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        settings = findViewById(R.id.ivSettings);
        //autoser = findViewById(R.id.autoser);
        //mechanician = findViewById(R.id.mechanic);
        spare = findViewById(R.id.spare);
        username = findViewById(R.id.tvUsername);
        rental = findViewById(R.id.rental);
        service = findViewById(R.id.btnReport);

        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        if (userStored != null) {
            username.setText(userStored);
        }else{
            retrieveUser();
        }
        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);
        //System.out.println("the session email is !!!!!! " +sessionmail);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), settings.class));
                //finish();
            }
        });

        spare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpareServices.class));
               // finish();
            }
        });
        rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegCarRental.class));
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void retrieveUser() {
       // SharedPreferences pref = getSharedPreferences("userLoginData", MODE_PRIVATE);
        //userStored = pref.getString("fullusername", null);
        //System.out.println("fullusername : "+userStored);
        //if (userStored != null)
          //  username.setText(userStored);
        //else {

            //final String userMail = sessionmail;
            //System.out.println(sessionmail);
            SharedPreferences prefs = getSharedPreferences("logindata", MODE_PRIVATE);
            String sessionmai = prefs.getString("username", null);
            String url = "https://www.gofix.rw/android/home.php?email=" + sessionmai;
            System.out.println(url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    rQueue.getCache().clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String fname = jsonObject.getString("Firstname");
                            String lname = jsonObject.getString("Lastname");
                            String phone = jsonObject.getString("phone");

                            username.setText(fname + " " + lname);
                            SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("fullusername", fname + " " + lname);
                            editor.putString("phone",phone);
                            editor.apply();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        int MY_SOCKET_TIMEOUT_MS=5000000;
            rQueue = Volley.newRequestQueue(Dashboard.this);
            rQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }
    //}
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