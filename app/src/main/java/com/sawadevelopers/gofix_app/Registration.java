package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static androidx.core.content.ContentProviderCompat.requireContext;

public class Registration extends AppCompatActivity {
    Button callLogin;
    TextView firstname,lastname,email,phone,password,passwordConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_registration);


        String[] type = new String[] {"Bed-sitter", "Single", "1- Bedroom", "2- Bedroom","3- Bedroom"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.user_type_select,
                        type);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.user_type_select);
        editTextFilledExposedDropdown.setAdapter(adapter);

        //hooks
        callLogin = findViewById(R.id.acc_exist);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        passwordConf = findViewById(R.id.conf_pass);


        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });


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