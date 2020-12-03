package DBOperations;


import android.os.AsyncTask;
import android.util.Log;

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
import java.net.ProtocolException;
import java.net.URL;

import AppConstant.AsyncResponse;
import AppConstant.GlobalConstant;

public class GetAllGroupMembers extends AsyncTask<Integer,Void,String> {
    public AsyncResponse result;

    public GetAllGroupMembers(AsyncResponse result) {
        this.result = result;
    }
    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            result.processFinish(s);
        }
    }

    @Override
    protected String doInBackground(Integer... integers) {
        int group_id = integers[0];
        int user_id = integers[1];
        try {
            URL url = new URL(GlobalConstant.getGroupMembers());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("group_row_id", group_id);
            jsonObject.put("user_row_id", user_id);
            Log.i("Group Member", integers[0]+" "+integers[1]);
            Thread.sleep(2000);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append((line + "\n"));
            }

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
