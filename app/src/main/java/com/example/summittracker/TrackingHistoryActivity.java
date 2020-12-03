package com.example.summittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.summittracker.map.activity.GroupViewMapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import AppConstant.AsyncResponse;
import AppConstant.CustomHistoryAdapter;
import DBOperations.HistoryDatabase;
import Model.HistoryModel;
import Model.UserLocationModel;

import static AppConstant.GlobalConstant.getHistoryInMap;
import static AppConstant.GlobalConstant.getHistoryModels;
import static AppConstant.GlobalConstant.historyWithDate;
import static AppConstant.GlobalConstant.setHistoryModels;
import static AppConstant.GlobalConstant.userFullData;

public class TrackingHistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView histlistview;
    private ProgressBar progressBar;
    private int option_id = -1;
    private Spinner pickSearchElement;
    private EditText etDateTime;
    private ImageButton ibDateTime;
    private LinearLayout llayout;
    ArrayList<String> listHeader = new ArrayList<>();
    ArrayList<String> headerWithTime = new ArrayList<>();
    private final Handler handler = new Handler();
    private boolean coordinator_flag = false;
    private int coordinator_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_history);
        histlistview = findViewById(R.id.histList);
        progressBar = findViewById(R.id.progressBar);
        pickSearchElement = findViewById(R.id.sp_search);
        etDateTime = findViewById(R.id.et_history);
        ibDateTime = findViewById(R.id.date_time);
        llayout = findViewById(R.id.llayout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickSearchElement.setAdapter(adapter);
        histlistview.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        coordinator_flag = intent.getBooleanExtra("get_coordinator_tracking_tistory",false);
        coordinator_id = intent.getIntExtra("coordinator_id",-1);

        prepareHistoryList();

        ibDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });
        pickSearchElement.setOnItemSelectedListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CustomHistoryAdapter listAdapter = new CustomHistoryAdapter(TrackingHistoryActivity.this,listHeader);
                histlistview.setAdapter(listAdapter);
                progressBar.setVisibility(View.GONE);
                histlistview.setVisibility(View.VISIBLE);
                histlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(TrackingHistoryActivity.this, GroupViewMapsActivity.class);
                        intent.putExtra("history_flag",true);
                        intent.putExtra("header",listHeader.get(i));
                        Log.i("HISTORY",listHeader.get(i));
                        startActivity(intent);
                    }
                });
            }
        },3000);

    }

    private void pickTime() {
        if(option_id==0){

        }else if(option_id==1){

        }else if(option_id==2){
            MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
            pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                    Toast.makeText(TrackingHistoryActivity.this, year + "-" + month, Toast.LENGTH_SHORT).show();
                }
            });
            pickerDialog.show(getSupportFragmentManager(), "com.example.summittracker.MonthYearPickerDialog");
        }else if(option_id==3){

        }

    }

    private void prepareHistoryList() {
        final List<HistoryModel> eachHistoryByDate = new ArrayList<>();
        HistoryDatabase historyDatabase = new HistoryDatabase(new AsyncResponse() {
            @Override
            public void processFinish(String result) {
                try {
                    int flag = 0;
                    List<UserLocationModel> locationList;
                    JSONArray jsonArray = new JSONArray(result);
                    Log.i("HISTORY1", String.valueOf(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        UserLocationModel locationModel = new UserLocationModel(0,0.0,0.0,null);
                        HistoryModel historyListMap = new HistoryModel(null,null);
                        String full_Date = jsonObject.getString("date_time");
                        String[] header_date = full_Date.split(" ");
                        int user_id = jsonObject.getInt("user_row_id");
                        double lat = jsonObject.getDouble("lat");
                        double lon = jsonObject.getDouble("lon");
                        String geo = jsonObject.getString("geolocation");
                        locationModel.setUser_id(user_id);
                        locationModel.setLatitude(lat);
                        locationModel.setLongitude(lon);
                        locationModel.setGeolocation(geo);
                        if(eachHistoryByDate.isEmpty()){
                            locationList = new ArrayList<>();
                            locationList.add(locationModel);
                            historyListMap.setList(locationList);
                            historyListMap.setDate(full_Date);
                            eachHistoryByDate.add(historyListMap);
                            listHeader.add(header_date[0]);
                            headerWithTime.add(full_Date);
                            Log.i("HISTORY2",header_date[0]+" "+header_date[1]);
                        }else{
                            for (int j = 0; j < eachHistoryByDate.size(); j++) {
                                String[] temp_date = eachHistoryByDate.get(j).getDate().split(" ");
                                if(header_date[0].contentEquals(temp_date[0])){
                                    eachHistoryByDate.get(j).getList().add(locationModel);
                                    Log.i("HISTORY3","if called "+header_date[0]+" "+header_date[1]);
                                    flag = 0;
                                    break;
                                }else{
                                    flag = 1;
                                }

                            }
                            if(flag == 1){
                                locationList = new ArrayList<>();
                                historyListMap = new HistoryModel();
                                locationList.add(locationModel);
                                historyListMap.setList(locationList);
                                historyListMap.setDate(full_Date);
                                eachHistoryByDate.add(historyListMap);
                                listHeader.add(header_date[0]);
                                headerWithTime.add(full_Date);
                                flag = 0;
                                Log.i("HISTORY4", header_date[0] + " " + header_date[1]);
                                Log.i("HISTORY4", String.valueOf(locationList.get(0).getLatitude()));
                            }
                        }
                    }
                    setHistoryModels(eachHistoryByDate);

//                    for (int i = 0; i < getHistoryModels().size(); i++) {
//                        for (int j = 0; j < getHistoryModels().get(i).getList().size(); j++) {
//                            Log.i("HISTORY"+i,getHistoryModels().get(i).getList().get(j).getLatitude()
//                                    +" "+getHistoryModels().get(i).getList().get(j).getLongitude());
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        if(coordinator_flag && coordinator_id>-1){
            historyDatabase.execute(coordinator_id);
        }else{
            historyDatabase.execute(userFullData.getRow_id());
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Toast.makeText(this, adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                option_id = 0;
                break;
            case 1:
                Toast.makeText(this,adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                option_id = 1;
                llayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                Toast.makeText(this,adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                option_id = 2;
                llayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                Toast.makeText(this,adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                option_id = 3;
                llayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
