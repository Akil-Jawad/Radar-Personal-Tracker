package com.example.summittracker.map.activity;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.summittracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import Model.UserLocationModel;

public class MyLocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public LocationManager locationManager;
    public LocationUpdateListener listener;
    Marker marker;
    double latitude;
    double longitude;
    String geolocation;
    boolean count = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        UserLocationModel locationModel = intent.getParcelableExtra("location");
        latitude=locationModel.getLatitude();
        longitude=locationModel.getLongitude();
        geolocation=locationModel.getGeolocation();
        Log.i("LOCATION",latitude+" "+longitude+" "+geolocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationUpdateListener();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PermissionChecker.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==
                PermissionChecker.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(latitude, longitude);
        marker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in "+geolocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18.0f));
    }

    class  LocationUpdateListener implements LocationListener{

        @Override
        public void onLocationChanged(final Location location) {
            marker.remove();
            LatLng updateLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if(count){
                marker = mMap.addMarker(new MarkerOptions().position(updateLocation)
                        .title("Marker in "+geolocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                count=false;
            }else{
                marker = mMap.addMarker(new MarkerOptions().position(updateLocation)
                        .title("Marker in "+geolocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                count=true;
            }
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(updateLocation,18.0f));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(updateLocation,18.0f));
            Log.i("Location Update :",location.getLatitude()+"  "+location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        locationManager.removeUpdates(listener);
        Log.i("onBackPressed","Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
        Log.i("onPause","Called");

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(listener);
        Log.i("onStop","Called");

    }
}
