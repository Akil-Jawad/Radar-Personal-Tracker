package AppConstant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.UserLocationModel;

import static AppConstant.GlobalConstant.userFullData;

public class LocationGetter {
    private static UserLocationModel user_location = new UserLocationModel(0,0.0,0.0,null);
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static LocationRequest locationRequest;
    private static String geolocation = null;

    public static void checkSelfPermission(final Activity activity){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        String rationale = "Please provide location permission so that you can use this service";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(activity/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                requestLocationUpdates(activity);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.

            }
        });
    }
    public static UserLocationModel requestLocationUpdates(final Activity activity) {
        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)== PermissionChecker.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)==
                PermissionChecker.PERMISSION_GRANTED){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            locationRequest = new LocationRequest();

            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(1000);
            locationRequest.setInterval(3000);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Geocoder geocoder = new Geocoder(activity, Locale.US);
                        try{
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                            geolocation = addressList.get(1).getSubLocality() + "," + addressList.get(1).getLocality();
                            Log.i("GEOLOCATION :",geolocation);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        user_location.setUser_id(userFullData.getRow_id());//////////////////id bosbe
                        user_location.setLatitude(location.getLatitude());
                        user_location.setLongitude(location.getLongitude());
                        user_location.setGeolocation(geolocation);
                        Log.i("TAG", String.valueOf(user_location.getLatitude()));

                    }
                }

            });
            return user_location;
        }else{
            checkSelfPermission(activity);
        }
        return null;

    }
}
