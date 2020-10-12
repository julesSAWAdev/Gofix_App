package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    TextView tvback,username;
    private ProgressDialog progressDialog;
    final String TAG = "Change";
    Button resetPasswdBtn;
    EditText etCurrentPassword;
    EditText etNewPassword;
    EditText etConfirmedPassword;
    String userStored,sessionmail,phoneStored;
    private RequestQueue rQueue;
    String URL_pwd = "http://gofix.rw/android/changePassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);

        etCurrentPassword    = findViewById(R.id.oldpwd);
        etNewPassword        = findViewById(R.id.newpwd);
        etConfirmedPassword  = findViewById(R.id.cnewpwd);
        resetPasswdBtn       = findViewById(R.id.btnResetPassword);


        //shared preferences
        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);


        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        phoneStored = prefs.getString("phone",null);
        username.setText(userStored);

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChangePassword.this,settings.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        resetPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNewPassword.getText().toString().equals( etConfirmedPassword.getText().toString())) {
                    changePwd();
                }else {
                    Toast.makeText(ChangePassword.this, "New and confirmed password doesn't match", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void changePwd(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("password update in progress");
        progressDialog.show();

        final String user = this.sessionmail.toString();
        final String oldpswd = this.etCurrentPassword.getText().toString();
        final String newpswd = this.etNewPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_pwd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(ChangePassword.this, "Password changed Successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
                                SharedPreferences.Editor editors = pref.edit();
                                editors.clear();
                                editors.commit();
                                startActivity(new Intent(getBaseContext(), login.class));
                                finish();
                            } else {
                                Toast.makeText(ChangePassword.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangePassword.this, "Make sure you have internet", Toast.LENGTH_LONG).show();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user);
                params.put("oldpwd", oldpswd);
                params.put("newpwd", newpswd);
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(ChangePassword.this);
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