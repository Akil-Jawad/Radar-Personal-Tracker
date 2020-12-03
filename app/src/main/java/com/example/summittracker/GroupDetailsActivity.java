package com.example.summittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import AppConstant.CustomGroupDetailsAdapter;

import static AppConstant.GlobalConstant.getGrpMembrsDetail;

public class GroupDetailsActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        listView = findViewById(R.id.lView);

        final CustomGroupDetailsAdapter adapter = new CustomGroupDetailsAdapter(this, getGrpMembrsDetail());
        listView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        getGrpMembrsDetail().clear();
    }
}
