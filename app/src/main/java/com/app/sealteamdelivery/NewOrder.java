package com.app.sealteamdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sealteamdelivery.model.DelivaryCostInformation;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrder extends AppCompatActivity {

    TextView resturantName,resturantPlace,CustomerPlace,orderNumber,orderCost;
    Button btn_change_status,reject;

    private InterfaceRetro apiInterface;

    Data user_;
    int order_id;

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
        setContentView(R.layout.activity_new_order);


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

        SharedPreferences appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("user", "");
        user_ = gson.fromJson(json, Data.class);

        String value = getIntent().getStringExtra("data");

//        String value = data.replace("\"", "");
//        data = data.replace("{", "");
//        data = data.replace("}", "");

        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(value);

//            JSONObject uniObject = mainObject.getJSONObject("university");
//            String  uniName = uniObject.getJsonString("name");
//            String uniURL = uniObject.getJsonString("url");
//
//            JSONObject oneObject = mainObject.getJSONObject("1");
//            String id = oneObject.getJsonString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }



//        value = value.substring(2, value.length()-2);           //remove curly brackets
//        String[] keyValuePairs = value.split("\",\"");              //split the string to creat key-value pairs
//        Map<String,String> map = new HashMap<>();
//
//        for(String pair : keyValuePairs) {
//            Log.v("kkkkkkkkkkkkkkkk", "pair = "+pair);
//            String[] entry = pair.split("\":\"");                   //split the pairs to get key and value
////            String[] entry = pair.split("\":\"");                   //split the pairs to get key and value
//            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
//        }

//        String[] separated = data.split(",");

        resturantName = findViewById(R.id.resturantName);
        resturantPlace = findViewById(R.id.resturantPlace);
        CustomerPlace = findViewById(R.id.CustomerPlace);
        orderNumber = findViewById(R.id.orderNumber);
        orderCost = findViewById(R.id.orderCost);

//        try {
//            resturantName.setText(separated[2].split(":")[1]);
//        }catch (Exception e){}
//



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


        reject = findViewById(R.id.reject);
        btn_change_status = findViewById(R.id.btn_change_status);
        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    changeStatus(user_.getUser().getUserId(), order_id, 0,-1);

                }catch (Exception e){}
                finish();
            }
        });
            btn_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewOrder.this);
                    builder.setTitle(getString(R.string.enter_your_delivary_cost));


                    final EditText input = new EditText(NewOrder.this);
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
                                    startActivity(new Intent(NewOrder.this,MainActivity.class));
                                    finish();
                                }
                                dialog.cancel();
                            } else
                                Toast.makeText(NewOrder.this, getString(R.string.enter_your_delivary_cost), Toast.LENGTH_SHORT).show();
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

    }

    public void changeStatus(final int driver_id, int order_id,int delivrycost,int status){

        DelivaryCostInformation delivaryCostInformation = new DelivaryCostInformation();
        delivaryCostInformation.setDriver_id(driver_id);
        delivaryCostInformation.setResponseStatus(status);
        delivaryCostInformation.setOrder_id(order_id);
        delivaryCostInformation.setDelivrycost(delivrycost);

        Call<Result> call = apiInterface.sentDelivaryCost(delivaryCostInformation,user_.getAccessToken());
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

                AlertDialog alertDialog = new AlertDialog.Builder(NewOrder.this).create();
                alertDialog.setMessage(getString(R.string.success_send_delivary_cost));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            startActivity(new Intent(NewOrder.this,MainActivity.class));
                            finish();

                        }
                    });
                alertDialog.show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("oooooooooo","error");
//                Log.v("oooooooooo",t.getMessage());

                AlertDialog alertDialog = new AlertDialog.Builder(NewOrder.this).create();
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

}
