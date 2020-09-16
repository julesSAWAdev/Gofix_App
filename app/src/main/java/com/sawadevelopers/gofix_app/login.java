package com.sawadevelopers.gofix_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class login extends AppCompatActivity {

    final String TAG = "Login";
    Button callSignUp,loginbtn,forget;
    TextView logotext,slogan;
    ImageView image;
    TextInputLayout username,password;
    TextInputEditText etEmailAddress;
    TextInputEditText etPassword;
    private RequestQueue rQueue;
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    ProgressBar loading;
    private static String URL_Login = "http://gofix.rw/android/Login.php";

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
        etEmailAddress = findViewById(R.id.etusername);
        etPassword = findViewById(R.id.etpassword);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        loading = findViewById(R.id.loading);


    loginbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            loginAction();
        }
    });

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
                   // ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                    startActivity(intent);
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

    private void loginAction(){
        loading.setVisibility(View.VISIBLE);
        loginbtn.setVisibility(View.GONE);

        final String username = this.etEmailAddress.getText().toString().trim();
        final String password = this.etPassword.getText().toString().trim();

        if(username.isEmpty()){
            etEmailAddress.setError("Email or phone required");

            loading.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            loading.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rQueue.getCache().clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("success").equals("1")) {
                       SharedPreferences pref = getSharedPreferences("logindata", MODE_PRIVATE);
                       SharedPreferences.Editor editor = pref.edit();
                       editor.putString("username", username);
                       editor.putString("password", password);
                       editor.apply();
                        Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), Dashboard.class));
                        finish();
                    } else {
                        Toast.makeText(login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        loginbtn.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login.this, "Make sure you are connected to the internet", Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                        loginbtn.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
         HttpsTrustManager.allowAllSSL();
        int MY_SOCKET_TIMEOUT_MS=5000000;
        rQueue = Volley.newRequestQueue(login.this);
        rQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       // MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

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

    /*********************************************************************************************/
    // EMAIL INPUT FIELD VALIDATION
    // Throws error if email address field is empty
    // Throws error if email format is incorrect

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String Email = etEmailAddress.getText().toString().trim();

        if (Email.isEmpty() || !isValidEmail(Email)){

            username.setError(getString(R.string.emailErr));
            username.requestFocus();
            return false;

        }else{
            username.setErrorEnabled(false);
        }
        return true;
    }

    /**********************************************************************************************/
    // PASSWORD INPUT FIELD VALIDATION
    // Throws error if password field is empty
    // Throws error if password is incorrect

    private boolean emptyPassword(EditText password){
        String passwd = password.getText().toString().trim();
        return (passwd.isEmpty());
    }

    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty()){

            password.setError(getString(R.string.passwordErr));
            password.requestFocus();
            return false;

        }else{
            password.setErrorEnabled(false);
        }
        return true;
    }

}