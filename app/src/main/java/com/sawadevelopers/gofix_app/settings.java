package com.sawadevelopers.gofix_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class settings extends AppCompatActivity {
    TextView tvback,username,phone,logout,ChangePassword;
    private RequestQueue rQueue;
    String userStored,phoneStored;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // retrieveUser();
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        tvback = findViewById(R.id.tvBack);
        username = findViewById(R.id.tvUsername);
        phone = findViewById(R.id.phone);
        logout = findViewById(R.id.tvLogout);
        ChangePassword = findViewById(R.id.tvChangePassword);

        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        phoneStored = prefs.getString("phone",null);
        username.setText(userStored);
        phone.setText(phoneStored);


        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =  new Intent(settings.this,Dashboard.class);
               startActivity(intent);
               finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(settings.this,ChangePassword.class);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(settings.this,login.class);
                startActivity(intent);
                finish();
                SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
                pref.edit().clear().apply();
                SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
                prefs.edit().clear().apply();
                //shared preferences
                SharedPreferences prefss = getSharedPreferences("logindata", MODE_PRIVATE);
                prefss.edit().clear().apply();

            }
        });


    }
    private void retrieveUser() {
        //final String userMail = sessionmail;
        //System.out.println(sessionmail);
        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        String sessionmai = pref.getString("username",null);
        String url = "https://www.sawadevelopers.rw/gofixapp/android/home.php?email="+sessionmai;
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
                        String phonee = jsonObject.getString("phone");

                        username.setText(fname + " " + lname);
                        phone.setText(phonee);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null){
                    Toast.makeText(settings.this,error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        rQueue = Volley.newRequestQueue(settings.this);
        rQueue.add(stringRequest);
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