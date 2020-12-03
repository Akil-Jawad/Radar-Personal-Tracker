package com.example.summittracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AppConstant.ExpandableProfileListAdapter;
import DBOperations.SQLiteENumberList;
import DBOperations.SQLiteImageDB;
import Model.UserEmergencyNumber;

import static AppConstant.GlobalConstant.userFullData;

public class UserProfileActivity extends AppCompatActivity {
    public  static final int RequestPermissionCode  = 3 ;
    public  static final int IMAGE_PICK_CODE =100;
    TextView name,mobile,email,address;
    ExpandableListView grpEmpSupList;
    ExpandableProfileListAdapter listAdapter;
    List<String> listProfileHeader;
    HashMap<String,List<String>> listProfileChildOption;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    SQLiteImageDB mydb;
    SQLiteENumberList mysqlitedb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileImage = findViewById(R.id.user_image);
        name = findViewById(R.id.user_name);
        mobile = findViewById(R.id.user_mobile);
        email = findViewById(R.id.user_email);
        address = findViewById(R.id.user_address);
        grpEmpSupList = findViewById(R.id.allExp);
        mysqlitedb = new SQLiteENumberList(this);
//        Log.i("USER", String.valueOf(mydb.getImage(userFullData.getRow_id())));
//        if(mydb.getImage(58)!=null){
//            profileImage.setImageBitmap(mydb.getImage(userFullData.getRow_id()));
//        }
        mydb = new SQLiteImageDB(this);
        Bitmap bitmap = mydb.getImage(userFullData.getRow_id());
        Log.i("Integer", String.valueOf(bitmap));
        if(bitmap==null){

        }else{
            profileImage.setImageBitmap(bitmap);
        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        enableRuntimePermission();
                    }else{
                        pickImageFromGallery();

                    }
                }else{
                    pickImageFromGallery();

                }
            }
        });
        setValues();
    }

    private void setValues() {
        name.setText(userFullData.getUser_name());
        mobile.setText(userFullData.getMobile_number());
        email.setText(userFullData.getEmail());
        address.setText(userFullData.getPresent_address());

        prepareListData();
        listAdapter = new ExpandableProfileListAdapter(this, listProfileHeader, listProfileChildOption);
        grpEmpSupList.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listProfileHeader = new ArrayList<>();
        listProfileChildOption = new HashMap<>();

        listProfileHeader.add("My Supervisor");
        listProfileHeader.add("My Groups");
        listProfileHeader.add("My Emergency Numbers");

        List<String> supervisor = new ArrayList<>();
        supervisor.add(userFullData.getSuprevisor_name());
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < userFullData.getUser_groups().size(); i++) {
            groups.add(userFullData.getUser_groups().get(i).getGroup_name());
        }
        ArrayList<UserEmergencyNumber> contacts;
        Log.i("ID", String.valueOf(userFullData.getRow_id()));
        contacts=mysqlitedb.getAllNumbers(userFullData.getRow_id());
        List<String> emNumbers = new ArrayList<>();
        for (int i = 0; i < userFullData.getUser_emergency_numbers().size(); i++) {
            if(contacts!=null){
                emNumbers.add(contacts.get(i).getContact_name()+" : "+contacts.get(i).getMobile_number());

            }else{
                emNumbers.add(userFullData.getUser_emergency_numbers().get(i).getMobile_number());
            }
        }

        listProfileChildOption.put(listProfileHeader.get(0), supervisor); // Header, Child data
        listProfileChildOption.put(listProfileHeader.get(1), groups);
        listProfileChildOption.put(listProfileHeader.get(2), emNumbers);
    }

    private String onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            //profileImage.setImageURI(selectedImage);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if(cursor!=null){
                int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            }
            return selectedImage.getPath();
        }
        return null;
    }

    public void enableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                UserProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Need contacts permission to access")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UserProfileActivity.this,new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(UserProfileActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(UserProfileActivity.this,"Permission Canceled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==IMAGE_PICK_CODE && resultCode==RESULT_OK){
            profileImage.setImageURI(data.getData());
            String x = onSelectFromGalleryResult(data);
            Integer num = userFullData.getRow_id();
            if(mydb.insertImage(x,num)){
                Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),"Not Successfull",Toast.LENGTH_LONG).show();

            }
        }
    }

}
