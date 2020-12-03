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

import AppConstant.GlobalConstant;
import Model.UserLocationModel;

public class UserLocationUpdate extends AsyncTask<UserLocationModel,Void,Void> {
    @Override
    protected Void doInBackground(UserLocationModel... params) {
        try {
            URL url = new URL(GlobalConstant.getUpdateUserLocation());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", params[0].getUser_id());
            jsonObject.put("latitude", params[0].getLatitude());
            jsonObject.put("longitude", params[0].getLongitude());
            jsonObject.put("geolocation",params[0].getGeolocation());
            Log.i("JSON", jsonObject.toString());
            Thread.sleep(2000);
            conn.setDoInput(true);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append((line + "\n"));
            }
            Log.i("LINE", sb.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());
            inputStream.close();
            conn.disconnect();
            return null;
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
