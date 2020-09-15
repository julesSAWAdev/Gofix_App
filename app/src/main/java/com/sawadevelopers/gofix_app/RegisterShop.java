package com.sawadevelopers.gofix_app;

import android.app.Activity;
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


public class RegisterShop extends AppCompatActivity {

    EditText shopname,address,tin,shopowner,phone;
    Button registershopp;
    String URL_regiShop = "http://gofix.rw/android/RegisterShop.php";
    String emailStored,userStored;
    ProgressBar loader;
    private RequestQueue rQueue;
    TextView tvback,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_shop);


        //hooks
        shopname = findViewById(R.id.shopname);
        address = findViewById(R.id.address);
        tin = findViewById(R.id.tin);
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
                Intent intent =  new Intent(RegisterShop.this,SpareServices.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        registershopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterShop();
            }
        });

    }
   private void RegisterShop(){
       loader.setVisibility(View.VISIBLE);
       registershopp.setVisibility(View.GONE);

       final String NameShop = this.shopname.getText().toString();
       final String ShopAdress = this.address.getText().toString();
       final String ShopTin = this.tin.getText().toString();
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
                           JSONObject jsonObject = new JSONObject(response);
                           if (jsonObject.optString("success").equals("1")) {
                               Toast.makeText(RegisterShop.this, "Shop Added Successfully", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getBaseContext(), ShopSuccess.class));
                               finish();
                           } else {
                               Toast.makeText(RegisterShop.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                               loader.setVisibility(View.GONE);
                               registershopp.setVisibility(View.VISIBLE);
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(RegisterShop.this, error.toString(), Toast.LENGTH_LONG).show();
                       loader.setVisibility(View.GONE);
                       registershopp.setVisibility(View.VISIBLE);
                   }
               }) {
           @Override
           protected Map<String, String> getParams() {
               Map<String, String> params = new HashMap<String, String>();
               params.put("shopname", NameShop);
               params.put("shopadress", ShopAdress);
               params.put("shoptin", ShopTin);
               params.put("phone", ShopPhone);
               params.put("shopowner", ShopOwner);
               params.put("user_id", emailStored);
               return params;
           }
       };
       int MY_SOCKET_TIMEOUT_MS=5000000;
       rQueue = Volley.newRequestQueue(RegisterShop.this);
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