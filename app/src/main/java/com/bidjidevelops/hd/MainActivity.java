package com.bidjidevelops.hd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.bidjidevelops.hd.Adapter.AdapterTL;
import com.bidjidevelops.hd.Gson.GsonTL;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GsonTL gsonTL;
    ArrayList<muser> data;
    public TextView txtUsername, txtEmail;
    String Spassword, Semail, Remail, userImager;
    SessionManager sessionManager;
    public ImageView imguser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.material_design_floating_action_menu_item1)
    FloatingActionButton fabAddSoal;
    @BindView(R.id.material_design_floating_action_menu_item2)
    FloatingActionButton belum;
    @BindView(R.id.material_design_floating_action_menu_item3)
    FloatingActionButton Belum2;
    @BindView(R.id.fab)
    FloatingActionMenu fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rcSoal)
    RecyclerView rcSoal;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //COY LIAT GETSOAL() TRUS TANYA KAK MURSIT
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        data = new ArrayList<>();
        LinearLayoutManager linearmanager = new LinearLayoutManager(MainActivity.this);
        rcSoal.setLayoutManager(linearmanager);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View header = navigationView.getHeaderView(0);
        imguser = (ImageView) header.findViewById(R.id.imguser);
        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });
        txtUsername = (TextView) header.findViewById(R.id.txtusername);
        txtEmail = (TextView) header.findViewById(R.id.txtEmail);
        fabAddSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddSoal.class));
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        Semail = user.get(SessionManager.kunci_email);
        Spassword = user.get(SessionManager.kunci_password);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                getSoal();
                getdata();
            }

        });
        getdata();
        getSoal();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(getApplicationContext(), AddSoal.class));

        } else if (id == R.id.nav_send) {
            sessionManager.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                            userImager = object.getString("Image");
                            data.add(d);

                            txtUsername.setText(username);
                            txtEmail.setText(email);
                            //Toast.makeText(MainActivity.this, userImager, Toast.LENGTH_SHORT).show();
                            Glide.with(getApplicationContext()).load(Helper.BASE_IMGUS + userImager).placeholder(R.drawable.student).into(imguser);
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
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onclick() {

    }

    public void getSoal() {
        data.clear();
        String url = Helper.BASE_URL + "getsoalall.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    gsonTL = gson.fromJson(response, GsonTL.class);
                    AdapterTL adapter = new AdapterTL(MainActivity.this, gsonTL.dataSoal);
                    rcSoal.setAdapter(adapter);
                }
                catch (Exception i){
                    Toast.makeText(MainActivity.this, "sad", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Maaf Internet Lambat", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}


