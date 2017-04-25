package com.bidjidevelops.hd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineMain extends Fragment {


    @BindView(R.id.rcSoal)
    RecyclerView rcSoal;
    SwipeRefreshLayout refresh;
    Unbinder unbinder;
    ArrayList<muser> data;
     RequestQueue requestQueue;
    SessionManager sessionManager;
    StringRequest stringRequest;
    String Spassword, Semail, Remail, userImager;
    GsonTL gsonTL;
    RecyclerView rc;
    public TimelineMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_main, container, false);
        rc = (RecyclerView)view.findViewById(R.id.rcSoal);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        LinearLayoutManager linearmanager = new LinearLayoutManager(getActivity());
        rc.setLayoutManager(linearmanager);
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        Semail = user.get(SessionManager.kunci_email);
        data = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());
        Spassword = user.get(SessionManager.kunci_password);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                getSoal();
                getdata();
            }
//
        });
        getdata();
        getSoal();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void getSoal() {
        data.clear();
        String url = Helper.BASE_URL + "getsoalall.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    gsonTL = gson.fromJson(response, GsonTL.class);
                    AdapterTL adapter = new AdapterTL(getActivity(),gsonTL.dataSoal);
                    rcSoal.setAdapter(adapter);
                }
                catch (Exception i){
                    Toast.makeText(getActivity(), "sad", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Maaf Internet Lambat", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    public void getdata() {

        data.clear();
        String url = Helper.BASE_URL + "login.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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

                        }
                    } else {
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "error parsing data", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

}
