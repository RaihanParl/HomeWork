package com.bidjidevelops.hd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bidjidevelops.hd.gambar.Upload;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    ArrayList<muser> data;
    private LayoutInflater inflater;

    @BindView(R.id.imgprof)
    CircleImageView imgprof;
    @BindView(R.id.txtnamanya)
    TextView txtnamanya;
    @BindView(R.id.txtEmailnya)
    TextView txtEmailnya;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtSekolahnya)
    TextView txtSekolahnya;
    String email, username, sekolah;
    String Spassword, Semail, Remail, userImager;
    public AQuery aQuery;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    public NavigationView navigationView;
    MainActivity mainActivity;
    SessionManager sessionManager;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        data = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        aQuery = new AQuery(getApplicationContext());
        Semail = user.get(SessionManager.kunci_email);
//        Toast.makeText(Profile.this, Semail, Toast.LENGTH_SHORT).show();
        Spassword = user.get(SessionManager.kunci_password);
        getdata();
    }

    public void getdata() {

        data.clear();
        String url = Helper.BASE_URL + "login.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("result");
                    String pesan = json.getString("msg");
                    if (result.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = json.getJSONArray("user");
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject object = jsonArray.getJSONObject(a);
                            muser d = new muser();

                            d.setId_user(object.getString("iduser"));
                            d.setEmail(object.getString("email"));
                            d.setPassword(object.getString("password"));
                            d.setSekolah(object.getString("School"));
                            d.setUsername(object.getString("Username"));
                            d.setUserimage(object.getString("Image"));
                            email = object.getString("email");
                            username = object.getString("Username");
                            id = object.getString("iduser");
                            userImager = object.getString("Image");
                            sekolah = object.getString("School");
                            data.add(d);

                            txtnamanya.setText(username);
                            txtEmailnya.setText(email);
                            txtSekolahnya.setText(sekolah);
                            //Toast.makeText(MainActivity.this, userImager, Toast.LENGTH_SHORT).show();
                            Glide.with(getApplicationContext()).load(Helper.BASE_IMGUS + userImager).placeholder(R.drawable.student).into(imgprof);
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
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void Edit(View v) {
        inflater = Profile.this.getLayoutInflater();
        View content = inflater.inflate(R.layout.pop_profile, null);
        final EditText edNama, edEmail, edSekolah;
        CircleImageView imgProfPo;
        Button btnImage;
        getdata();
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        edNama = (EditText) content.findViewById(R.id.ednamaProf);
        edEmail = (EditText) content.findViewById(R.id.edemailprof);
        edSekolah = (EditText) content.findViewById(R.id.edSekolahProf);
        imgProfPo = (CircleImageView) content.findViewById(R.id.imgprof);
        Glide.with(getApplicationContext()).load(Helper.BASE_IMGUS + userImager).placeholder(R.drawable.student).into(imgProfPo);
        builder.setView(content)
                .setTitle("Edit profile")
                .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String URL = Helper.BASE_URL + "editprofile.php";
                        Map<String, String> paramsendcomment = new HashMap<>();
                        paramsendcomment.put("email", edEmail.getText().toString());
                        paramsendcomment.put("username", edNama.getText().toString());
                        paramsendcomment.put("school", edSekolah.getText().toString());
                        paramsendcomment.put("school", edSekolah.getText().toString());
                        paramsendcomment.put("id", id);
                        sessionManager.createSession(edEmail.getText().toString(),Spassword);
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

                                /*jika result adalah benar, maka pindah ke activity login dan menampilkan pesan dari server,
                                serta mematikan activity*/
                                            if (result.equalsIgnoreCase("true")) {
//                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                                Helper.pesan(getApplicationContext(), msg);
                                                finish();
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
                });
        edEmail.setText(email);
        edNama.setText(username);
        edSekolah.setText(sekolah);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void Image(View v) {
      Intent i = new Intent(getApplicationContext(),Upload.class);
              i.putExtra("id_user",id);
        startActivity(i);

    }

}
