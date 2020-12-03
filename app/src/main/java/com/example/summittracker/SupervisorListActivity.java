package com.example.summittracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import AppConstant.CustomSupervisorListAdapter;
import AppConstant.GlobalConstant;
import Model.SupervisorNameModel;

import static AppConstant.GlobalConstant.getsNameArrayList;
import static AppConstant.GlobalConstant.modelSupervisor;

public class SupervisorListActivity extends AppCompatActivity {

    ListView supervisorListview;
    String name, phoneNumber,email ;
    ProgressBar progressBar;
    SearchView searchView;
    int supervisor_id,progressStatus=0;
    int count=0;
    private boolean supervisorFlag;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_list);
        searchView = findViewById(R.id.supervisorSearchView);
        Intent intent = getIntent();
        supervisorFlag = intent.getBooleanExtra("flag_for_no_sup",false);
        count=GlobalConstant.getsNameArrayList().size();
        supervisorListview = findViewById(R.id.supervisorListview);
        progressBar = findViewById(R.id.progressBar_cyclic);
        setTitle("Select Supervisor ("+count+"/1)");
        final List<SupervisorNameModel> supervisors_list = getSupervisorList();

        final CustomSupervisorListAdapter adapter = new CustomSupervisorListAdapter(this,supervisors_list);
        supervisorListview.setAdapter(adapter);


        supervisorListview.setTextFilterEnabled(true);
        supervisorListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SupervisorNameModel model = supervisors_list.get(position);

                if(model.isChecked()){
                    model.setChecked(false);
                    count--;
                    setTitle("Select Supervisor ("+count+"/1)");
                    for (int i = 0; i < GlobalConstant.getsNameArrayList().size(); i++) {
                        SupervisorNameModel checkObjects = GlobalConstant.getsNameArrayList().get(i);
                        if(model.getId()==checkObjects.getId()){
                            GlobalConstant.getsNameArrayList().remove(checkObjects);
                            Log.i("ArrayList",getsNameArrayList().toString());

                        }
                    }

                }else{
                    if(count>=1){
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.cls),"You can select only 1 supervisor",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        //Toast.makeText(SupervisorListActivity.this,"You can select only 1 supervisor", Toast.LENGTH_LONG).show();
                    }else{
                        model.setChecked(true);
                        count++;
                        setTitle("Select Supervisor ("+count+"/1)");
                        GlobalConstant.getsNameArrayList().add(model);

                    }

                }
                supervisors_list.set(position,model);

                adapter.updateRecords(supervisors_list);
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
                            supervisorListview.setVisibility(View.GONE);

                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        supervisorListview.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        FloatingActionButton fab = findViewById(R.id.supervisorfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(supervisorFlag){
                    Intent intent = new Intent(SupervisorListActivity.this,LoginActivity.class);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    Intent intent = new Intent(SupervisorListActivity.this,SignUpActivity.class);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

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
                    supervisorListview.clearTextFilter();
                }else{
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }

    private List<SupervisorNameModel> getSupervisorList() {
        final StringBuilder result = new StringBuilder();
        final List<SupervisorNameModel> supervisor_list = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final List<SupervisorNameModel> localArray=new ArrayList<>(GlobalConstant.getsNameArrayList());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                GlobalConstant.getSelectSupervisorUrl(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response",response.toString());
                            try {
                                for (int i = 0; i < response.length(); i++){
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    supervisor_id = jsonObject.getInt("id");
                                    name = jsonObject.getString("user_name");
                                    phoneNumber = jsonObject.getString("phone");
                                    email = jsonObject.getString("email");
                                    boolean isChecked=false;
                                    for (int j = 0; j < localArray.size(); j++) {
                                        SupervisorNameModel checkObjects = localArray.get(j);
                                        if(supervisor_id==checkObjects.getId()){
                                            isChecked=true;
                                            localArray.remove(j);
                                            Log.i("Check counter", name +"   "+ localArray.size());
                                        }
                                    }
                                    supervisor_list.add(new SupervisorNameModel(supervisor_id,name,phoneNumber,email,isChecked));
                                    modelSupervisor.add(new SupervisorNameModel(supervisor_id,name,phoneNumber,email,isChecked));
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
        Log.i("model",supervisor_list.toString());
        Log.i("return",supervisor_list.toString());
        return supervisor_list;
    }

    @Override
    protected void onStop() {
        super.onStop();
        modelSupervisor.clear();
        if(supervisorFlag){
            Intent intent = new Intent(SupervisorListActivity.this,LoginActivity.class);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }else{
            Intent intent = new Intent(SupervisorListActivity.this,SignUpActivity.class);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back Press disabled.",Toast.LENGTH_SHORT).show();
    }
}
