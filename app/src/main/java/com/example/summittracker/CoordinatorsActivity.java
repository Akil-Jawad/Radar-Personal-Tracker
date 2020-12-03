package com.example.summittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AppConstant.AsyncResponse;
import AppConstant.ExpandableListAdapter;
import DBOperations.UpdateSupervisor;

import static AppConstant.GlobalConstant.getCoordinatorList;
import static AppConstant.GlobalConstant.getSearchCoordinatorList;

public class CoordinatorsActivity extends AppCompatActivity {
    private androidx.constraintlayout.widget.ConstraintLayout constraintLayout;
    private ExpandableListAdapter listAdapter;
    private SearchView coordinatorSearch;
    private ProgressBar progressBar;
    private ExpandableListView expListView;
    private List<String> listCoordHeader;
    private HashMap<String,List<String>> listCoordChildOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinators);
        constraintLayout = findViewById(R.id.asl);
        coordinatorSearch = findViewById(R.id.coordinatorSearchView);
        progressBar = findViewById(R.id.progressBar_coord_cyclic);
        expListView = findViewById(R.id.grpCordExp);
        getCoordList();
        //Log.i("Group",getCoordinatorList().get(0).getUser_name());

        listAdapter = new ExpandableListAdapter(this, listCoordHeader, listCoordChildOption);
        expListView.setAdapter(listAdapter);
        progressBar.setVisibility(View.GONE);
        expListView.setVisibility(View.VISIBLE);

        coordinatorSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    listAdapter.filter("");
                    expListView.clearTextFilter();
                }else{
                    listAdapter.filter(s);
                }
                return false;
            }
        });
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int groupPosition, int childPosition, long l) {
                if(childPosition==0){
                    String details = "Name :"+getSearchCoordinatorList().get(groupPosition).getUser_name()+"\nEmail :"
                            +getSearchCoordinatorList().get(groupPosition).getEmail()+"\nMobile Number :"
                            +getSearchCoordinatorList().get(groupPosition).getMobile_number()+"\nPresent Address :"
                            +getSearchCoordinatorList().get(groupPosition).getPresent_address();
                    new AlertDialog.Builder(CoordinatorsActivity.this)
                            .setTitle("Coordinator Details")
                            .setMessage(details)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                }else if(childPosition==1){
                    Intent intent = new Intent(CoordinatorsActivity.this,TrackingHistoryActivity.class);
                    intent.putExtra("get_coordinator_tracking_tistory",true);
                    intent.putExtra("coordinator_id",getSearchCoordinatorList().get(groupPosition).getRow_id());
                    startActivity(intent);

                } else if(childPosition==2){
                    String alert = "Are you sure you want to remove "+getSearchCoordinatorList().get(groupPosition).getUser_name()+
                            " from your coordinator list?";
                    new AlertDialog.Builder(CoordinatorsActivity.this)
                            .setTitle("Allert Message")
                            .setMessage(alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    updateCoordinator(groupPosition);
                                    Log.i("GroupPosition", String.valueOf(groupPosition));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                }
                return false;
            }
        });
    }

    private void updateCoordinator(final int _id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",getSearchCoordinatorList().get(_id).getRow_id());
            jsonObject.put("new_sup_id",0);
            UpdateSupervisor updateSupervisor = new UpdateSupervisor(new AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    getCoordinatorList().remove(getSearchCoordinatorList().get(_id));
                    getCoordList();
                    coordinatorSearch.setQuery("",false);
                    //listAdapter.notifyDataSetChanged();
                    listAdapter = new ExpandableListAdapter(CoordinatorsActivity.this, listCoordHeader, listCoordChildOption);
                    expListView.setAdapter(listAdapter);
                    //expListView.setAdapter(listAdapter);
                    Snackbar snackbar = Snackbar.make(constraintLayout,result,Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
            updateSupervisor.execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCoordList() {
        listCoordHeader = new ArrayList<>();
        listCoordChildOption = new HashMap<>();
        List<String> options = new ArrayList<>();
        options.add("View details");
        options.add("View tracking history");
        options.add("Remove coordinator");
        // Adding child data
        if(getCoordinatorList().isEmpty()){
            Toast.makeText(CoordinatorsActivity.this,"No Coordinators",Toast.LENGTH_LONG).show();
        }else{
            for (int i = 0; i < getCoordinatorList().size(); i++) {
                listCoordHeader.add(getCoordinatorList().get(i).getUser_name());
                listCoordChildOption.put(listCoordHeader.get(i), options);

            }
            Log.i("Group",listCoordHeader.toString());
            Log.i("Child",listCoordChildOption.toString());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listCoordHeader.clear();
        listCoordChildOption.clear();
    }
}
