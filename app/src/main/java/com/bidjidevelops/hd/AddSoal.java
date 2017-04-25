package com.bidjidevelops.hd;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bidjidevelops.hd.gambar.Pref;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddSoal extends AppCompatActivity implements View.OnClickListener {
    ArrayList<muser> data;
    String Spassword, Semail;
    SessionManager sessionManager;
    AQuery aQuery;
    String id;
    Pref pref;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.buttonChoose)
    Button buttonChoose;
    @BindView(R.id.buttonUpload)
    Button buttonUpload;
    @BindView(R.id.buttonUploadwithimg)
    Button buttonUploadwithimg;

//start otak atik add soal
    //Declaring views

    private RequestQueue requestQueue;
    private StringRequest stringRequest;


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
        setContentView(R.layout.activity_add_soal);
        ButterKnife.bind(this);
        pref = new Pref(this);
        sessionManager = new SessionManager(getApplicationContext());
//        sessionManager.checkupload();

        HashMap<String, String> user = sessionManager.getUserDetails();
        Semail = user.get(SessionManager.kunci_email);
        Spassword = user.get(SessionManager.kunci_password);
        //Requesting storage permission
        requestStoragePermission();
        data = new ArrayList<>();
        aQuery = new AQuery(this);
        requestQueue = Volley.newRequestQueue(AddSoal.this);
        getiduser();
        //Initializing views
        Semail = user.get(SessionManager.kunci_email);
        Spassword = user.get(SessionManager.kunci_password);
        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonUploadwithimg.setOnClickListener(this);

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
            new MultipartUploadRequest(this, uploadId, Helper.BASE_URL + "prosesquest.php")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("id_user", id) //Adding text parameter to the request
                    .addParameter("pertanyaan", editText.getText().toString()) //Adding text parameter to the request
                    //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the Upload

            startActivity(new Intent(getApplicationContext(), MainActivity.class));

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


    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
            buttonUpload.setVisibility(View.GONE);
            buttonUploadwithimg.setVisibility(View.VISIBLE);
        }
        if (v == buttonUploadwithimg) {
            uploadMultipart();
//            sendtanya();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        if (v == buttonUpload){
            insertSoalnoImage();
        }

    }

    public void getiduser() {
        data.clear();
        String url = Helper.BASE_URL + "login.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(AddSoal.this, response, Toast.LENGTH_SHORT).show();
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("result");
                    String pesan = json.getString("msg");
                    if (result.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = json.getJSONArray("user");
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject object = jsonArray.getJSONObject(a);
                            muser d = new muser();
                            String email, username;

                            d.setId_user(object.getString("iduser"));
                            d.setEmail(object.getString("email"));
                            d.setPassword(object.getString("password"));
                            d.setSekolah(object.getString("School"));
                            d.setUsername(object.getString("Username"));
                            d.setUserimage(object.getString("Image"));
                            id = object.getString("iduser");

                            data.add(d);
                            //Toast.makeText(MainActivity.this, userImager, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error parsing data", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddSoal.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramuserlogin = new HashMap<>();
                paramuserlogin.put("email", Semail);
                paramuserlogin.put("password", Spassword);
                return paramuserlogin;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void insertSoalnoImage() {
        String URL = Helper.BASE_URL + "prosesquestnoimg.php";
        if (editText.getText().toString().equals(null) || editText.getText().toString().equals("") || editText.getText().toString().equals(" ")) {
            editText.setError("tidak boleh kosong");
        } else {
            Map<String, String> paramsendcomment = new HashMap<>();
            paramsendcomment.put("pertanyaan",editText.getText().toString());
            paramsendcomment.put("id_user", id);

            paramsendcomment.put("image"," ");


            /*menampilkan progressbar saat mengirim data*/
            ProgressDialog pd = new ProgressDialog(getApplicationContext());
            try {
                /*format ambil data*/
                aQuery.progress(pd).ajax(URL, paramsendcomment, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object, AjaxStatus status) {
                        /*jika objek tidak kosong*/
                        if (object != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(object);
                                String result = jsonObject.getString("result");
                                String msg = jsonObject.getString("msg");
//                                Toast.makeText(AddSoal.this, result, Toast.LENGTH_SHORT).show();
                                /*jika result adalah benar, maka pindah ke activity login dan menampilkan pesan dari server,
                                serta mematikan activity*/
                                if (result.equalsIgnoreCase("true")) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                Helper.pesan(getApplicationContext(), msg);
                                } else {
                                    Helper.pesan(getApplicationContext(), msg);
                                }

                            } catch (JSONException e) {
                                Helper.pesan(getApplicationContext(), "Error convert data json");
                            }
                        }
                    }
                });
            } catch (Exception e) {
                Helper.pesan(getApplicationContext(), "Gagal mengambil data");
            }
        }
    }
}