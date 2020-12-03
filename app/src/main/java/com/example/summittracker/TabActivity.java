package com.example.summittracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;


import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import AppConstant.SectionsPagerAdapter;

import static AppConstant.GlobalConstant.getGrpMembrsDetail;
import static androidx.core.content.ContextCompat.getSystemService;

public class TabActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private static final String CHANNEL_ID = "channel_02";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ViewPager viewPager = findViewById(R.id.view_pager);
        createViewPager(viewPager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        //Log.i("LINE", String.valueOf(mapGroups.get(userFullData.getUser_groups().get(0).getGroup_row_id())));


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            // Set the Notification Channel for the Notification Manager.
            NotificationManager mNotificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    private void createViewPager(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new HomeFragment(),"HOME");
        //sectionsPagerAdapter.addFragment(new GroupViewFragment(),"GROUP VIEW");
        sectionsPagerAdapter.addFragment(new NavigationViewFragment(),"NAVIGATION TO \nNEAREST SC");
        sectionsPagerAdapter.addFragment(new HelplineFragment(),"HELPLINE");
        sectionsPagerAdapter.addFragment(new AboutFragment(),"ABOUT");
        viewPager.setAdapter(sectionsPagerAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Destroy tab","Called");
        getGrpMembrsDetail().clear();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);    }
}