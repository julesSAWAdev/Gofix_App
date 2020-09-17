package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener {
    ImageView settings;
    TextView tvback,username;
    String sessionmail,userStored;
    private TextView mDisplayDate;
    private Button selectDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private MaterialSpinner services,utility,agents,province,district;

    //An ArrayList for Spinner Items
    private ArrayList<String> provinces;
    private ArrayList<String> districts;
    //JSON Array
    private JSONArray result;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.ivSettings);
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        mDisplayDate = findViewById(R.id.tvDate);
        selectDate = findViewById(R.id.selectdate);
        services = findViewById(R.id.SpService);
        utility = findViewById(R.id.spUtility);
        agents = findViewById(R.id.spAgent);
        province = findViewById(R.id.SpProvince);
        district = findViewById(R.id.spDistrict);

        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);

        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);

        //Initializing the ArrayList
        provinces = new ArrayList<String>();
        districts = new ArrayList<String>();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), settings.class));
                finish();
            }
        });
        //spinners configuration

        services.setItems("Engine", "Electricity", "Pnematic", "Suspension","Painting","Gearbox","AC");
        services.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        utility.setItems("Repair", "Maintenance");
        utility.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        agents.setItems("Garage", "private mechanician");
        agents.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        province.setOnItemSelectedListener(this);
        province.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                //Snackbar.make(view, "Clicked " + position, Snackbar.LENGTH_LONG).show();
                int Itemposition = position + 1;
                //System.out.println(Itemposition);
               // district.setAdapter(null);
                getDistricts(Itemposition);

            }
        });
        getData();


        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,Dashboard.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
                mDisplayDate.setVisibility(View.VISIBLE);
            }
        };

    }
    private void getData(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retrieving Provinces");
        progressDialog.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(ProvinceConfig.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0;i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                provinces.add(name);
                            }
                            //Setting adapter to show the items in the spinner
                            province.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, provinces));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Make sure you are connected to the internet",Toast.LENGTH_LONG).show();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.hide();
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    private void getDistricts(int provinceID){
        district.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, Collections.<String>emptyList()));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retrieving districts");
        progressDialog.show();
        //Creating a string request
        StringRequest stringRequest = new StringRequest("http://gofix.rw/android/getDistrict.php?pro_id="+provinceID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.hide();

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0;i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                String name = jsonObject.getString("district_name");
                                districts.add(name);
                            }
                            //Setting adapter to show the items in the spinner
                            district.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, districts));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Make sure you are connected to the internet",Toast.LENGTH_LONG).show();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.hide();
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

    }
}