package com.app.sealteamdelivery.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.app.sealteamdelivery.model.LocationInformation;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.gson.Gson;

import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleService extends Service implements LocationListener{

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;

    int userId;
    Data user_;

//    private Handler mHandler = new Handler();
//    private Timer mTimer = null;
//    long notify_interval = 1000;
//    public static String str_receiver = "servicetutorial.service.receiver";
//    Intent intent;

    InterfaceRetro apiInterface;




    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        SharedPreferences appSharedPrefs = (SharedPreferences) getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("user", "");
        user_ = gson.fromJson(json, Data.class);

        userId = user_.getUser().getUserId();

//        mTimer = new Timer();
//        mTimer.schedule(new TimerTaskToGetLocation(),5,notify_interval);
//        intent = new Intent(str_receiver);
        try {
            fn_getlocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            addLocation(location.getLatitude(),location.getLongitude(),userId);

        }catch (Exception e){}
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

    @SuppressLint("MissingPermission")
    private void fn_getlocation() throws Exception{
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

        }else {

            if (isNetworkEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//                        fn_update(location);
                        addLocation(latitude,longitude,userId);
                    }
                }

            }


            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//                        fn_update(location);

                        addLocation(latitude,longitude,userId);
                    }
                }
            }


        }

    }

//    private class TimerTaskToGetLocation extends TimerTask{
//        @Override
//        public void run() {
//
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    fn_getlocation();
//                }
//            });
//
//        }
//    }
//
//    private void fn_update(Location location){
//
//        intent.putExtra("latutide",location.getLatitude()+"");
//        intent.putExtra("longitude",location.getLongitude()+"");
//        sendBroadcast(intent);
//    }

    public void addLocation(double lat,double lng,int driver_id){

        LocationInformation locationInformation = new LocationInformation();
        locationInformation.setLat(lat+"");
        locationInformation.setLng(lng+"");
        locationInformation.setDriver_id(driver_id);

        Call<Result> call = apiInterface.addLocation(locationInformation,user_.getAccessToken());
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("aaaaaaaaaaaaaa","done");

                Result resource = response.body();


//                Log.v("aaaaaaaaaaaaaa",response.message());
//                Log.v("aaaaaaaaaaaaaa","FaildReason = "+resource.getFaildReason());
//                Log.v("aaaaaaaaaaaaaa",resource.getData()+"");
//                Log.v("aaaaaaaaaaaaaa",resource.getIsSuccess()+"");


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("aaaaaaaaaaaaaa","error");
//                Log.v("aaaaaaaaaaaaaa",t.getMessage());

            }
        });
//        Log.v("aaaaaaaaaaaaaa","finish");

    }

}