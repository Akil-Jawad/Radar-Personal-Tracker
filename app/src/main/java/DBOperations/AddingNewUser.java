package DBOperations;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.summittracker.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import AppConstant.AsyncResponse;
import AppConstant.GlobalConstant;
import Model.SignUpModel;

public class AddingNewUser extends AsyncTask<SignUpModel,Void,String> {
    public AsyncResponse result = null;

    public AddingNewUser(AsyncResponse result) {
        this.result = result;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("Calling",s);
        //Toast.makeText(SignUpActivity.getContext(),"Registration Successful",Toast.LENGTH_LONG).show();
        result.processFinish(s);
    }

    @Override
    protected String doInBackground(SignUpModel... params) {
        try {
            URL url = new URL(GlobalConstant.getSignupUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_name",params[0].getUser_name());
            jsonObject.put("password",params[0].getPassword());
            jsonObject.put("email",params[0].getEmail());
            jsonObject.put("mobile_number",params[0].getMobile_number());
            jsonObject.put("present_address",params[0].getPresent_address());
            jsonObject.put("supervisor_id",params[0].getSuprevisor_id());

            JSONArray group_row_id = new JSONArray(params[0].getGroup_row_id());
            jsonObject.put("group_row_id",group_row_id);

            JSONArray emergency_number = new JSONArray(params[0].getEmergency_number());
            jsonObject.put("emergency_number",emergency_number);
            Thread.sleep(2000);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            Log.i("json",jsonObject.toString());
            os.flush();
            os.close();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();
            sb.append(line);
            Log.i("LINE", sb.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            inputStream.close();
            conn.disconnect();
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
