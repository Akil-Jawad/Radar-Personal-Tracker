package com.example.summittracker.map.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.summittracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AppConstant.AsyncResponse;
import DBOperations.GetAllGroupMembers;
import Model.HistoryModel;

import static AppConstant.GlobalConstant.getGrpMembrsDetail;
import static AppConstant.GlobalConstant.getHistoryInMap;
import static AppConstant.GlobalConstant.getHistoryModels;
import static AppConstant.GlobalConstant.getHistoryWithDate;
import static AppConstant.GlobalConstant.userFullData;

public class GroupViewMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button refreshBtn;
    private GoogleMap mMap;
    private boolean history_flag = false;
    private String header_name = "";
    HashMap<Integer,Marker> markers = new HashMap<>();
    private int group_id;
    private int count = 0;
    Handler handler = new Handler();
    Runnable timedTask = new Runnable() {
        @Override
        public void run() {
            Log.i("Again Running : ","run");
            trackGroupLocations();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view_maps);
        refreshBtn=findViewById(R.id.refreshButton);
        Intent intent = getIntent();
        history_flag = intent.getBooleanExtra("history_flag",false);
        header_name = intent.getStringExtra("header");
        group_id = intent.getIntExtra("group_row_id",-1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(timedTask);
                trackGroupLocations();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(history_flag){
            final List<HistoryModel> historyModel;
            historyModel = getHistoryModels();
            if (historyModel != null) {
                for (int i = 0; i < historyModel.size(); i++) {
                    String[] temp_datetime = historyModel.get(i).getDate().split(" ");
                    if(temp_datetime[0].contentEquals(header_name)){
                        for (int j = 0; j < historyModel.get(i).getList().size(); j++) {
                            final int finalJ = j;
                            final int finalI = i;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng myLocation = new LatLng(historyModel.get(finalI).getList().get(finalJ).getLatitude(),
                                            historyModel.get(finalI).getList().get(finalJ).getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(myLocation).title(historyModel.get(finalI).getDate()
                                            +" "+historyModel.get(finalI).getList().get(finalJ).getGeolocation()));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,17.0f));
                                }
                            },500);
                        }
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(),"No History",Toast.LENGTH_LONG).show();
            }
        }else {
            for (int i = 0; i < getGrpMembrsDetail().size(); i++) {
                final int finalI = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LatLng grpMembersLocation = new LatLng(getGrpMembrsDetail().get(finalI).getLat(), getGrpMembrsDetail().get(finalI).getLon());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(grpMembersLocation).title(getGrpMembrsDetail().get(finalI).getPhone()
                                +" "+ getGrpMembrsDetail().get(finalI).getGeolocation()));
                        markers.put(getGrpMembrsDetail().get(finalI).getRow_id(),marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(grpMembersLocation,10.5f));

//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,17.0f));
                    }
                },500);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    trackGroupLocations();
                }
            },10000);
        }
        // Add a marker in Sydney and move the camera

    }

    public void trackGroupLocations(){
        count++;
        Log.i("GroupView", String.valueOf(count));
        int group_row_id = userFullData.getUser_groups().get(group_id).getGroup_row_id();
        final GetAllGroupMembers getAllGroupMembers = new GetAllGroupMembers(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int row_id = jsonObject.getInt("row_id");
                        double latitude = jsonObject.getDouble("lat");
                        double longitude = jsonObject.getDouble("lon");
                        String geolocation = jsonObject.getString("geolocation");
                        String phone = jsonObject.getString("phone");
                        LatLng grpMembersLocation = new LatLng(latitude, longitude);
                        Marker marker = markers.get(row_id);
                        if(marker!=null){
                            marker.remove();
                        }
                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(grpMembersLocation).title(phone
                                +" "+ geolocation));
                        markers.put(row_id,newMarker);
//                        marker = mMap.addMarker(new MarkerOptions().position(myLocation).title(phone
//                                +" "+geolocation));
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,17.0f));
                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getAllGroupMembers.execute(group_row_id,userFullData.getRow_id());
        handler.postDelayed(timedTask,10000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getGrpMembrsDetail().clear();
        handler.removeCallbacks(timedTask);
    }


}
