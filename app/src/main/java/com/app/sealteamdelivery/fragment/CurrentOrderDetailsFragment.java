package com.app.sealteamdelivery.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v4.app.Fragment;

import androidx.fragment.app.Fragment;

import com.app.sealteamdelivery.MainActivity;
import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.model.ChangeStatusInformation;
import com.app.sealteamdelivery.model.currentorder.CurrentOrder;
import com.app.sealteamdelivery.model.rateInfo.RateInfo;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CurrentOrderDetailsFragment extends Fragment {

    CurrentOrder currentOrders;

    TextView resturantName,resturantPlace,resturantPhone,CustomerName,CustomerPhone,CustomerPlace,orderNumber,orderCost,deliveryCost;
    Button btn_change_status;
    ImageView iconPhone,customerPhone_icon,shopPlace_icon,order_number_copy_icon;

    InterfaceRetro apiInterface;
    Data user_;
    AlertDialog dialog;

    LinearLayout call_layout,shopPlace_layout;
    LinearLayout customer_name_layout,customer_phone_layout,delivary_cost_layout;
    SharedPreferences mPrefs ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_order_details, container, false);

        currentOrders = (CurrentOrder) getArguments().getSerializable("currentOrders");
//        user_ = (User_) getArguments().getSerializable("user");
        dialog = new SpotsDialog.Builder().setContext(getContext()).build();
        mPrefs = (SharedPreferences) getActivity().getSharedPreferences("pref", MODE_PRIVATE);

//        SharedPreferences appSharedPrefs = PreferenceManager
//            .getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");
        user_ = gson.fromJson(json, Data.class);

        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        call_layout = view.findViewById(R.id.call_layout);
        shopPlace_layout = view.findViewById(R.id.shopPlace_layout);
        delivary_cost_layout = view.findViewById(R.id.delivary_cost_layout);

        iconPhone = view.findViewById(R.id.iconPhone);
        customerPhone_icon = view.findViewById(R.id.customerPhone_icon);
        shopPlace_icon = view.findViewById(R.id.shopPlace_icon);
        order_number_copy_icon = view.findViewById(R.id.order_number_copy_icon);

        resturantName = view.findViewById(R.id.resturantName);
        resturantPlace = view.findViewById(R.id.resturantPlace);
        resturantPhone = view.findViewById(R.id.resturantPhone);
        CustomerName = view.findViewById(R.id.CustomerName);
        CustomerPhone = view.findViewById(R.id.CustomerPhone);
        CustomerPlace = view.findViewById(R.id.CustomerPlace);
        orderNumber = view.findViewById(R.id.orderNumber);
        orderCost = view.findViewById(R.id.orderCost);
        deliveryCost = view.findViewById(R.id.deliveryCost);

        customer_name_layout = view.findViewById(R.id.customer_name_layout);
        customer_phone_layout = view.findViewById(R.id.customer_phone_layout);
        delivary_cost_layout = view.findViewById(R.id.delivary_cost_layout);

        btn_change_status = view.findViewById(R.id.btn_change_status);



        try {
            resturantName.setText(currentOrders.getResturantName());
        }catch (Exception e){}


        try {
            resturantPlace.setText(currentOrders.getResturantslocation());
        }catch (Exception e){}


        try {
            resturantPhone.setText(currentOrders.getResturantsTelephone());
        }catch (Exception e){}


        try {
            CustomerName.setText(currentOrders.getCustomerName());
        }catch (Exception e){}


        try {
            CustomerPhone.setText(currentOrders.getCustomerPhone());
        }catch (Exception e){}


        try {
            CustomerPlace.setText(currentOrders.getOrderDest());
        }catch (Exception e){}


        try {
            orderNumber.setText(currentOrders.getId()+"");
        }catch (Exception e){}


        try {
            orderCost.setText(currentOrders.getOrderCost()+ getString(R.string.egp));
        }catch (Exception e){}


        try {
            deliveryCost.setText(currentOrders.getDeliveryCost()+ getString(R.string.egp));
        }catch (Exception e){}


        switch (currentOrders.getStatus()){
            case "1":
                btn_change_status.setEnabled(true);
                btn_change_status.setText(getContext().getString(R.string.arrive_resturant));
                break;

            case "2":
                btn_change_status.setEnabled(true);
                btn_change_status.setText(getContext().getString(R.string.received_the_request));
                break;

            case "3":
                btn_change_status.setEnabled(true);
                btn_change_status.setText(getContext().getString(R.string.delivered_the_order));
                break;
            case "4":
//                new AlertDialog.Builder(getContext())
//                    .setTitle("Success")
//                    .setMessage("Finish Delivered Order")
//
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(getContext(), ChatActivity.class));
//                        }
//                    })
//
//                    .show();
                rateDialog();
                btn_change_status.setEnabled(false);
                btn_change_status.setVisibility(View.GONE);
                break;

            case "-2":
                btn_change_status.setText(getContext().getString(R.string.order_canceled));
                btn_change_status.setEnabled(false);
                break;
            case "0":
                btn_change_status.setText(getContext().getString(R.string.order_pending));
                btn_change_status.setEnabled(false);
                break;

        }

        btn_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Log.v("vvvvvvvvvv","currentOrders status = "+currentOrders.getStatus());

                    if (currentOrders.getStatus().equals(""+4)){
//                        new AlertDialog.Builder(getContext())
//                            .setTitle("Success")
//                            .setMessage("Finish Delivered Order")
//
//                            // Specifying a listener allows you to take an action before dismissing the dialog.
//                            // The dialog is automatically dismissed when a dialog button is clicked.
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                    fragmentTransaction.replace(R.id.navigation_placeholder, new StatusFragment());
//                                    fragmentTransaction.addToBackStack(null).commit();
//                                }
//                            })
//
//                            .show();
                        rateDialog();
                        return;
                    }
                    changeStatus(user_.getUser().getUserId(), Integer.parseInt(currentOrders.getStatus())+1,currentOrders.getId());
                }catch (Exception e){
//                    Log.v("vvvvvvvvvv","error");
//                    Log.v("vvvvvvvvvv",e.getMessage());

                }
            }
        });


        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getResturantsTelephone());
                }catch (Exception e){}
            }
        });

        resturantPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getResturantsTelephone());
                }catch (Exception e){}
            }
        });

        iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getResturantsTelephone());
                }catch (Exception e){}
            }
        });


        customer_phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getCustomerPhone());
                }catch (Exception e){}
            }
        });

        customerPhone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getCustomerPhone());
                }catch (Exception e){}
            }
        });

        CustomerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callShop(currentOrders.getCustomerPhone());
                }catch (Exception e){}
            }
        });

        shopPlace_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    destShop(currentOrders.getResturantslat(),currentOrders.getResturantslng());
                }catch (Exception e){}
            }
        });

        resturantPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    destShop(currentOrders.getResturantslat(),currentOrders.getResturantslng());
                }catch (Exception e){}
            }
        });

        shopPlace_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    destShop(currentOrders.getResturantslat(),currentOrders.getResturantslng());
                }catch (Exception e){}
            }
        });

        order_number_copy_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = orderNumber.getText().toString();

                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                            getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(text);
                    Toast.makeText(getActivity(), "Copied Text", Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Copied Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void callShop(String customerPhone) throws Exception{

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+customerPhone));
        startActivity(intent);

    }

    private void destShop(String lat,String lon) throws Exception{

        String url = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon + "&travelmode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);



    }

    public void changeStatus(final int driver_id, final int status, int order_id){
//        Log.v("vvvvvvvvvv","changeStatus");
//        Log.v("vvvvvvvvvv","status = "+status);

        ChangeStatusInformation changeStatusInformation = new ChangeStatusInformation();
        changeStatusInformation.setDriver_id(driver_id);
        changeStatusInformation.setStatus(status);
        changeStatusInformation.setOrder_id(order_id);
        dialog.show();

//        Call<Result> call = apiInterface.changeStatus(changeStatusInformation);
        Call<Result> call = apiInterface.changeStatus(driver_id,status,order_id,user_.getAccessToken());
//        Log.v("vvvvvvvvvv","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("vvvvvvvvvv","done");

                Result resource = response.body();

//                Log.v("vvvvvvvvvv",response.message());
//                Log.v("vvvvvvvvvv","FaildReason = "+resource.getFaildReason());
//                Log.v("vvvvvvvvvv",resource.getData()+"");
//                Log.v("vvvvvvvvvv",resource.getIsSuccess()+"");

                if (resource.getIsSuccess().equals(true)){
                    currentOrders.setStatus(status+"");
//                    Log.v("vvvvvvvvvv","new status = "+currentOrders.getStatus());
                    switch (currentOrders.getStatus()){


                        case "0":
                            delivary_cost_layout.setVisibility(View.GONE);
                            customer_name_layout.setVisibility(View.GONE);
                            customer_phone_layout.setVisibility(View.GONE);
                            break;


                        case "1":
                            btn_change_status.setText(getContext().getString(R.string.arrive_resturant));
                            delivary_cost_layout.setVisibility(View.GONE);
                            customer_name_layout.setVisibility(View.GONE);
                            customer_phone_layout.setVisibility(View.GONE);
                            break;

                        case "2":
                            btn_change_status.setText(getContext().getString(R.string.received_the_request));
                            delivary_cost_layout.setVisibility(View.GONE);
                            customer_name_layout.setVisibility(View.GONE);
                            customer_phone_layout.setVisibility(View.GONE);
                            break;

                        case "3":
                            btn_change_status.setText(getContext().getString(R.string.delivered_the_order));
                            delivary_cost_layout.setVisibility(View.VISIBLE);
                            customer_name_layout.setVisibility(View.VISIBLE);
                            customer_phone_layout.setVisibility(View.VISIBLE);
                            break;

                        case "4":
//                            new AlertDialog.Builder(getContext())
//                                .setTitle("Success")
//                                .setMessage("Finish Delivered Order")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent(getContext(), ChatActivity.class));
//                                    }
//                                })
//
//                                .show();
                            try {
                                rateDialog();
                            }catch (Exception e){}
                            btn_change_status.setText(getContext().getString(R.string.finish));
                            break;
                    }
                    dialog.dismiss();

                }else {
                    new AlertDialog.Builder(getContext())
                        .setTitle(getContext().getString(R.string.failure))
                        .setMessage(getContext().getString(R.string.something_error_happened))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        })

                        .show();
                }

//                Log.v("vvvvvvvvvv","**********************************************************");

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("vvvvvvvvvv","error");
//                Log.v("vvvvvvvvvv",t.getMessage());

                dialog.dismiss();

                new AlertDialog.Builder(getContext())
                    .setTitle(getContext().getString(R.string.failure))
                    .setMessage(getContext().getString(R.string.something_error_happened))

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    })

                    .show();
            }
        });
//        Log.v("vvvvvvvvvv","finish");

    }


    private void rateDialog(){
        Dialog rate = new Dialog(getActivity());
        rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rate.setContentView(R.layout.rate_dialog);
        Button Submit = (Button)rate.findViewById(R.id.Submit);
        final RatingBar ratingBar = (RatingBar)rate.findViewById(R.id.ratingsBar);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() > 0){
                    rateResturant(user_.getUser().getUserId(),currentOrders.getId(),ratingBar.getRating());
                }else
                    Toast.makeText(getContext(), getContext().getString(R.string.please_rate_resturant), Toast.LENGTH_SHORT).show();
            }
        });
        rate.show();

    }
    public void rateResturant(int driver_id,int order_id, double rate){

        RateInfo rateInfo = new RateInfo();
        rateInfo.setDriver_id(driver_id);
        rateInfo.setRate(rate);
        rateInfo.setOrder_id(order_id);

        Call<Result> call = apiInterface.rateResturant(rateInfo,user_.getAccessToken());
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("aaaaaaaaaaaaaa","done");

                new AlertDialog.Builder(getContext())
                    .setTitle(getContext().getString(R.string.finished))
                    .setMessage(getContext().getString(R.string.thank_you_for_using_our_app))

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    })

                    .show();


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
