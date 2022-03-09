package com.app.sealteamdelivery.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.MainActivity;
import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.fragment.CurrentOrderDetailsFragment;
import com.app.sealteamdelivery.interfaces.DelivaryInterface;
import com.app.sealteamdelivery.model.DelivaryCostInformation;
import com.app.sealteamdelivery.model.currentorder.CurrentOrder;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.network.InterfaceRetro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static.Callback;

public class DelivariesAdapter extends RecyclerView.Adapter<DelivariesAdapter.ViewHolder> {

    private List<CurrentOrder> currentOrders;
    private LayoutInflater mInflater;
    private Context context;
    private InterfaceRetro apiInterface;
    int driver_id;
    private DelivaryInterface delivaryInterface;
    String auth;

    // data is passed into the constructor
    public DelivariesAdapter(Context context, List<CurrentOrder> currentOrders, InterfaceRetro apiInterface, DelivaryInterface delivaryInterface, int driver_id,String auth) {
        this.mInflater = LayoutInflater.from(context);
        this.currentOrders = currentOrders;
        this.context = context;

        this.apiInterface = apiInterface;
        this.delivaryInterface = delivaryInterface;

        this.driver_id = driver_id;
        this.auth = auth;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.delivery_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        try {
            holder.resturantName.setText(currentOrders.get(position).getResturantName());
        }catch (Exception e){}


        try {
            holder.resturantPlace.setText(currentOrders.get(position).getResturantslocation());
        }catch (Exception e){}


        try {
            holder.resturantPhone.setText(currentOrders.get(position).getResturantsTelephone());
        }catch (Exception e){}


        try {
            holder.CustomerName.setText(currentOrders.get(position).getCustomerName());
        }catch (Exception e){}


        try {
            holder.CustomerPhone.setText(currentOrders.get(position).getCustomerPhone());
        }catch (Exception e){}


        try {
            holder.CustomerPlace.setText(currentOrders.get(position).getOrderDest());
        }catch (Exception e){}


        try {
            holder.orderNumber.setText(currentOrders.get(position).getId()+"");
        }catch (Exception e){}


        try {
            holder.orderCost.setText(currentOrders.get(position).getOrderCost()+ context.getString(R.string.egp));
        }catch (Exception e){}


        try {
            holder.deliveryCost.setText(currentOrders.get(position).getDeliveryCost()+ context.getString(R.string.egp));
        }catch (Exception e){}
        holder.dropped_off.setText(""+context.getString(R.string.order_details));

        switch (currentOrders.get(position).getStatus()){
            case "1":
                holder.deliverystatus.setText(context.getString(R.string.start_order));
                holder.delivary_cost_layout.setVisibility(View.GONE);
                holder.customer_name_layout.setVisibility(View.GONE);
                holder.customer_phone_layout.setVisibility(View.GONE);
                break;

            case "2":
                holder.deliverystatus.setText(context.getString(R.string.arrive_resturant));
                holder.delivary_cost_layout.setVisibility(View.GONE);
                holder.customer_name_layout.setVisibility(View.GONE);
                holder.customer_phone_layout.setVisibility(View.GONE);
                break;

            case "3":
                holder.deliverystatus.setText(context.getString(R.string.received_the_request));
                holder.delivary_cost_layout.setVisibility(View.VISIBLE);
                holder.customer_name_layout.setVisibility(View.VISIBLE);
                holder.customer_phone_layout.setVisibility(View.VISIBLE);
                break;
            case "-2":
                holder.deliverystatus.setText(context.getString(R.string.order_canceled));
                break;
            case "4":
                holder.deliverystatus.setText(context.getString(R.string.order_finished));
                break;
            case "0":
                holder.deliverystatus.setText(context.getString(R.string.order_pending));
                holder.dropped_off.setText(""+context.getString(R.string.accept));

                holder.delivary_cost_layout.setVisibility(View.GONE);
                holder.customer_name_layout.setVisibility(View.GONE);
                holder.customer_phone_layout.setVisibility(View.GONE);
                holder.customer_place_layout.setVisibility(View.GONE);
                break;
        }


        holder.dropped_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentOrders.get(position).getStatus().equals("0")){


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.enter_your_delivary_cost));


                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    builder.setView(input);
                    builder.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        m_Text = input.getText().toString();
                            if (input.getText().toString().trim().length() > 0){
                                changeStatus(driver_id,currentOrders.get(position).getId(),Integer.parseInt(input.getText().toString().trim()));
                                dialog.cancel();

                                context.startActivity(new Intent(context,MainActivity.class));

                            }else
                                Toast.makeText(context, context.getString(R.string.enter_your_delivary_cost), Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();


                    return;
                }
                CurrentOrderDetailsFragment currentOrderDetailsFragment = new CurrentOrderDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentOrders", currentOrders.get(position));

                currentOrderDetailsFragment.setArguments(bundle);

                ((MainActivity)context).showFragment(currentOrderDetailsFragment);


            }



        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return currentOrders.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView resturantName,resturantPlace,resturantPhone,CustomerName,CustomerPhone,CustomerPlace,orderNumber,orderCost,deliveryCost,deliverystatus;
        Button dropped_off;
        LinearLayout customer_name_layout,customer_phone_layout,customer_place_layout,delivary_cost_layout;

        ViewHolder(View view) {
            super(view);
            customer_name_layout = view.findViewById(R.id.customer_name_layout);
            customer_phone_layout = view.findViewById(R.id.customer_phone_layout);
            customer_place_layout = view.findViewById(R.id.customer_place_layout);
            delivary_cost_layout = view.findViewById(R.id.delivary_cost_layout);

            resturantName = view.findViewById(R.id.resturantName);
            resturantPlace = view.findViewById(R.id.resturantPlace);
            resturantPhone = view.findViewById(R.id.resturantPhone);
            CustomerName = view.findViewById(R.id.CustomerName);
            CustomerPhone = view.findViewById(R.id.CustomerPhone);
            CustomerPlace = view.findViewById(R.id.CustomerPlace);
            orderNumber = view.findViewById(R.id.orderNumber);
            orderCost = view.findViewById(R.id.orderCost);
            deliveryCost = view.findViewById(R.id.deliveryCost);
            deliverystatus = view.findViewById(R.id.deliverystatus);

            dropped_off = view.findViewById(R.id.dropped_off);

        }

    }

    public void changeStatus(final int driver_id, int order_id,int delivrycost){

        DelivaryCostInformation delivaryCostInformation = new DelivaryCostInformation();
        delivaryCostInformation.setDriver_id(driver_id);
        delivaryCostInformation.setResponseStatus(1);
        delivaryCostInformation.setOrder_id(order_id);
        delivaryCostInformation.setDelivrycost(delivrycost);

        Call<Result> call = apiInterface.sentDelivaryCost(delivaryCostInformation,auth);
//        Log.v("oooooooooo","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("oooooooooo","done");

                Result resource = response.body();

                delivaryInterface.update(driver_id);

//                Log.v("oooooooooo",response.message());
//                Log.v("oooooooooo","FaildReason = "+resource.getFaildReason());
//                Log.v("oooooooooo",resource.getData()+"");
//                Log.v("oooooooooo",resource.getIsSuccess()+"");

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage(context.getString(R.string.success_send_delivary_cost));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

//                            AppCompatActivity activity = (AppCompatActivity) context;

//                            ((ChatActivity)context).showFragment(new StatusFragment());
//                            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(R.id.navigation_placeholder, new StatusFragment());
//                            fragmentTransaction.addToBackStack(null).commit();

                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("oooooooooo","error");
//                Log.v("oooooooooo",t.getMessage());

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage(context.getString(R.string.check_your_internet));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok),
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
