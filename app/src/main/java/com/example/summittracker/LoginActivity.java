package com.example.summittracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import AppConstant.AsyncResponse;
import DBOperations.CheckSignInUser;
import AppConstant.GlobalConstant;
import DBOperations.SQLiteENumberList;
import DBOperations.UpdateSupervisor;
import Model.UserEmergencyNumber;
import Model.UserFullData;
import Model.UserGroups;

import static AppConstant.GlobalConstant.coordinatorList;
import static AppConstant.GlobalConstant.getCoordinatorList;
import static AppConstant.GlobalConstant.getSearchCoordinatorList;
import static AppConstant.GlobalConstant.getsNameArrayList;
import static AppConstant.GlobalConstant.userFullData;


public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    String LOGIN_CREDENTIALS = "LOGIN_CREDENTIALS";
    ScrollView scrollView;
    TextInputLayout mTil,pTil;
    TextInputEditText mobileNumber,password;
    TextView registration;
    private Button signin;
    ProgressDialog dialog;
    //TextView forgetPassword;
    CheckBox keepLoggedIn;
    boolean checkLoggedIn = false;
    //ProgressBar progressBar;
    public static ArrayList<UserEmergencyNumber> getEmNumbDetail = new ArrayList<>();
    public static ArrayList<UserGroups> getGrpDetail = new ArrayList<>();
    public int userId = 0;
    private Handler handler = new Handler();
    SQLiteENumberList mysqlitedb;
    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getGrpDetail.clear();
        getEmNumbDetail.clear();
        dialog = new ProgressDialog(LoginActivity.this);
        signin = findViewById(R.id.signin_btn);
        scrollView = findViewById(R.id.cl);
        mTil = findViewById(R.id.mobileInputLayout);
        pTil = findViewById(R.id.passwordInputLayout);
        mobileNumber = findViewById(R.id.input_mobile);
        password = findViewById(R.id.input_password);
        registration = findViewById(R.id.signintext);
        //forgetPassword = findViewById(R.id.forgetPass);
        keepLoggedIn = findViewById(R.id.logged_in_check_box);
        //progressBar = findViewById(R.id.progressCycleSignin);
        mysqlitedb = new SQLiteENumberList(this);
        if(GlobalConstant.isCompleteSignUp()){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.cl),"Registration Complete",Snackbar.LENGTH_LONG);
            snackbar.show();
            GlobalConstant.setCompleteSignUp(false);
        }
        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    mTil.setError("You need to insert your mobile number");
                }else{
                    mTil.setError(null);
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
                    pTil.setError("Minimum password length is 8 characters");
                }else{
                    pTil.setError(null);
                }
            }
        });

        keepLoggedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoggedIn = ((CheckBox) v).isChecked();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = getApplicationContext().getSharedPreferences(LOGIN_CREDENTIALS, MODE_PRIVATE);
        if(mSharedPreferences.getBoolean("isloggedin",false)){
            Log.i("onResume","called");
            String mobile = mSharedPreferences.getString("mobile_number", null);
            String pass = mSharedPreferences.getString("password", null);
            boolean check = mSharedPreferences.getBoolean("isloggedin",false);
            Log.i("TAG", String.valueOf(check));
            if(mobile!=null && pass !=null){
                mobileNumber.setText(mobile);
                password.setText(pass);
                keepLoggedIn.setChecked(true);
                checkLoggedIn=true;
            }else{
                Log.i("onResume","null");

            }
        }
    }

    public void signIn() {
        dialog.setMessage("Signing In");
        dialog.setTitle("Please Wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        signin.setEnabled(false);
        boolean editFieldCheck;
        //progressBar.setVisibility(View.VISIBLE);
        editFieldCheck = checkEditText();
        if(editFieldCheck){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mobile_number",mobileNumber.getText().toString());
                jsonObject.put("password",password.getText().toString());
                CheckSignInUser checkSignInUser = new CheckSignInUser(this);
                checkSignInUser.execute(jsonObject.toString());
                Log.i("Json",jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //Toast.makeText(this,"Mobile number or Password is invalid",Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.cl),"Insert valid mobile number or password",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private boolean checkEditText() {
        if(TextUtils.isEmpty(mobileNumber.getText())){
            if(TextUtils.isEmpty(password.getText())){
                return false;
            }
        }
        return true;
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String result) {
        coordinatorList.clear();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            if(jsonObject.has("message")){
                String message = jsonObject.getString("message");
                //progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                signin.setEnabled(true);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.cl),message,Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(jsonObject.has("no_sup_id")){
                userId = jsonObject.getInt("no_sup_id");
                //progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                signin.setEnabled(true);
                Toast.makeText(LoginActivity.this,"Your supervisor removed you from " +
                        "his coordinator list.Please choose another supervisor and then signin again",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this,SupervisorListActivity.class);
                intent.putExtra("flag_for_no_sup",true);
                startActivityForResult(intent,1);

            } else{
                int row_id = jsonObject.getInt("row_id");
                String user_name = jsonObject.getString("user_name");
                String email = jsonObject.getString("email");
                String pass = jsonObject.getString("pass");
                String phone = jsonObject.getString("phone");
                String present_address = jsonObject.getString("present_address");
                String supervisor_name = jsonObject.getString("supervisor_name");
                JSONArray jsonArray = jsonObject.getJSONArray("group_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    getGrpDetail.add(new UserGroups(obj.getInt("group_row_id"),obj.getString("group_name")));
                }
                jsonArray = jsonObject.getJSONArray("em_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    getEmNumbDetail.add(new UserEmergencyNumber(obj.getInt("row_id"),obj.getString("mobile_number"),null));
                }
                jsonArray = jsonObject.getJSONArray("coord_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    getCoordinatorList().add(new UserFullData(obj.getInt("row_id"),obj.getString("user_name"),obj.getString("email")
                            ,null,obj.getString("phone"),obj.getString("present_address"),null,null,null));
                }
                getSearchCoordinatorList().addAll(getCoordinatorList());
                userFullData = new UserFullData(row_id,user_name,email,pass,phone,present_address,supervisor_name,
                        getGrpDetail,getEmNumbDetail);
                //Log.i("DATA ",coordinatorList.get(0).getRow_id()+" "+coordinatorList.get(0).getUser_name());
                if(checkLoggedIn){
                    Log.i("check","check");
                    SharedPreferences.Editor sharedPref = getApplicationContext().getSharedPreferences(
                            LOGIN_CREDENTIALS, MODE_PRIVATE).edit();
                    sharedPref.putString("mobile_number", mobileNumber.getText().toString());
                    sharedPref.putString("password", password.getText().toString());
                    Log.i("mobile",mobileNumber.getText().toString());
                    sharedPref.putBoolean("isloggedin",checkLoggedIn);
                    sharedPref.apply();
                }
                //getAllGroupMembers(getGrpDetail);
                successfulSignIn();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id",userId);
                    jsonObject.put("new_sup_id",getsNameArrayList().get(0).getId());

                    UpdateSupervisor updateSupervisor = new UpdateSupervisor(new AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            Snackbar snackbar = Snackbar.make(scrollView,"Update Supervisor Successfully",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                    updateSupervisor.execute(jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void successfulSignIn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        //mPopupWindow.dismiss();
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.cl),"SignIn successful",Snackbar.LENGTH_SHORT);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlue));
                        snackbar.show();
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG","RUNNNNNN");
                        Intent intent = new Intent(LoginActivity.this, TabActivity.class);
                        startActivity(intent);
                        signin.setEnabled(true);
                        finish();
                    }
                }, 2500);
            }
        }).start();
    }
}
