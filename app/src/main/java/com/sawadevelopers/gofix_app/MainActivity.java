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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener {
    ImageView settings;
    TextView tvback,username;
    String sessionmail,userStored;
    String utilitys;
    private TextView mDisplayDate;
    private Button selectDate,btnRequest;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private MaterialSpinner services,utility,agents,province,district,sResult;
    private int agent_id;
    private RequestQueue rQueue;

    //An ArrayList for Spinner Items
    private ArrayList<String> provinces;
    private ArrayList<String> districts;
    private ArrayList<String> results;
    public ArrayList<Integer> ids;
    //JSON Array
    private JSONArray result;
    private ProgressDialog progressDialog;
    private  TextView Gnumber,Gname,Gdesc,Gloc;
    private String selUtility;
    private String districtName;
    LinearLayout info;
    String Request_url = "\"http://gofix.rw/android/request_agent.php";

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
        Gname = findViewById(R.id.garageName);
        Gnumber = findViewById(R.id.garageNumber);
        Gdesc = findViewById(R.id.garageDescription);
        Gloc = findViewById(R.id.garageLocation);
        sResult = findViewById(R.id.spResults);
        info = findViewById(R.id.info);
        btnRequest = findViewById(R.id.register);


        //tvusername
        SharedPreferences prefs = getSharedPreferences("userLoginData", MODE_PRIVATE);
        userStored = prefs.getString("fullusername", null);
        username.setText(userStored);

        SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
        sessionmail = pref.getString("username",null);
        //
        selUtility = "Garage";
        //Initializing the ArrayList
        provinces = new ArrayList<String>();
        districts = new ArrayList<String>();
        results = new ArrayList<String>();
        ids = new ArrayList<Integer>();

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
                if (position == 1)
                 selUtility = "private";
                else
                 selUtility = item;
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

        sResult.setOnItemSelectedListener(this);
        sResult.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                 agent_id = ids.get(position);
                System.out.println(agent_id);
                System.out.println(sessionmail);
                getinformation(agent_id,selUtility);
                System.out.println(selUtility);

            }
        });
        district.setOnItemSelectedListener(this);
        district.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                 districtName = item.toString();
                 System.out.println(districtName);
                 System.out.println(selUtility);

                getResults(districtName,selUtility);

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
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequest();
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
    private void getinformation(int id_agent, String util){
        final int agentid = id_agent;
        final String agent = util;
        String URL = "http://gofix.rw/android/getInfo.php?agentid="+agentid+"&agent="+util;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retrieving results");
        progressDialog.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(URL,
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
                                String phone = jsonObject.getString("phone");
                                String location = jsonObject.getString("district_name");
                                String descr = jsonObject.getString("description");
                                int id = jsonObject.getInt("id");
                                results.add(name);
                                ids.add(id);
                                Gloc.setText(location);
                                Gdesc.setText(descr);
                                Gname.setText(name);
                                Gnumber.setText(phone);
                                info.setVisibility(View.VISIBLE);
                            }

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
    private void getResults(String dist, String utils){
        final String util = utils;
        final String distr = dist;
        String URL = "http://gofix.rw/android/getResult.php?district="+distr+"&agent="+util;
        results.clear();
        ids.clear();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retrieving results");
        progressDialog.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(URL,
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
                                String phone = jsonObject.getString("phone");
                                String location = jsonObject.getString("district_name");
                                String descr = jsonObject.getString("description");
                                int id = jsonObject.getInt("id");
                                results.add(name);
                                ids.add(id);
                                //Gloc.setText(location);
                                //Gdesc.setText(descr);
                                //Gname.setText(name);
                                //Gnumber.setText(phone);
                            }
                            //System.out.println(ids);
                            //Setting adapter to show the items in the spinner
                            sResult.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, results));
                            //getDistricts(1);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,"No Garage/Mechanician available in that area",Toast.LENGTH_LONG).show();
                            sResult.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, results));

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
    private void getData(){
        provinces.clear();
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
                            getDistricts(1);
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
        //ArrayList<String> districtss = new ArrayList<String>();
        //district.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, districtss));
       districts.clear();
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
        int MY_SOCKET_TIMEOUT_MS=20000;
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void SendRequest(){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registration in progress");
        progressDialog.show();

        final String finalUtility = this.utility.getText().toString();
        final String finalAgent = this.agents.getText().toString();
        final String finalProvince = this.province.getText().toString();
        final String finalDistrict = this.district.getText().toString();
        final int finalAgentid = this.agent_id;
        final String agentId = Integer.toString(finalAgentid);
        final String finalUser = this.sessionmail.toString();
        final String finalDate = this.mDisplayDate.getText().toString();





        // System.out.println("utility : "+finalUtility);
       // System.out.println("agent : "+finalAgent);
        //System.out.println("province : "+finalProvince);
     //   System.out.println("district : "+finalDistrict);
       // System.out.println("agentd : "+finalAgentid);
       // System.out.println("user : "+finalUser);
       // System.out.println("date : "+finalDate);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(MainActivity.this, "Request Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), requestSuccess.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Make sure you have internet", Toast.LENGTH_LONG).show();
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("utility", finalUtility);
                params.put("agent", finalAgent);
                params.put("province", finalProvince);
                params.put("district", finalDistrict);
                params.put("agentid", agentId);
                params.put("userMail", finalUser);
                params.put("datereq", finalDate);
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(MainActivity.this);
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

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

    }
}