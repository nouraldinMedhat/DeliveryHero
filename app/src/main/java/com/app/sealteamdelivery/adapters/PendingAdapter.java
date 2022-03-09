package com.app.sealteamdelivery.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.interfaces.DelivaryInterface;
import com.app.sealteamdelivery.model.DelivaryCostInformation;
import com.app.sealteamdelivery.model.currentorder.PendingOrder;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.network.InterfaceRetro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {

    private List<PendingOrder> currentOrders;
    private LayoutInflater mInflater;
    private InterfaceRetro apiInterface;
    private DelivaryInterface delivaryInterface;
    private Context context;
    private int driver_id;
    String auth;

    // data is passed into the constructor
    public PendingAdapter(Context context, List<PendingOrder> currentOrders, InterfaceRetro apiInterface, DelivaryInterface delivaryInterface,int driver_id,String auth) {
        this.mInflater = LayoutInflater.from(context);
        this.currentOrders = currentOrders;
        this.apiInterface = apiInterface;
        this.delivaryInterface = delivaryInterface;
        this.context = context;
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
            holder.delivary_cost_layout.setVisibility(View.GONE);
        }catch (Exception e){}

        try {
            holder.delivary_status.setVisibility(View.GONE);
        }catch (Exception e){}


        holder.dropped_off.setText(context.getString(R.string.accept_order));
        holder.dropped_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        }else
                            Toast.makeText(context, context.getString(R.string.enter_your_delivary_cost), Toast.LENGTH_SHORT).show();
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });

                builder.show();


//                changeStatus(1357,currentOrders.get(position).getId());
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
        TextView resturantName,resturantPlace,resturantPhone,CustomerName,CustomerPhone,CustomerPlace,orderNumber,orderCost,deliveryCost;
        Button dropped_off;
        LinearLayout delivary_cost_layout,delivary_status,customer_place_layout;

        ViewHolder(View view) {
            super(view);
            resturantName = view.findViewById(R.id.resturantName);
            resturantPlace = view.findViewById(R.id.resturantPlace);
            resturantPhone = view.findViewById(R.id.resturantPhone);
            CustomerName = view.findViewById(R.id.CustomerName);
            CustomerPhone = view.findViewById(R.id.CustomerPhone);
            CustomerPlace = view.findViewById(R.id.CustomerPlace);
            orderNumber = view.findViewById(R.id.orderNumber);
            orderCost = view.findViewById(R.id.orderCost);
            deliveryCost = view.findViewById(R.id.deliveryCost);

            dropped_off = view.findViewById(R.id.dropped_off);
            delivary_cost_layout = view.findViewById(R.id.delivary_cost_layout);
            delivary_status = view.findViewById(R.id.delivary_status);
            customer_place_layout = view.findViewById(R.id.customer_place_layout);

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

    public void sendPriceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.what_is_your_cost));

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_SIGNED);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                m_Text = input.getText().toString();
            }
        });

        builder.show();

    }

}
