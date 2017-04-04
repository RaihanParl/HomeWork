package com.bidjidevelops.carilawan.gambar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bidjidevelops.carilawan.BaseApp;
import com.bidjidevelops.carilawan.Helper;
import com.bidjidevelops.carilawan.MainActivity;
import com.bidjidevelops.carilawan.R;
import com.bidjidevelops.carilawan.SessionManager;
import com.bidjidevelops.carilawan.muser;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Upload extends AppCompatActivity implements View.OnClickListener {
    ArrayList<muser> data;
    String Spassword, Semail, Remail;
    SessionManager sessionManager;
    AQuery aQuery;
    String id;
    Pref pref;


    //Declaring views

    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    TextView txtlewat;
    private EditText editText;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        pref =  new Pref(this);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkupload();
        if (!pref.isFirstTimeLaunched()) {
            launchHome();
        }

        HashMap<String, String> user = sessionManager.getUserDetails();
        Semail = user.get(SessionManager.kunci_email);
        Spassword = user.get(SessionManager.kunci_password);
        //Requesting storage permission
        requestStoragePermission();
        data = new ArrayList<>();
        aQuery = new AQuery(this);
        getiduser();
        //Initializing views
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        txtlewat = (TextView) findViewById(R.id.txtlewat);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        txtlewat.setOnClickListener(this);
    }


    /*
    * This is the method responsible for image Upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {
        //getting name for the image
        //getting the actual path of the image
        String path = getPath(filePath);
        getiduser();
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Helper.BASE_URL + "Upload.php")
                    .addFileToUpload(path, "image") //Adding file
       .addParameter("id", id) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the Upload
            Toast.makeText(this, "berhasil menambah dan id == "+id, Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            ;
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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


    //Thi
    // s method will be called when the user will tap on allow or deny
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


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        if (v == txtlewat) {
            getiduser();
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        }

    }

    public void getiduser() {

        data.clear();
        String url = Helper.BASE_URL + "login.php";
        Map<String, String> parampa = new HashMap<>();
        parampa.put("email", Semail);
        parampa.put("password", Spassword);
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading...");
        try {
            aQuery.progress(progressDialog).ajax(url, parampa, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String hasil, AjaxStatus status) {
                    if (hasil != null) {

                        try {
//                            Toast.makeText(Upload.this, hasil, Toast.LENGTH_SHORT).show();
                            JSONObject json = new JSONObject(hasil);
                            String result = json.getString("result");
                            String pesan = json.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = json.getJSONArray("berita");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    muser d = new muser();
                                    d.setId_user(object.getString("id"));
                                    d.setEmail(object.getString("email"));
                                    d.setPassword(object.getString("password"));
                                    d.setSekolah(object.getString("School"));
                                    d.setUsername(object.getString("Username"));
                                    id = object.getString("id");
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error parsing data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error get data ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void launchHome() {
        pref.setFirstLaunched(false);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}


