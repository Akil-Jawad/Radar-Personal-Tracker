package com.example.summittracker;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.summittracker.map.activity.MyLocationMapsActivity;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

import AppConstant.LocationGetter;
import DBOperations.SQLiteENumberList;
import DBOperations.SQLiteImageDB;
import Model.UserEmergencyNumber;
import Model.UserLocationModel;

import static AppConstant.GlobalConstant.coordinatorList;
import static AppConstant.GlobalConstant.geteNumberArrayList;
import static AppConstant.GlobalConstant.userFullData;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
//    private static final int RequestPermissionCode  = 1 ;
//    public  static final int RequestPermissionCodeForCall  = 2 ;

    SQLiteENumberList mysqlitedb;
    private static final String CHANNEL_ID = "channel_02";
    final Handler handler = new Handler();
    TabActivity tab;
    ProgressDialog dialog;
    SQLiteImageDB db;

    private String message="";
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        GridLayout mainGrid = view.findViewById(R.id.gridl);
        de.hdodenhof.circleimageview.CircleImageView proImage = view.findViewById(R.id.circleView);
        TextView userName = view.findViewById(R.id.user_name);
        TextView userMobile = view.findViewById(R.id.user_mobile);
        TextView userEmail = view.findViewById(R.id.user_email);
        dialog = new ProgressDialog(getActivity());
        //eNumberArrayList.add(new EmergencyNumberModel("Akil","01872796633",true));
        //eNumberArrayList.add(new EmergencyNumberModel("Shafayet","01868903697",true));
        userName.setText(userFullData.getUser_name());
        userMobile.setText(userFullData.getMobile_number());
        userEmail.setText(userFullData.getEmail());
        tab = (TabActivity) getActivity();
        db = new SQLiteImageDB(tab);
        Bitmap bitmap = db.getImage(userFullData.getRow_id());
        mysqlitedb = new SQLiteENumberList(getActivity());
        Log.i("Integer", String.valueOf(bitmap));
        if(bitmap!=null){
            Bitmap bit = Bitmap.createScaledBitmap(bitmap,150,150,true);
            proImage.setImageBitmap(bit);
        }
        setEvents(mainGrid);

        return view;
    }

    private void setEvents(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalI) {
                        case 0:
                            startLocationUpdateService();
                            break;
                        case 1:
                            showGroupInformation();
                            break;
                        case 2:
                            showCoordinator();
                            break;
                        case 3:
                            showTrackingHistory();
                            break;
                        case 4:
                            sendMessage();
                            break;
                        case 5:
                            showMyLocation();
                            break;
                        case 6:
                            showUserProfile();
                            break;
                        case 7:
                            userLogout();
                            break;
                    }
                }
            });
        }
    }

    private void showGroupInformation() {
        Intent intent = new Intent(getActivity(), ShowGroupInfoActivity.class);
        startActivity(intent);
    }


    private void startLocationUpdateService() {
        //Toast.makeText(getContext(), "Start Service", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TrackingActivity.class);
        startActivity(intent);
    }

    private void showTrackingHistory() {
        //Toast.makeText(getContext(), "Tracking History", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TrackingHistoryActivity.class);
        startActivity(intent);
    }

    private void sendMessage() {
        message = "";
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)== PermissionChecker.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)== PermissionChecker.PERMISSION_GRANTED){
            final UserLocationModel user_location= LocationGetter.requestLocationUpdates(getActivity());
            dialog.setTitle("Loading");
            dialog.setMessage("Please Wait ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            if(user_location!=null){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StringBuffer smsbody = new StringBuffer();
                        smsbody.append("http://maps.google.com?q=");
                        smsbody.append(user_location.getLatitude());
                        smsbody.append(",");
                        smsbody.append(user_location.getLongitude());
                        message = "This is "+userFullData.getUser_name()+
                                ". I need emergency help. Below is my location. "+smsbody.toString();
                        Log.i("TAG","CALLED");
                        Log.i("TAG","Insert");
                        for (UserEmergencyNumber e: userFullData.getUser_emergency_numbers()) {
                            SmsManager.getDefault().sendTextMessage(e.getMobile_number(),null,message,null,null);
                            Log.i("TAG",e.getMobile_number());
                        }
                        ArrayList<UserEmergencyNumber> contacts;
                        Log.i("ID", String.valueOf(userFullData.getRow_id()));
                        contacts=mysqlitedb.getAllNumbers(userFullData.getRow_id());
                        String contactName;
                        final String contactNumber;
                        if(contacts!=null){
                            contactName = contacts.get(0).getContact_name();
                            contactNumber = contacts.get(0).getMobile_number();
                        }else{
                            contactName = "";
                            contactNumber = userFullData.getUser_emergency_numbers().get(0).getMobile_number();
                        }

                        String message_text = "An auto-call to "
                                +contactName+"("
                                +contactNumber+")"
                                +"will be started after 30 seconds.";
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                        notificationManager.notify(0,getNotification(message_text));
                        dialog.dismiss();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startACall(contactNumber);
                            }
                        },30000);
                    }
                }, 3000);

            }
        }else{
            checkSelfPermission(getActivity());
        }


    }

    private void startACall(String contactNumber) {
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+contactNumber));
            startActivity(callIntent);
        }else{
            checkSelfPermission(getActivity());
        }
    }

    private Notification getNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setContentTitle("SMS sent successfully")
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        return builder.build();
    }
    private void showMyLocation() {
        final UserLocationModel user_location= LocationGetter.requestLocationUpdates(getActivity());
        dialog.setMessage("Getting your location");
        dialog.setTitle("Please Wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if(user_location!=null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent map = new Intent(getActivity(),MyLocationMapsActivity.class);
                    map.putExtra("location",new UserLocationModel(user_location.getUser_id(),
                            user_location.getLatitude(),user_location.getLongitude(),user_location.getGeolocation()));
                    startActivity(map);
                    dialog.dismiss();
                }
            }, 3000);
        }else{
            dialog.dismiss();
            //Toast.makeText(getActivity(),"Press again to show location on map",Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserProfile() {
        Toast.makeText(getContext(), "User Profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),UserProfileActivity.class);
        startActivity(intent);
    }


    private void showInformation() {
        Intent intent = new Intent(getActivity(), ShowGroupInfoActivity.class);
        startActivity(intent);
    }

    private void showCoordinator() {
        Intent intent = new Intent(getActivity(),CoordinatorsActivity.class);
        startActivity(intent);
    }

    private void userLogout() {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        coordinatorList.clear();
        tab.finish();
    }

    public void checkSelfPermission(final Activity activity){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE};
        String rationale = "Please provide location permission so that you can use this service";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(activity/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                sendMessage();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.

            }
        });
    }

}
