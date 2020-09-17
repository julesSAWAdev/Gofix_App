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
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterRental extends AppCompatActivity {
    EditText shopname,address,shopowner,phone;
    Button registershopp;
    String URL_regiShop = "http://gofix.rw/android/RegisterRental.php";
    String emailStored,userStored;
    ProgressBar loader;
    private RequestQueue rQueue;
    TextView tvback,username;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_rental);


        //hooks
        shopname = findViewById(R.id.shopname);
        address = findViewById(R.id.address);
        shopowner = findViewById(R.id.shopowner);
        phone = findViewById(R.id.phone);
        registershopp = findViewById(R.id.registershop);
        loader = findViewById(R.id.ShopLoader);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);

        //shared preference
        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);


        SharedPreferences pref = getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        emailStored = pref.getString("username",null);

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(RegisterRental.this,RegCarRental.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        registershopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRental();
            }
        });


    }

    private void RegisterRental(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registration in progress");
        progressDialog.show();

        final String NameShop = this.shopname.getText().toString();
        final String ShopAdress = this.address.getText().toString();
        final String ShopOwner = this.shopowner.getText().toString();
        final String ShopPhone = this.phone.getText().toString();
        final String user = this.emailStored.toString();

        // System.out.println("user_id : "+user);
        //System.out.println("shopname : "+NameShop);
        //System.out.println("ShopAdress : "+ShopAdress);
        //System.out.println("ShopOwner : "+ShopOwner);
        //System.out.println("ShopPhone : "+ShopPhone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_regiShop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(RegisterRental.this, "Company Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), RentalSuccess.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterRental.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterRental.this, "Make sure you have internet", Toast.LENGTH_LONG).show();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("shopname", NameShop);
                params.put("shopadress", ShopAdress);
                params.put("phone", ShopPhone);
                params.put("shopowner", ShopOwner);
                params.put("user_id", emailStored);
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(RegisterRental.this);
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