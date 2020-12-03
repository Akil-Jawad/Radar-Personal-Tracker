package com.example.summittracker;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import AppConstant.AsyncResponse;
import AppConstant.ExpandableListAdapter;
import AppConstant.LocationGetter;
import DBOperations.NavSubCenterLocation;
import Model.SubCenterList;
import Model.UserLocationModel;

import static AppConstant.GlobalConstant.subCenterLists;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationViewFragment extends Fragment {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expNavListView;
    private Button searchNavBtn;
    ProgressDialog dialog;
    private List<String> listNavHeader;
    private HashMap<String,List<String>> listNavChildOption;
    private final Handler handler = new Handler();
    public NavigationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        expNavListView = view.findViewById(R.id.navExp);
        searchNavBtn = view.findViewById(R.id.search_nav_btn);
        listNavHeader = new ArrayList<>();
        listNavChildOption = new HashMap<>();
        dialog = new ProgressDialog(getActivity());

        searchNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Getting Subcenter List");
                dialog.setTitle("Please Wait ...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                subCenterLists.clear();
                listNavHeader.clear();
                listNavChildOption.clear();
                expNavListView.setVisibility(View.GONE);
                final UserLocationModel user_location= LocationGetter.requestLocationUpdates(getActivity());
                if(user_location!=null){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NavSubCenterLocation getNavData = new NavSubCenterLocation(new AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        JSONObject jsnObj = jsonArray.getJSONObject(0);
                                        if(jsnObj.has("message")){
                                            String message = jsnObj.getString("message");
                                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                        }else{
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                SubCenterList subCenter = new SubCenterList(0,null,0.0,0.0,0.0);
                                                subCenter.setRow_id(jsonObject.getInt("row_id"));
                                                subCenter.setSubcenter_name(jsonObject.getString("subcenter_name"));
                                                subCenter.setLatitude(jsonObject.getDouble("latitude"));
                                                subCenter.setLongitude(jsonObject.getDouble("longitude"));
                                                subCenter.setDistance(jsonObject.getDouble("distance"));
                                                subCenterLists.add(subCenter);
                                            }
                                            prepareNavListData(subCenterLists);

                                            listAdapter = new ExpandableListAdapter(container.getContext(), listNavHeader, listNavChildOption);
                                            expNavListView.setAdapter(listAdapter);
                                            expNavListView.setVisibility(View.VISIBLE);
                                            dialog.dismiss();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            getNavData.execute(user_location);
                        }
                    }, 2000);
                    //expNavListView.setVisibility(View.VISIBLE);
                }
            }
        });
        expNavListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expNavListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expNavListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expNavListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String label = subCenterLists.get(groupPosition).getSubcenter_name();
                String uriBegin = "geo:"+subCenterLists.get(groupPosition).getLatitude()+","+subCenterLists.get(groupPosition).getLongitude();
                String query = subCenterLists.get(groupPosition).getLatitude()+","+subCenterLists.get(groupPosition).getLongitude()+"(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                getActivity().startActivity(intent);
                return false;
            }
        });

        return view;
    }

    private void prepareNavListData(List<SubCenterList> list) {
        Collections.sort(list, new DistanceComparator());
        List<String> options = new ArrayList<>();
        //options.add("View details");
        options.add("Show on map");
        // Adding child data
        for (int i = 0; i < list.size(); i++) {
            listNavHeader.add(subCenterLists.get(i).getSubcenter_name());
            listNavChildOption.put(listNavHeader.get(i),options);
            Log.i("subcenter",subCenterLists.get(i).getSubcenter_name());
        }
        Log.i("Group",listNavHeader.toString());
        Log.i("Child",listNavChildOption.toString());
    }

    class DistanceComparator implements Comparator<SubCenterList>{

        @Override
        public int compare(SubCenterList o1, SubCenterList o2) {
            if(o1.getDistance()>o2.getDistance()){
                return 1;
            }else{
                return -1;
            }
        }
    }

}
