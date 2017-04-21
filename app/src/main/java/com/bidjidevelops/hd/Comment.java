package com.bidjidevelops.hd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bidjidevelops.hd.Adapter.AdapterComment;
import com.bidjidevelops.hd.Gson.GsonComment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class Comment extends AppCompatActivity {
    String id_pertanyaan, susername, simage_user, ssekolah, swaktuSoal, sgbr_pertanyaan, spertanyaan, sidpertanyaan, sid_user;
    GsonComment gsonComment;
    @BindView(R.id.imguser)
    CircleImageView imguser;
    @BindView(R.id.txtUser)
    TextView txtUser;
    @BindView(R.id.txtTanggal)
    TextView txtTanggal;
    @BindView(R.id.txtTingkat)
    TextView txtTingkat;
    public AQuery aQuery;
    @BindView(R.id.txt_feed)
    TextView txtFeed;
    @BindView(R.id.img_content)
    ImageView imgContent;
    @BindView(R.id.edJawabSoal)
    EditText edJawabSoal;
    @BindView(R.id.btnjawab)
    Button btnjawab;
    @BindView(R.id.cardview)
    CardView cardview;
    @BindView(R.id.rcComentSoal)
    RecyclerView rcComentSoal;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    public List<GsonComment.Commentar> DataComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        LinearLayoutManager linearmanager = new LinearLayoutManager(Comment.this);
        rcComentSoal.setLayoutManager(linearmanager);
        id_pertanyaan = getIntent().getStringExtra("id_pertanyaan");
        susername = getIntent().getStringExtra("username");
        ssekolah = getIntent().getStringExtra("sekolah");
        spertanyaan = getIntent().getStringExtra("pertanyaan");
        simage_user = getIntent().getStringExtra("image_user");
        sidpertanyaan = getIntent().getStringExtra("idpertanyaan");
        sgbr_pertanyaan = getIntent().getStringExtra("gbr_pertanyaan");
        sid_user = getIntent().getStringExtra("id_user");
        swaktuSoal = getIntent().getStringExtra("waktuSoal");
        aQuery = new AQuery(getApplicationContext());
        requestQueue = Volley.newRequestQueue(Comment.this);
        btnjawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertcomment();
            }
        });
        getcomment();

        settextandimage();
    }

    public void getcomment() {
        String url = Helper.BASE_URL + "getcomment.php";


        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    gsonComment = gson.fromJson(response, GsonComment.class);
                    AdapterComment adapter = new AdapterComment(Comment.this, gsonComment.DataComment);
                    rcComentSoal.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Maaf Internet Lambat", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramComment = new HashMap<>();
                paramComment.put("id_pertanyaan", id_pertanyaan);
                return paramComment;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void settextandimage() {
        txtUser.setText(susername);
        txtFeed.setText(spertanyaan);
        txtTanggal.setText(swaktuSoal);
        txtTingkat.setText(ssekolah);
        Glide.with(getApplicationContext())
                .load(Helper.BASE_IMGUS + simage_user)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imguser);
        Glide.with(getApplicationContext())
                .load(Helper.BASE_IMGUS + sgbr_pertanyaan)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgContent);
    }

    public void addcomment() {
        String url = Helper.BASE_URL + "upcomment.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");
                                /*jika result adalah benar, maka pindah ke activity login dan menampilkan pesan dari server,
                                serta mematikan activity*/
                    if (result.equalsIgnoreCase("false")) {
                        Toast.makeText(Comment.this, "di tambah", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Comment.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramsendcomment = new HashMap<>();
                paramsendcomment.put("idpertanyaan", sidpertanyaan);
                paramsendcomment.put("id_user", sid_user);
                paramsendcomment.put("commentar", edJawabSoal.getText().toString());

                return paramsendcomment;
            }
        };

    }

    public void insertcomment() {
        String URL = Helper.BASE_URL + "upcomment.php";
        if (edJawabSoal.getText().toString().equals(null) || edJawabSoal.getText().toString().equals("") || edJawabSoal.getText().toString().equals(" ")) {
            edJawabSoal.setError("tidak boleh kosong");
        } else {
            Map<String, String> paramsendcomment = new HashMap<>();
            paramsendcomment.put("idpertanyaan", sidpertanyaan);
            paramsendcomment.put("id_user", sid_user);

            paramsendcomment.put("commentar", edJawabSoal.getText().toString());


            /*menampilkan progressbar saat mengirim data*/
            ProgressDialog pd = new ProgressDialog(getApplicationContext());
//        pd.setIndeterminate(true);
//        pd.setCancelable(false);
//        pd.setInverseBackgroundForced(false);
//        pd.setCanceledOnTouchOutside(false);
//        pd.setTitle("Info");
//        pd.setMessage("Sedang menambah data");
//        pd.show();

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
                                    edJawabSoal.setText("");
                                    getcomment();
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
