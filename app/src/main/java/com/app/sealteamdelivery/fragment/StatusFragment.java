package com.app.sealteamdelivery.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

//import android.support.v4.app.Fragment;

import androidx.fragment.app.Fragment;

import com.app.sealteamdelivery.MainActivity;
import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.model.AvailableInformation;
import com.app.sealteamdelivery.model.DeviceTokenInformation;
import com.app.sealteamdelivery.model.currentDriver.Driver;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.resultDriverRate.ResultDriverRate;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {


    Data user_;

    ImageView driver_profile;
    TextView driver_name,driver_email,driver_phone,driver_CurrentBalance,driver_busy,driver_working;
    InterfaceRetro apiInterface;
    private RatingBar ratingBar;

    Switch driver_online_offline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        driver_working = view.findViewById(R.id.driver_working);
        driver_busy = view.findViewById(R.id.driver_busy);
        driver_CurrentBalance = view.findViewById(R.id.driver_CurrentBalance);
        driver_profile = view.findViewById(R.id.driver_profile);
        driver_name = view.findViewById(R.id.driver_name);
        driver_email = view.findViewById(R.id.driver_email);
        driver_phone = view.findViewById(R.id.driver_phone);
        ratingBar = view.findViewById(R.id.ratingBar);
        driver_online_offline = view.findViewById(R.id.driver_online_offline);

        driver_working.setText(R.string.not_working);
        driver_working.setTextColor(Color.RED);

        user_ = (Data) getArguments().getSerializable("user");

        Log.v("pppppppid",user_.getUser().getUserId()+"");

        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        try {
            getCurrentDriver(user_.getUser().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("pppppppf",e.getMessage());
        }
        try {
            getResultDriverRate(user_.getUser().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user_ != null) {
            try {

//                Log.v("mmmmmmmmmm","https://api.sealteamdelivery.tk/storage/"+user_.getImage());
//                Log.v("mmmmmmmmmm",user_.getImage());
//                if (!user_.getImage().equals(null) && !user_.getImage().equals(""))
                    Picasso.with(getContext())
                        .load("https://api.sealteamdelivery.tk/storage/"+user_.getUser().getImage())
//                        .placeholder(R.drawable.ic_person_black_24dp)
                        .error(R.drawable.ic_person_black_24dp)
                        .into(driver_profile);

//                else
//                    Picasso.with(getContext())
//                        .load(R.drawable.ic_person_black_24dp)
//                        .error(R.drawable.ic_person_black_24dp)
//                        .into(driver_profile);
            } catch (Exception e) {
                Picasso.with(getContext())
                    .load(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .into(driver_profile);

            }

            try {
                if (!user_.getUser().getName().equals(null))
                    driver_name.setText(user_.getUser().getName());
            }catch (Exception e){}
            try {
                if (!user_.getUser().getEmail().equals(null))
                    driver_email.setText(user_.getUser().getEmail());
            }catch (Exception e){}
            try {
                if (!user_.getUser().getTelephone().equals(null))
                    driver_phone.setText(user_.getUser().getTelephone());

            }catch (Exception e){}

        }

        driver_online_offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    changeAvailability(user_.getUser().getUserId(),"on");
                    try {
                        addDeviceToken(user_.getUser().getUserId(),FirebaseInstanceId.getInstance().getToken());
                        driver_working.setText(R.string.working);
                        driver_working.setTextColor(Color.GREEN);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        driver_working.setText(R.string.not_working);
//                        driver_working.setTextColor(Color.RED);
                    }
                }else {
                    changeAvailability(user_.getUser().getUserId(),"off");
                    try {
                        addDeviceToken(user_.getUser().getUserId(),"");
                        driver_working.setText(R.string.not_working);
                        driver_working.setTextColor(Color.RED);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        driver_working.setText(R.string.working);
//                        driver_working.setTextColor(Color.GREEN);
                    }
                }
            }
        });
        return view;
    }

    public void addDeviceToken(int driver_id,String deviceToken)throws Exception{


        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
        deviceTokenInformation.setDriver_id(driver_id);
        deviceTokenInformation.setDeviceToken(deviceToken);

        Call<Result> call = apiInterface.addDeviceToken(deviceTokenInformation,user_.getAccessToken());
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

    public void getResultDriverRate(int driver_id)throws Exception{


//        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
//        deviceTokenInformation.setDriver_id(driver_id);
//        deviceTokenInformation.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        Call<ResultDriverRate> call = apiInterface.getResultDriverRate(driver_id,user_.getAccessToken());
//        Log.v("vvvvvvvvvvvv","4");
        call.enqueue(new Callback<ResultDriverRate>() {
            @Override
            public void onResponse(Call<ResultDriverRate> call, Response<ResultDriverRate> response) {

//                Log.v("vvvvvvvvvvvv","done");

                ResultDriverRate resource = response.body();

//                Log.v("vvvvvvvvvvvv",response.message());
//                Log.v("vvvvvvvvvvvv","FaildReason = "+resource.getFaildReason());
//                Log.v("vvvvvvvvvvvv",resource.getData().get(0).getRate()+"");
//                Log.v("vvvvvvvvvvvv",resource.getIsSuccess()+"");


                try {
                    int x = (int) (resource.getData().get(0).getRate() * 10);
                    float rate = (float) x / 10;
                    ratingBar.setRating(rate);
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResultDriverRate> call, Throwable t) {
                call.cancel();

//                Log.v("vvvvvvvvvvvv","error");
//                Log.v("vvvvvvvvvvvv",t.getMessage());

            }
        });
//        Log.v("vvvvvvvvvvvv","finish");

    }

    public void changeAvailability(int driver_id,String status){
        Log.v("mmmmmmmmmm",status);


//        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
//        deviceTokenInformation.setDriver_id(driver_id);
//        deviceTokenInformation.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        AvailableInformation availableInformation = new AvailableInformation();
        availableInformation.setDriver_id(driver_id);
        availableInformation.setStatus(status);

        Call<Result> call = apiInterface.changeAvailability(availableInformation,user_.getAccessToken());
        Log.v("mmmmmmmmmm","4");
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

                Log.v("mmmmmmmmmm","error");
                Log.v("mmmmmmmmmm",t.getMessage());

            }
        });
//        Log.v("mmmmmmmmmm","finish");

    }

    public void getCurrentDriver(int driver_id){
//        Log.v("mmmmmmmmmm",status);

        Log.v("ppppppp","n");

        Call<Driver> call = apiInterface.getDriverData(driver_id,user_.getAccessToken());
//        Log.v("mmmmmmmmmm","4");
        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {

//                Log.v("mmmmmmmmmm","done");

                Driver resource = null;
                try {
                    resource = response.body();
                }catch (Exception e){
                    return;
                }
                Log.v("ppppppp","done");
//                Log.v("ppppppp",resource.getIsSuccess()+"");

//                resource.getData().getCurrentOrders().get(0).getCurrentBalance()
//kllllllllllllllllllllllllllllllllllllllllllllllll
//                resource.getData().getCurrentOrders().get(0).getBusy();
//                resource.getData().getCurrentOrders().get(0).getCurrentBalance();



                if (resource != null) {
                    Log.v("ppppppp","not null");

                    try {
                        if (Integer.parseInt(resource.getData().get(0).getCurrentBalance()) >= 4000){
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("برجاء التوجه للشركه لتسديد المستحقات وتفعيل حسابك مره اخري");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "اغلاق",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                            getActivity().finish();
                                        }
                                    });
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    }catch (Exception e){}

                    Log.v("ppppppp","1");

                    try {
                        Log.v("ppppppp","2");
                        if (!user_.getUser().getImage().equals(null) && !user_.getUser().getImage().equals(""))
                        Picasso.with(getContext())
                                    .load("https://api.sealteamdelivery.tk/storage/"+resource.getData().get(0).getImage())
//                                    .placeholder(R.drawable.ic_person_black_24dp)
                                    .error(R.drawable.ic_person_black_24dp)
                                    .into(driver_profile);

                        else
                            Picasso.with(getContext())
                                    .load(R.drawable.ic_person_black_24dp)
                                    .error(R.drawable.ic_person_black_24dp)
                                    .into(driver_profile);
                        Log.v("ppppppp","3");
                    } catch (Exception e) {
                        Picasso.with(getContext())
                                .load(R.drawable.ic_person_black_24dp)
                                .error(R.drawable.ic_person_black_24dp)
                                .into(driver_profile);

                        Log.v("ppppppp","4");
                    }



                    try {
                        driver_name.setText(resource.getData().get(0).getName());
                    }catch (Exception e){}
                    Log.v("ppppppp","eeeeeeee");
                    try {
                        Log.v("ppppppp","wwwweeee");

                        driver_CurrentBalance.setText(resource.getData().get(0).getCurrentBalance()+" "+getString(R.string.egp));
                        Log.v("ppppppp","wwwwwww");
                        Log.v("ppppppp",getString(R.string.current_balance)+resource.getData().get(0).getCurrentBalance());
                    }catch (Exception e){
                        Log.v("ppppppp","error");
                        Log.v("ppppppp",e.getMessage());
                        driver_CurrentBalance.setText("0 "+getString(R.string.egp));

                    }
                    try {
                        if (resource.getData().get(0).getBusy() == 1) {
                            driver_busy.setText(R.string.busy);
                            driver_busy.setTextColor(Color.RED);

                        }else {
                            driver_busy.setText(R.string.not_busy);
                            driver_busy.setTextColor(Color.GREEN);
                        }
                    }catch (Exception e){
                        Log.v("ppppppp2",e.getMessage());

                    }
                    try {
                        driver_phone.setText(resource.getData().get(0).getTelephone());

                    }catch (Exception e){}

                    try{
                        switch (resource.getData().get(0).getAvailability()){
                            case "on":
                                driver_online_offline.setChecked(true);
                                driver_working.setText(R.string.working);
                                driver_working.setTextColor(Color.GREEN);
                                try {
                                    addDeviceToken(user_.getUser().getUserId(),FirebaseInstanceId.getInstance().getToken());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "off":
                                driver_online_offline.setChecked(false);
                                driver_working.setText(R.string.not_working);
                                driver_working.setTextColor(Color.RED);
                                try {
                                    addDeviceToken(user_.getUser().getUserId(),"");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }   break;
                        }
                    }catch (Exception e){}

                    try{
                        int x = (int) (resource.getData().get(0).getRate()*10);
                        float rate = (float) x / 10;
                        ratingBar.setRating(rate);

                    }catch (Exception e){}
                }
                    Log.v("ppppppp","equl null");

//                Log.v("mmmmmmmmmm",response.message());
//                Log.v("mmmmmmmmmm","FaildReason = "+resource.getFaildReason());
//                Log.v("mmmmmmmmmm",resource.getIsSuccess()+"");


            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                call.cancel();

                Log.v("ppppppp","error");
                Log.v("ppppppp",t.getMessage());

            }
        });
//        Log.v("mmmmmmmmmm","finish");

    }


    @Override
    public void onResume() {
        super.onResume();

        if (driver_online_offline.isChecked()){
            driver_working.setText(R.string.working);
            driver_working.setTextColor(Color.GREEN);
        }else {
            driver_working.setText(R.string.not_working);
            driver_working.setTextColor(Color.RED);
        }
    }
}
