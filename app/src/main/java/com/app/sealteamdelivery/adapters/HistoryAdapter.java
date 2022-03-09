package com.app.sealteamdelivery.adapters;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.MainActivity;
import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.fragment.CurrentOrderDetailsFragment;
import com.app.sealteamdelivery.fragment.HistoryOrderDetailsFragment;
import com.app.sealteamdelivery.model.historyResult.OrderHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<OrderHistory> historyResults;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public HistoryAdapter(Context context, List<OrderHistory> historyResults) {
        this.mInflater = LayoutInflater.from(context);
        this.historyResults = historyResults;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {





        try {
            holder.orderNumber.setText(historyResults.get(position).getId()+"");
        }catch (Exception e){}


        switch (historyResults.get(position).getStatus()){
            case "1":
                holder.deliverystatus.setText(context.getString(R.string.start_order));
                break;

            case "2":
                holder.deliverystatus.setText(context.getString(R.string.arrive_resturant));
                break;

            case "3":
                holder.deliverystatus.setText(context.getString(R.string.received_the_request));
                break;
            case "-2":
                holder.deliverystatus.setText(context.getString(R.string.order_canceled));
                break;
            case "4":
                holder.deliverystatus.setText(context.getString(R.string.order_finished));
                break;
            case "0":
                holder.deliverystatus.setText(context.getString(R.string.order_pending));
                break;
        }



//        holder.dropped_off.setVisibility(View.GONE);
        holder.dropped_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryOrderDetailsFragment currentOrderDetailsFragment = new HistoryOrderDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentOrders", historyResults.get(position));
                bundle.putInt("flag", 1);

                currentOrderDetailsFragment.setArguments(bundle);

                ((MainActivity)context).showFragment(currentOrderDetailsFragment);


            }



        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return historyResults.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber,deliverystatus;
        Button dropped_off;

        ViewHolder(View view) {
            super(view);
            deliverystatus = view.findViewById(R.id.deliverystatus);
            orderNumber = view.findViewById(R.id.orderNumber);

            dropped_off = view.findViewById(R.id.dropped_off);

        }

    }

}
