package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import java.util.Locale;

public class LangSelect extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lang_select);
        //action bar
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getResources().getString(R.string.app_name));

        getDialog();

    }
    public void getDialog(){
        //arrays of languages in an alert dialog

        final String[] ListItems = {"English","Kinyarwanda"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(ListItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0) {
                    //english
                    setLocale("En");
                    recreate();
                }
                else if (i==1){
                    //kinyarwanda
                    setLocale("b+rwk");
                    recreate();
                }
                //dismiss alert dialog when language selected
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
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
        if (language != null) {
            setLocale(language);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LangSelect.this, splash_screen.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_SCREEN);
        } else
            setLocale(language);
    }
}