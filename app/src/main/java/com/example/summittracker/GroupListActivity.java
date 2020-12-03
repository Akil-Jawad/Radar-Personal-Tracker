package com.example.summittracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import AppConstant.CustomGroupListAdapter;
import AppConstant.GlobalConstant;
import Model.GroupListModel;

import static AppConstant.GlobalConstant.getgNameArrayList;
import static AppConstant.GlobalConstant.modelGroup;
import static AppConstant.GlobalConstant.modelSupervisor;

public class GroupListActivity extends AppCompatActivity {
    ListView groupListview;
    String group_name ;
    ProgressBar progressBar;
    SearchView searchView;

    int group_id,progressStatus=0;
    int count=0;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        searchView = findViewById(R.id.groupSearchView);
        count=GlobalConstant.getgNameArrayList().size();
        setTitle("Group List");
        groupListview = findViewById(R.id.groupListview);
        progressBar = findViewById(R.id.progressBar_cyclic);

        final List<GroupListModel> group_name_list = getGroupList();
        //Log.i("List",group_name_list.toString());
        final CustomGroupListAdapter adapter = new CustomGroupListAdapter(this,group_name_list);
        groupListview.setAdapter(adapter);
        groupListview.setTextFilterEnabled(true);
        groupListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListModel model = group_name_list.get(position);

                if(model.isChecked()){
                    model.setChecked(false);
                    count--;
                    setTitle("Select Group ("+count+")");
                    for (int i = 0; i < GlobalConstant.getgNameArrayList().size(); i++) {
                        GroupListModel checkObjects = GlobalConstant.getgNameArrayList().get(i);
                        if(model.getId()==checkObjects.getId()){
                            GlobalConstant.getgNameArrayList().remove(checkObjects);
                            Log.i("Stopp", String.valueOf(getgNameArrayList()));
                        }
                    }

                }else{
                    model.setChecked(true);
                    count++;
                    setTitle("Select Group ("+count+")");
                    GlobalConstant.getgNameArrayList().add(model);
                    Log.i("Stopp", String.valueOf(getgNameArrayList()));

                }
                group_name_list.set(position,model);

                adapter.updateRecords(group_name_list);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    progressStatus++;
                    android.os.SystemClock.sleep(10);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            groupListview.setVisibility(View.GONE);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        groupListview.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        FloatingActionButton fab = findViewById(R.id.groupfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupListActivity.this,SignUpActivity.class);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    adapter.filter("");
                    groupListview.clearTextFilter();
                }else{
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }

    private List<GroupListModel> getGroupList() {
        final StringBuilder result = new StringBuilder();
        final List<GroupListModel> group_list = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final List<GroupListModel> localArray=new ArrayList<>(GlobalConstant.getgNameArrayList());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                GlobalConstant.getSelectGroupUrl(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response",response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                group_id = jsonObject.getInt("row_id");
                                group_name = jsonObject.getString("group_name");
                                boolean isChecked=false;
                                for (int j = 0; j < localArray.size(); j++) {
                                    GroupListModel checkObjects = localArray.get(j);
                                    if(group_id==checkObjects.getId()){
                                        isChecked=true;
                                        localArray.remove(j);
                                        Log.i("Check counter", group_name +"   "+ localArray.size());
                                    }
                                }
                                group_list.add(new GroupListModel(group_id,group_name,isChecked));
                                modelGroup.add(new GroupListModel(group_id,group_name,isChecked));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.append("ERROR.............");
                    }
                });

        requestQueue.add(jsonArrayRequest);
        Log.i("return",group_list.toString());
        return group_list;
    }
    @Override
    protected void onStop() {
        super.onStop();
        modelGroup.clear();
        Log.i("Stopp","Called");
        Intent intent = new Intent(GroupListActivity.this,SignUpActivity.class);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back Press disabled.",Toast.LENGTH_SHORT).show();
    }
}
