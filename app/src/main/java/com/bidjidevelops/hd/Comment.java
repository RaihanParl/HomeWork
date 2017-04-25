package com.bidjidevelops.hd;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bidjidevelops.hd.Adapter.AdapterComment;
import com.bidjidevelops.hd.Gson.GsonComment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;


public class Comment extends AppCompatActivity {
    String id_pertanyaan, susername, simage_user, ssekolah, swaktuSoal, sgbr_pertanyaan, spertanyaan, sidpertanyaan, sid_user, sid_usercom;
    GsonComment gsonComment;
    @BindView(R.id.txthapus)
    TextView txthapus;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private LayoutInflater inflater;
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
    SwipeRefreshLayout sr;
    SessionManager sessionManager;
    String Spassword, Semail, Remail, userImager;
    ArrayList<muser> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        LinearLayoutManager linearmanager = new LinearLayoutManager(Comment.this);
        rcComentSoal.setLayoutManager(linearmanager);
//        munculhapus();
        id_pertanyaan = getIntent().getStringExtra("id_pertanyaan");
        susername = getIntent().getStringExtra("username");
        ssekolah = getIntent().getStringExtra("sekolah");
        spertanyaan = getIntent().getStringExtra("pertanyaan");
        simage_user = getIntent().getStringExtra("image_user");
        sidpertanyaan = getIntent().getStringExtra("idpertanyaan");
        sid_usercom = getIntent().getStringExtra("id_user");
        sgbr_pertanyaan = getIntent().getStringExtra("gbr_pertanyaan");
        swaktuSoal = getIntent().getStringExtra("waktuSoal");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        Semail = user.get(SessionManager.kunci_email);
        Spassword = user.get(SessionManager.kunci_password);
        aQuery = new AQuery(getApplicationContext());
        requestQueue = Volley.newRequestQueue(Comment.this);
        data = new ArrayList<>();
        btnjawab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
                insertcomment();
            }
        });
        sr = (SwipeRefreshLayout) findViewById(R.id.refresh);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr.setRefreshing(false);
                getdata();
                getcomment();
            }
        });
        getdata();
        getcomment();
        settextandimage();
        getdata();

    }

    public void getcomment() {
        String url = Helper.BASE_URL + "getcomment.php";


        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (
                            String.valueOf(new JSONObject(response).getString("msg")).equals("Ada data")
                            ) {
                        try {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            gsonComment = gson.fromJson(response, GsonComment.class);
                            AdapterComment adapter = new AdapterComment(Comment.this, gsonComment.DataComment);
                            rcComentSoal.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                        Toast.makeText(Comment.this, "Belum ada komentar", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

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
                .into(imgContent);
    }

    public void insertcomment() {
        String URL = Helper.BASE_URL + "upcomment.php";
        if (edJawabSoal.getText().toString().equals(null) || edJawabSoal.getText().toString().equals("") || edJawabSoal.getText().toString().equals(" ")) {
            edJawabSoal.setError("tidak boleh kosong");
        } else {
            Map<String, String> paramsendcomment = new HashMap<>();
            getdata();
            paramsendcomment.put("idpertanyaan", sidpertanyaan);
            paramsendcomment.put("id_user", sid_user);
            Toast.makeText(this, sid_user, Toast.LENGTH_SHORT).show();
            paramsendcomment.put("commentar", edJawabSoal.getText().toString());
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

    public void getdata() {

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
                            String email, username;

                            d.setId_user(object.getString("iduser"));
                            d.setEmail(object.getString("email"));
                            d.setPassword(object.getString("password"));
                            d.setSekolah(object.getString("School"));
                            d.setUsername(object.getString("Username"));
                            d.setUserimage(object.getString("Image"));
                            email = object.getString("email");
//                                    school=object.getString("School");
                            username = object.getString("Username");
                            sid_user = object.getString("iduser");
                            userImager = object.getString("Image");
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
                Toast.makeText(Comment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void imgcontent(View v) {
        inflater = Comment.this.getLayoutInflater();
        View content = inflater.inflate(R.layout.imagview, null);
        PhotoView imgcontent;
        TextView txttest;
        AlertDialog.Builder builder = new AlertDialog.Builder(Comment.this);
        imgcontent = (PhotoView) content.findViewById(R.id.imgcontent);
//        Toast.makeText(this, sgbr_pertanyaan, Toast.LENGTH_SHORT).show();
        Glide.with(getApplicationContext())
                .load(Helper.BASE_IMGUS + sgbr_pertanyaan)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgcontent);
        builder.setView(content);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

}
