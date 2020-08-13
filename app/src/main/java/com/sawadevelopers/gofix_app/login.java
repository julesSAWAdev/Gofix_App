package com.sawadevelopers.gofix_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class login extends AppCompatActivity {

    Button callSignUp,loginbtn,forget;
    TextView logotext,slogan;
    ImageView image;
    TextInputLayout username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.do_register);
        image = findViewById(R.id.logoImage);
        logotext = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);
        forget = findViewById(R.id.forgot);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,Registration.class);

                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logotext,"logo_text");
                pairs[2] = new Pair<View,String>(slogan,"logo_desc");
                pairs[3] = new Pair<View,String>(username,"username_trans");
                pairs[4] = new Pair<View,String>(password,"user_trans");
                pairs[5] = new Pair<View,String>(loginbtn,"login_trans");
                pairs[6] = new Pair<View,String>(callSignUp,"acc_trans");
                pairs[7] = new Pair<View,String>(forget,"forg_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                    startActivity(intent, options.toBundle());
                   // finish();
                }
            }
        });


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,ResetPassword.class);
                startActivity(intent);

            }
        });

        Button changelang = findViewById(R.id.change_lang);
        changelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }
    private void showChangeLanguageDialog(){
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
                    setLocale("rwk");
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
            setLocale(language);
    }
}