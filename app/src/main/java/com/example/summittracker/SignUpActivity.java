package com.example.summittracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import DBOperations.AddingNewUser;
import AppConstant.AsyncResponse;
import AppConstant.CustomSignupListAdapter;
import AppConstant.GlobalConstant;
import DBOperations.SQLiteENumberList;
import Model.SignUpAllListViewModel;
import Model.SignUpModel;

import static AppConstant.GlobalConstant.eNumberArrayList;
import static AppConstant.GlobalConstant.gNameArrayList;
import static AppConstant.GlobalConstant.geteNumberArrayList;
import static AppConstant.GlobalConstant.getgNameArrayList;
import static AppConstant.GlobalConstant.getsNameArrayList;
import static AppConstant.GlobalConstant.sNameArrayList;

public class SignUpActivity extends AppCompatActivity implements AsyncResponse {
    public  static final int RequestPermissionCode  = 1 ;
    TextInputLayout userTIL,emailTIL,passTIL,cpassTIL,mobilTIL,paTIL;
    TextInputEditText userName,email,password,cPassword,mobileNumber,presentAddress;
    Button selectSupervisor,selectGroup,selectENumber,signUp;
    TextView loginPage;
    //ProgressBar progressBar;
    ProgressDialog dialog;
    CheckBox supervisorCheckBox;
    ScrollView sv1;
    int progressStatus=0;
    public static ListView eNumberList,supervisorList,groupList;
    private Handler handler = new Handler();
    SQLiteENumberList mysqlitedb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sv1 = findViewById(R.id.sv);
        userTIL = findViewById(R.id.textInputLayout);
        emailTIL =findViewById(R.id.textInputLayout2);
        passTIL =findViewById(R.id.textInputLayout3);
        cpassTIL =findViewById(R.id.textInputLayout4);
        mobilTIL =findViewById(R.id.textInputLayout5);
        paTIL =findViewById(R.id.textInputLayout6);
        userName = findViewById(R.id.input_username);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.confirm_password);
        mobileNumber = findViewById(R.id.phone);
        presentAddress = findViewById(R.id.address);
        selectSupervisor = findViewById(R.id.supervisor_btn);
        selectGroup = findViewById(R.id.group_btn);
        selectENumber = findViewById(R.id.emergency_btn);
        signUp = findViewById(R.id.signup_btn);
        loginPage = findViewById(R.id.signin_text);
        //progressBar = findViewById(R.id.progressCycle);
        eNumberList = findViewById(R.id.emergency_number_list_view);
        supervisorList = findViewById(R.id.supervisor_list_view);
        groupList = findViewById(R.id.group_list_view);
        supervisorCheckBox = findViewById(R.id.selfSupervisor);
        mysqlitedb = new SQLiteENumberList(this);
        dialog = new ProgressDialog(SignUpActivity.this);
        GlobalConstant.setCompleteSignUp(false);
        supervisorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(supervisorCheckBox.isChecked()){
                    selectSupervisor.setVisibility(View.VISIBLE);
                }else{
                    selectSupervisor.setVisibility(View.GONE);
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<8){
                    passTIL.setError("Minimum password length is 8 characters");
                }else{
                    passTIL.setErrorEnabled(false);
                }
            }
        });

        cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(password.getText().toString())){
                    Log.i("Show",s.toString());
                    cpassTIL.setError("Password Not Match");
                }else{
                    cpassTIL.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    public void signIn(View view) {
        finish();
    }

    public void signUp() {
        signUp.setEnabled(false);
        boolean isValid = validation();
        //boolean isValid = true;
        if(isValid){
            dialog.setMessage("SignUp is in progress");
            dialog.setTitle("Please Wait ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
            ArrayList<Integer> group_list = new ArrayList<>();
            ArrayList<String> e_mobile_list = new ArrayList<>();
            for (int i = 0; i < GlobalConstant.getgNameArrayList().size(); i++) {
                group_list.add(GlobalConstant.getgNameArrayList().get(i).getId());
            }
            for (int i = 0; i < GlobalConstant.geteNumberArrayList().size(); i++) {
                e_mobile_list.add(GlobalConstant.geteNumberArrayList().get(i).getContactNumber());
            }
            final SignUpModel createUser = new SignUpModel();
            createUser.setUser_name(userName.getText().toString());
            createUser.setEmail(email.getText().toString());
            createUser.setPassword(password.getText().toString());
            createUser.setMobile_number(mobileNumber.getText().toString());
            createUser.setPresent_address(presentAddress.getText().toString());
            if(supervisorCheckBox.isChecked()){
                createUser.setSuprevisor_id(GlobalConstant.getsNameArrayList().get(0).getId());
            }else{
                createUser.setSuprevisor_id(-1);
            }
            createUser.setGroup_row_id(group_list);
            createUser.setEmergency_number(e_mobile_list);
            AddingNewUser add = new AddingNewUser(this);
            add.execute(createUser);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(progressStatus < 100){
//                        progressStatus++;
//                        android.os.SystemClock.sleep(50);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.GONE);
//                            GlobalConstant.geteNumberArrayList().clear();
//                            GlobalConstant.getsNameArrayList().clear();
//                            GlobalConstant.getgNameArrayList().clear();
//                        }
//                    });
//                }
//            }).start();


        }
        else{
            signUp.setEnabled(true);
        }
    }

    private String trimFunction(TextInputEditText trimString){
        if(trimString.getText()!=null){
            return trimString.getText().toString().trim();
        }else{
            return "";
        }

    }

    private boolean validation() {
        if(TextUtils.isEmpty(trimFunction(userName))){
            userTIL.setError("Enter your name");
            return false;
        }else{
            userTIL.setErrorEnabled(false);
        }
        if(!GlobalConstant.isValidEmail(trimFunction(email))){
            emailTIL.setError("Enter your valid email");
            return false;
        }else{
            emailTIL.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(trimFunction(password)) && trimFunction(password).length()<8){
            passTIL.setError("Invalid password");
            return false;
        }else{
            passTIL.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(trimFunction(cPassword))){
            cpassTIL.setError("Confirm your password");
            return false;
        }else{
            cpassTIL.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(trimFunction(mobileNumber))){
            mobilTIL.setError("Enter your valid mobile number");
            return false;
        }else{
            mobilTIL.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(trimFunction(presentAddress))){
            paTIL.setError("Enter your valid permanent address");
            return false;
        }else{
            paTIL.setErrorEnabled(false);
        }
        if(GlobalConstant.getsNameArrayList().size()<1 && supervisorCheckBox.isChecked()){
            Toast.makeText(this,"Select atleast 1 supervisor",Toast.LENGTH_LONG).show();
            return false;
        }else if(GlobalConstant.getgNameArrayList().size()<1){
            Toast.makeText(this,"Select atleast 1 group number",Toast.LENGTH_LONG).show();
            return false;
        }else if(GlobalConstant.geteNumberArrayList().size()<1) {
            Toast.makeText(this, "Select atleast 1 emergency number", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }

    }

    public void groupSelect(View view) {
        Intent intent = new Intent(SignUpActivity.this,GroupListActivity.class);
        startActivityForResult(intent,3);
    }

    public void supervisorSelect(View view) {
        Intent intent = new Intent(SignUpActivity.this,SupervisorListActivity.class);

        startActivityForResult(intent,2);
        //finish();//pore finish remove krte hbe
    }

    public void emergencyNumberSelect(View view) {
        if(ContextCompat.checkSelfPermission(SignUpActivity.this,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(SignUpActivity.this,EmergencyNumberListActivity.class);
            startActivityForResult(intent,1);
        }else{
            enableRuntimePermission();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.i("onPause ","Called");
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<SignUpAllListViewModel> list = new ArrayList<>();
                if (data != null) {
                    for (int i = 0; i < eNumberArrayList.size(); i++) {
                        String name = eNumberArrayList.get(i).getContactName();
                        list.add(new SignUpAllListViewModel(name, false, requestCode));
                    }
                    if (geteNumberArrayList().size() > 0) {
                        Snackbar snackbar = Snackbar.make(sv1, "Emergency Number Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(sv1, "No Emergency Number Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    final CustomSignupListAdapter adapter = new CustomSignupListAdapter(this, list);
                    eNumberList.setAdapter(adapter);
                    eNumberList.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(eNumberList, list.size());
                }

            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<SignUpAllListViewModel> list = new ArrayList<>();

                if (data != null) {
                    for (int i = 0; i < sNameArrayList.size(); i++) {
                        String name = sNameArrayList.get(i).getSupervisorName();
                        list.add(new SignUpAllListViewModel(name, false, requestCode));
                        Log.i("ArrayList", getsNameArrayList().toString());

                    }
                    if (getsNameArrayList().size() > 0) {
                        Snackbar snackbar = Snackbar.make(sv1, "Supervisor Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(sv1, "No Supervisor Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    final CustomSignupListAdapter adapter = new CustomSignupListAdapter(this, list);
                    supervisorList.setAdapter(adapter);
                    supervisorList.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(supervisorList, 1);
                }

            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("Stopping", String.valueOf(gNameArrayList));
                ArrayList<SignUpAllListViewModel> list = new ArrayList<>();
                if (data != null) {
                    for (int i = 0; i < gNameArrayList.size(); i++) {
                        String name = gNameArrayList.get(i).getGroupName();
                        //Log.i("Grp_name",name);
                        Log.i("Stopp", String.valueOf(gNameArrayList.get(i).isChecked()));
                        list.add(new SignUpAllListViewModel(name, false, requestCode));
                    }
                    if (getgNameArrayList().size() > 0) {
                        Snackbar snackbar = Snackbar.make(sv1, "Group Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(sv1, "No Group Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    final CustomSignupListAdapter adapter = new CustomSignupListAdapter(this, list);
                    groupList.setAdapter(adapter);
                    groupList.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(groupList, list.size());
                }

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();

    }
    public static void setListViewHeightBasedOnChildren(ListView listView,int count){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        //Log.i("ArrayList",geteNumberArrayList().toString());
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            //Log.i("Height", String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //Log.i("Actual Height", String.valueOf(params.height));
        if(count<1){
            listView.setVisibility(View.GONE);
        }else{
            listView.setLayoutParams(params);
        }
    }
    public void enableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                SignUpActivity.this,
                Manifest.permission.READ_CONTACTS))
        {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Need contacts permission to access")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SignUpActivity.this,new String[]{
                                        Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
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
            ActivityCompat.requestPermissions(SignUpActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(SignUpActivity.this,EmergencyNumberListActivity.class);
                    startActivityForResult(intent,1);
                } else {
                    Toast.makeText(SignUpActivity.this,"Permission Canceled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void processFinish(String result) {
        if(result!=null){
//            GlobalConstant.setCompleteSignUp(true);
//            int user_id = Integer.parseInt(result);
//            //Toast.makeText(getApplicationContext(),user_id+"_id",Toast.LENGTH_LONG).show();
//            for (int i = 0; i < geteNumberArrayList().size(); i++) {
//                mysqlitedb.insertContact(user_id,geteNumberArrayList().get(i).getContactName(),geteNumberArrayList().get(i).getContactNumber());
//            }
//            finish();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject.has("message")){
                    String message = jsonObject.getString("message");
                    //progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    signUp.setEnabled(true);
                    Snackbar snackbar = Snackbar.make(sv1,message,Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if(jsonObject.has("success")){
                    int user_id = jsonObject.getInt("success");
                    for (int i = 0; i < geteNumberArrayList().size(); i++) {
                        mysqlitedb.insertContact(user_id,geteNumberArrayList().get(i).getContactName(),geteNumberArrayList().get(i).getContactNumber());
                    }
                    dialog.dismiss();
                    signUp.setEnabled(true);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Snackbar snackbar = Snackbar.make(sv1,"Registration Unsuccessful",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalConstant.geteNumberArrayList().clear();
        GlobalConstant.getsNameArrayList().clear();
        GlobalConstant.getgNameArrayList().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalConstant.geteNumberArrayList().clear();
        GlobalConstant.getsNameArrayList().clear();
        GlobalConstant.getgNameArrayList().clear();
    }
}
