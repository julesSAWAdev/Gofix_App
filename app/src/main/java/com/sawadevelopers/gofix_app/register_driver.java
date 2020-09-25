package com.sawadevelopers.gofix_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class register_driver extends AppCompatActivity implements View.OnClickListener {
    String emailStored,userStored;
    ProgressBar loader;
    private RequestQueue rQueue;
    TextView tvback,username;
    EditText drivername,address,idnumber,dlnumber,phone,category;
    ImageView IdProf;
    Button Upload_Btn,Register;

    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    String UPLOAD_URL = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_register_driver);


        //hooks
        tvback = findViewById(R.id.back);
        username = findViewById(R.id.tvUsername);
        drivername = findViewById(R.id.drivername);
        address = findViewById(R.id.address);
        idnumber = findViewById(R.id.idnumber);
        dlnumber = findViewById(R.id.dlnumber);
        phone = findViewById(R.id.phone);
        category = findViewById(R.id.category);
        IdProf = findViewById(R.id.IdProf);
        Upload_Btn = findViewById(R.id.UploadBtn);
        Register = findViewById(R.id.registerdriver);
        imageView = findViewById(R.id.IdProf);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.UploadBtn);
        buttonUpload = (Button) findViewById(R.id.registerdriver);
        //imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);





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
                Intent intent =  new Intent(register_driver.this,SpareServices.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void RegisterDriver(){

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registration in progress");
        progressDialog.show();

        final String driverName = this.drivername.getText().toString();
        final String Address = this.address.getText().toString();
        final String IDnumber = this.idnumber.getText().toString();
        final String DLnumber = this.dlnumber.getText().toString();
        final String Phone = this.phone.getText().toString();
        final String Category = this.category.getText().toString();




    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);
        System.out.println("path is : "+path);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            final String driverName = this.drivername.getText().toString();
            final String Address = this.address.getText().toString();
            final String IDnumber = this.idnumber.getText().toString();
            final String DLnumber = this.dlnumber.getText().toString();
            final String Phone = this.phone.getText().toString();
            final String Category = this.category.getText().toString();

            System.out.println("phone :" + Phone);

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", driverName) //Adding text parameter to the request
                    .addParameter("address", Address) //Adding text parameter to the request
                    .addParameter("idnumber", IDnumber) //Adding text parameter to the request
                    .addParameter("dlnumber", DLnumber) //Adding text parameter to the request
                    .addParameter("phone", Phone) //Adding text parameter to the request
                    .addParameter("category", Category) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

            System.out.println("path " +path);
            System.out.println("name " +name);
            System.out.println("URL " + Constants.UPLOAD_URL);


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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
    public void onClick(View v) {

            if (v == buttonChoose) {
                showFileChooser();
            }
            if (v == buttonUpload) {
                uploadMultipart();
            }
    }


}