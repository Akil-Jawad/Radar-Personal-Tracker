package com.example.summittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.summittracker.map.activity.GroupViewMapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AppConstant.AsyncResponse;
import AppConstant.ExpandableListAdapter;
import DBOperations.GetAllGroupMembers;
import Model.GetAllGroupMembersDetails;

import static AppConstant.GlobalConstant.getGrpMembrsDetail;
import static AppConstant.GlobalConstant.grpMembrsDetail;
import static AppConstant.GlobalConstant.userFullData;

public class ShowGroupInfoActivity extends AppCompatActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expGroupListView;
    private List<String> listGroupHeader;
    private HashMap<String,List<String>> listGroupChildOption;
    private int group_row_id;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information);
        expGroupListView = findViewById(R.id.grpExp);
        dialog = new ProgressDialog(ShowGroupInfoActivity.this);
        listGroupHeader = new ArrayList<>();
        listGroupChildOption = new HashMap<>();
        Log.i("SIZE", String.valueOf(listGroupHeader.size()));
        prepareListData();

        listAdapter = new ExpandableListAdapter(ShowGroupInfoActivity.this, listGroupHeader, listGroupChildOption);
        expGroupListView.setAdapter(listAdapter);

        expGroupListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expGroupListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expGroupListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                dialog.setMessage("Getting Group Members details");
                dialog.setTitle("Please Wait ...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                group_row_id = userFullData.getUser_groups().get(groupPosition).getGroup_row_id();
                final GetAllGroupMembers getAllGroupMembers = new GetAllGroupMembers(new AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GetAllGroupMembersDetails user_groups = new GetAllGroupMembersDetails(0,null,null,null,0.0,0.0,null);
                                user_groups.setRow_id(jsonObject.getInt("row_id"));
                                user_groups.setUser_name(jsonObject.getString("user_name"));
                                user_groups.setEmail(jsonObject.getString("email"));
                                user_groups.setPhone(jsonObject.getString("phone"));
                                user_groups.setLat(jsonObject.getDouble("lat"));
                                user_groups.setLon(jsonObject.getDouble("lon"));
                                user_groups.setGeolocation(jsonObject.getString("geolocation"));
                                getGrpMembrsDetail().add(user_groups);
                                Log.i("USER", String.valueOf(grpMembrsDetail.get(i).getRow_id()));
                            }
                            if(childPosition==0){
                                Intent intent = new Intent(ShowGroupInfoActivity.this, GroupDetailsActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }else{
                                Intent intent = new Intent(ShowGroupInfoActivity.this, GroupViewMapsActivity.class);
                                intent.putExtra("group_row_id",groupPosition);
                                startActivity(intent);
                                dialog.dismiss();
                            }


                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getAllGroupMembers.execute(group_row_id,userFullData.getRow_id());

                return false;
            }
        });
    }

    private void prepareListData() {

        // Adding child data
        for (int i = 0; i < userFullData.getUser_groups().size(); i++) {
            listGroupHeader.add(userFullData.getUser_groups().get(i).getGroup_name());
        }
        // Adding child data
        List<String> options = new ArrayList<>();
        options.add("View details");
        options.add("Show on map");
        Log.i("SIZE", String.valueOf(listGroupHeader.size()));

        for (int i = 0; i < listGroupHeader.size(); i++) {
            listGroupChildOption.put(listGroupHeader.get(i), options);
        }

        Log.i("Group",listGroupHeader.toString());
        Log.i("Child",listGroupChildOption.toString());

    }

}
