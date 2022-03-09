package com.app.sealteamdelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.app.sealteamdelivery.chat.ChatActivity;
import com.app.sealteamdelivery.fragment.ChatFragment;
import com.app.sealteamdelivery.fragment.DelivaryFragment;
import com.app.sealteamdelivery.fragment.HistoryFragment;
import com.app.sealteamdelivery.fragment.MapFragment;
import com.app.sealteamdelivery.fragment.StatusFragment;
import com.app.sealteamdelivery.model.AvailableInformation;
import com.app.sealteamdelivery.model.DelivaryCostInformation;
import com.app.sealteamdelivery.model.DeviceTokenInformation;
import com.app.sealteamdelivery.model.LocationInformation;
import com.app.sealteamdelivery.model.checkversion.CheckVersion;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.app.sealteamdelivery.service.GoogleService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import android.support.v4.app.Fragment;

import android.preference.PreferenceManager;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    StatusFragment statusFragment;
    DelivaryFragment delivaryFragment;
    MapFragment mapFragment;
    ChatFragment chatFragment;
    HistoryFragment historyFragment;

    TextView version,version2;
    String auth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user_);

            switch (item.getItemId()) {

                case R.id.navigation_status:

                    statusFragment.setArguments(bundle);

                    showFragment(statusFragment);
                    return true;
                case R.id.navigation_deliveries:

                    delivaryFragment.setArguments(bundle);

                    showFragment(delivaryFragment);
                    return true;
                case R.id.navigation_map:

                    mapFragment.setArguments(bundle);

                    showFragment(mapFragment);
                    return true;
//                case R.id.navigation_chat:
//
//                    chatFragment.setArguments(bundle);
//
//                    showFragment(chatFragment);
//                    return true;
                case R.id.navigation_history:

                    historyFragment.setArguments(bundle);

                    showFragment(historyFragment);
                    return true;
            }
            return false;
        }
    };
    SlidingRootNav drawer;

    float LOCATION_REFRESH_DISTANCE = 1;
    long LOCATION_REFRESH_TIME = 100;

    InterfaceRetro apiInterface;

    Data user_;

    SharedPreferences mPrefs ;

    Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPrefs = (SharedPreferences) getSharedPreferences("pref", MODE_PRIVATE);

        if (mPrefs.getString("lang","").length() > 0) {
            Locale locale = new Locale(mPrefs.getString("lang",""));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        }else {
            if (getCurrentLocale(this).getLanguage().contains("ar")) {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("lang", "ar");
                prefsEditor.commit();
            } else {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("lang", "en");
                prefsEditor.commit();
            }

        }


        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        statusFragment = new StatusFragment();
        delivaryFragment = new DelivaryFragment();
        mapFragment = new MapFragment();
        chatFragment = new ChatFragment();
        historyFragment = new HistoryFragment();

        if (getCurrentLocale(this).getLanguage().contains("ar")) {
            drawer = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withGravity(SlideGravity.RIGHT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite.
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .inject();
        } else {
            drawer = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withGravity(SlideGravity.LEFT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .inject();
        }


        try {
            version = findViewById(R.id.version);
            version2 = findViewById(R.id.version2);

            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);

            version.setText("Version Name : "+pInfo.versionName);
            version2.setText("Version Number : "+pInfo.versionCode);
//                    String version = pInfo.versionName;
//
//                    int verCode = pInfo.versionCode;

        }catch (Exception e){}
        //        drawer = new SlidingRootNavBuilder(this)
//            .withMenuLayout(R.layout.menu_left_drawer)
//            .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
//            .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
//            .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
//            .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
//            .inject();

        drawer.closeMenu();


        apiInterface = APIClient.getClient().create(InterfaceRetro.class);
        checkVersion();


//        SharedPreferences appSharedPrefs = PreferenceManager
//            .getDefaultSharedPreferences(this.getApplicationContext());
        auth = mPrefs.getString("auth", "");
        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");
        user_ = gson.fromJson(json, Data.class);


        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user_);

        delivaryFragment.setArguments(bundle);
        showFragment(delivaryFragment);
        navView.setSelectedItemId(R.id.navigation_deliveries);

        if (getIntent().getBooleanExtra("accept",false)){

            new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.new_order))
                .setMessage(R.string.you_get_new_order)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }) .show();

        }

        if (getIntent().getBooleanExtra("newOrder",false)){
            String value = getIntent().getStringExtra("data");

            openDialogNewOrder(value);
        }

        Log.v("ooooo","1");
        try {
            Log.v("ooooo","2");
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.v("ooooo","3");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }else{
                Log.v("ooooo","4");
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
            }

        }catch (Exception e){
            Log.v("ooooo","error");
            Log.v("ooooo",e.getMessage());

        }

        try{
            statusCheck();
        }catch (Exception e){}

        try {
            addDeviceToken(user_.getUser().getUserId(),FirebaseInstanceId.getInstance().getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        changeAvailability(user_.getUser().getUserId(),"on");

        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
        startService(intent);
    }

    int order_id;
    private void openDialogNewOrder(String value) {

        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_new_order);
        dialog.setTitle(R.string.new_order);
        resturantName = dialog.findViewById(R.id.resturantName);
        resturantPlace = dialog.findViewById(R.id.resturantPlace);
        CustomerPlace = dialog.findViewById(R.id.CustomerPlace);
        orderNumber = dialog.findViewById(R.id.orderNumber);
        orderCost = dialog.findViewById(R.id.orderCost);

        try{
            resturantName.setText(mainObject.getString("ResturantName"));

        }catch (Exception e){}

        try {
            resturantPlace.setText(mainObject.getString("ResturantLocation"));
        }catch (Exception e){}


        try {
            orderNumber.setText(mainObject.getInt("OrderId")+"");
            order_id = mainObject.getInt("OrderId");
//            Log.v("kkkkkkkkkkkkkkkk", "OrderId  = " + order_id);
//            Log.v("kkkkkkkkkkkkkkkk", "OrderId2  = " + mainObject.get("OrderId"));
        }catch (Exception e){
//            Log.v("kkkkkkkkkkkkkkkk", "OrderId error = " + e.getMessage());

        }

        try {
            CustomerPlace.setText(mainObject.getString("OrderLocation"));
        }catch (Exception e){}

        try {
            orderCost.setText(mainObject.getString("OrderCost"));
        }catch (Exception e){}


        reject = dialog.findViewById(R.id.reject);
        btn_change_status = dialog.findViewById(R.id.btn_change_status);
        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    changeStatus(user_.getUser().getUserId(), order_id, 0,-1);

                }catch (Exception e){}

                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
                dialog.dismiss();
//                finish();
            }
        });
        btn_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.enter_your_delivary_cost));


                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        m_Text = input.getText().toString();
                        if (input.getText().toString().trim().length() > 0) {

                            try {
                                changeStatus(user_.getUser().getUserId(), order_id, Integer.parseInt(input.getText().toString().trim()),1);
                            }catch (Exception e){
                                startActivity(new Intent(MainActivity.this,MainActivity.class));
                                finish();
                            }
                            dialog.cancel();
                        } else
                            Toast.makeText(MainActivity.this, getString(R.string.enter_your_delivary_cost), Toast.LENGTH_SHORT).show();
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });

                builder.show();

            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    TextView resturantName,resturantPlace,CustomerPlace,orderNumber,orderCost;
    Button btn_change_status,reject;

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        getSupportFragmentManager().beginTransaction().add(R.id.navigation_placeholder,fragment).commit();


        fragmentTransaction.replace(R.id.navigation_placeholder, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void openCloseMenu(View view) {
        if (drawer.isMenuOpened())
            drawer.closeMenu();
        else
            drawer.openMenu();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            try {
                addLocation(location.getLatitude(),location.getLongitude(),user_.getUser().getUserId());
//                Log.v("lllllllllllll",location.getLatitude()+" , "+location.getLongitude()+" , "+user_.getUserId());
            }catch (Exception e){}
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void checkVersion(){

        Call<CheckVersion> call = apiInterface.checkVersion(auth);
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<CheckVersion>() {
            @Override
            public void onResponse(Call<CheckVersion> call, Response<CheckVersion> response) {

//                Log.v("aaaaaaaaaaaaaa","done");

                CheckVersion resource = response.body();

                try {
                    PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
//                    String version = pInfo.versionName;
//
//                    int verCode = pInfo.versionCode;

                    if ( !resource.getData().getValue().equals(""+pInfo.versionCode)){
//                    if (!resource.getData().getValue().equals("V1.2")){

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setMessage("يجب تحديث التطبيق لتتمكن من استلام الطلبات");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "تحديث",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = "https://play.google.com/store/apps/details?id=com.app.sealteamdelivery";
//                                        String url = "https://drive.google.com/open?id=1XRYtJPHQ1aOXyT2NEu_1bftZLuvovslu";
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);

                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                Log.v("aaaaaaaaaaaaaa",response.message());
//                Log.v("aaaaaaaaaaaaaa","FaildReason = "+resource.getFaildReason());
//                Log.v("aaaaaaaaaaaaaa",resource.getData()+"");
//                Log.v("aaaaaaaaaaaaaa",resource.getIsSuccess()+"");


            }

            @Override
            public void onFailure(Call<CheckVersion> call, Throwable t) {
                call.cancel();

//                Log.v("aaaaaaaaaaaaaa","error");
//                Log.v("aaaaaaaaaaaaaa",t.getMessage());

            }
        });
//        Log.v("aaaaaaaaaaaaaa","finish");

    }

    public void addLocation(double lat,double lng,int driver_id){

        LocationInformation locationInformation = new LocationInformation();
        locationInformation.setLat(lat+"");
        locationInformation.setLng(lng+"");
        locationInformation.setDriver_id(driver_id);

        Call<Result> call = apiInterface.addLocation(locationInformation,auth);
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

    public void addDeviceToken(int driver_id,String deviceToken)throws Exception{


        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
        deviceTokenInformation.setDriver_id(driver_id);
        deviceTokenInformation.setDeviceToken(deviceToken);

        Call<Result> call = apiInterface.addDeviceToken(deviceTokenInformation,auth);
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

    public void logout(View view) {
        try {
            apiInterface = APIClient.getClient().create(InterfaceRetro.class);
            addDeviceToken(user_.getUser().getUserId(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            changeAvailability(user_.getUser().getUserId(),"off");

        }catch (Exception e){}
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putString("user", "");
        prefsEditor.commit();

        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    public void changeAvailability(int driver_id,String status){
//        Log.v("mmmmmmmmmm",status);


//        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
//        deviceTokenInformation.setDriver_id(driver_id);
//        deviceTokenInformation.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        AvailableInformation availableInformation = new AvailableInformation();
        availableInformation.setDriver_id(driver_id);
        availableInformation.setStatus(status);

        Call<Result> call = apiInterface.changeAvailability(availableInformation,auth);
//        Log.v("mmmmmmmmmm","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("mmmmmmmmmm","done");
//
//                Result resource = response.body();

//                Log.v("mmmmmmmmmm",response.message());
//                Log.v("mmmmmmmmmm","FaildReason = "+resource.getFaildReason());
//                Log.v("mmmmmmmmmm",resource.getIsSuccess()+"");


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("mmmmmmmmmm","error");
//                Log.v("mmmmmmmmmm",t.getMessage());

            }
        });
//        Log.v("mmmmmmmmmm","finish");

    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.permission_denied))
                        .setMessage(getString(R.string.you_will_not_get_order))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                finish();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void openSettings(View view) {
        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        drawer.closeMenu();
    }

    public void changePassord(View view) {
        startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));
        drawer.closeMenu();
    }

    public void openTerms(View view) {
        startActivity(new Intent(MainActivity.this,TermsAndConditions.class));
        drawer.closeMenu();
    }

    public void changeStatus(final int driver_id, int order_id, int delivrycost, final int status){

        DelivaryCostInformation delivaryCostInformation = new DelivaryCostInformation();
        delivaryCostInformation.setDriver_id(driver_id);
        delivaryCostInformation.setResponseStatus(status);
        delivaryCostInformation.setOrder_id(order_id);
        delivaryCostInformation.setDelivrycost(delivrycost);

        Call<Result> call = apiInterface.sentDelivaryCost(delivaryCostInformation,auth);
//        Log.v("oooooooooo","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("oooooooooo","done");

                Result resource = response.body();


//                Log.v("oooooooooo",response.message());
//                Log.v("oooooooooo","FaildReason = "+resource.getFaildReason());
//                Log.v("oooooooooo",resource.getData()+"");
//                Log.v("oooooooooo",resource.getIsSuccess()+"");

//                AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this).create();
//                alertDialog.setMessage(getString(R.string.success_send_delivary_cost));
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                                startActivity(new Intent(ChatActivity.this,ChatActivity.class));
//                                finish();
//
//                            }
//                        });

                if (status != -1){
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();

                }
//                    alertDialog.show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("oooooooooo","error");
//                Log.v("oooooooooo",t.getMessage());

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage(getString(R.string.check_your_internet));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });
//        Log.v("oooooooooo","finish");

    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void OpenChat(View view) {

        Intent i = new Intent(MainActivity.this, ChatActivity.class);
        i.putExtra("userID",user_.getUser().getUserId());
        i.putExtra("name",user_.getUser().getName());
        startActivity(i);
        drawer.closeMenu();

    }

    public void openContactUs(View view) {
        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
        i.putExtra("userID",user_.getUser().getUserId());
        i.putExtra("name",user_.getUser().getName());
        startActivity(i);
        drawer.closeMenu();

    }
}
