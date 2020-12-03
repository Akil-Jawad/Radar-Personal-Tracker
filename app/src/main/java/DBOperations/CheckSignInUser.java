package DBOperations;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import AppConstant.AsyncResponse;
import AppConstant.GlobalConstant;

public class CheckSignInUser extends AsyncTask<String,Void,String> {
    public AsyncResponse result;

    public CheckSignInUser(AsyncResponse result) {
        this.result = result;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            result.processFinish(s);
            Log.i("TAG", s);
        }


    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(GlobalConstant.getSigninUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Thread.sleep(2000);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(params[0]);
            Log.i("json",params[0]);
            os.flush();
            os.close();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            sb.append(bufferedReader.readLine());
            Log.i("LINE", sb.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            inputStream.close();
            conn.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
