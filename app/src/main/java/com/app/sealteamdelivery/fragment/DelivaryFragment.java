package com.app.sealteamdelivery.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.adapters.DelivariesAdapter;
import com.app.sealteamdelivery.interfaces.DelivaryInterface;
import com.app.sealteamdelivery.model.currentorder.CurrentOrder;
import com.app.sealteamdelivery.model.currentorder.CurrentOrders;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelivaryFragment extends Fragment implements DelivaryInterface {



    LinearLayout no_deliveries;
    RecyclerView deliveries_list;
//    RecyclerView pending_list;
    InterfaceRetro apiInterface;
    AlertDialog dialog;
    DelivariesAdapter adapter;
//    PendingAdapter adapter2;
    RecyclerView recyclerView;
    RadioGroup toggle;
    CurrentOrders resource = null;

    Data user_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliveries, container, false);

        no_deliveries = view.findViewById(R.id.no_deliveries);
        deliveries_list = view.findViewById(R.id.deliveries_list);
//        pending_list = view.findViewById(R.id.pending_list);
        toggle = view.findViewById(R.id.toggle);

        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        user_ = (Data) getArguments().getSerializable("user");
        getCurrentOrder(user_.getUser().getUserId());

        recyclerView = view.findViewById(R.id.deliveries_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        pending_list.setLayoutManager(new LinearLayoutManager(getContext()));


//        toggle.check(R.id.current_order_btn);;
//        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // checkedId is the RadioButton selected
//                if (resource != null){
//                    if (checkedId == R.id.current_order_btn){
//                        deliveries_list.setVisibility(View.VISIBLE);
//                        pending_list.setVisibility(View.GONE);
//                    }else if (checkedId == R.id.pending_order_btn){
//                        deliveries_list.setVisibility(View.GONE);
//                        pending_list.setVisibility(View.VISIBLE);
//
//                    }
//                }
//            }
//        });

        return view;
    }


    public void getCurrentOrder(int driver_id){
//        Log.v("aaaaaaaaaaaaaa","1");

        dialog = new SpotsDialog.Builder().setContext(getContext()).build();
//        Log.v("aaaaaaaaaaaaaa","2");
        dialog.show();

//        Log.v("aaaaaaaaaaaaaa","3");


        Call<CurrentOrders> call = apiInterface.currentOrders(driver_id,user_.getAccessToken());
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<CurrentOrders>() {
            @Override
            public void onResponse(Call<CurrentOrders> call, Response<CurrentOrders> response) {


                dialog.dismiss();

                resource = response.body();

                try {
                    if (resource.getData().getCurrentOrders().size() > 0 ) {

                        no_deliveries.setVisibility(View.GONE);
                        deliveries_list.setVisibility(View.VISIBLE);
//                        toggle.setVisibility(View.VISIBLE);
                        initRecuclerview(resource.getData().getCurrentOrders());
//                        initPendingList(resource.getData().getPendingOrders());
                    }
                }catch (Exception e){}

//                try {
//                    if (resource.getData().getPendingOrders().size() > 0) {
//
//                        no_deliveries.setVisibility(View.GONE);
//                        deliveries_list.setVisibility(View.VISIBLE);
////                        toggle.setVisibility(View.VISIBLE);
////                        initRecuclerview(resource.getData().getCurrentOrders());
//                        initPendingList(resource.getData().getPendingOrders());
//                    }
//                }catch (Exception e){}
//                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//                Gson gson = new Gson();
//                String json = gson.toJson(resource.getData().getUser());
//                prefsEditor.putString("user", json);
//                prefsEditor.commit();
//
//                startActivity(new Intent(LoginActivity.this,ChatActivity.class));

//                Log.v("aaaaaaaaaaaaaa",response.message());
//                Log.v("sssssssssssssssss","resource.getData() = "+resource.getData().getPendingOrders().size());
//                Log.v("aaaaaaaaaaaaaa",resource.getData().getCurrentOrders().get(0).getCustomerName());


            }

            @Override
            public void onFailure(Call<CurrentOrders> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
//                Log.v("aaaaaaaaaaaaaa","error");
//                Log.v("aaaaaaaaaaaaaa",t.getMessage());
                no_deliveries.setVisibility(View.VISIBLE);
                deliveries_list.setVisibility(View.GONE);
                toggle.setVisibility(View.GONE);

            }
        });

    }

    private void initRecuclerview(List<CurrentOrder> currentOrders) {

        adapter = new DelivariesAdapter(getContext(), currentOrders,apiInterface,this,user_.getUser().getUserId(),user_.getAccessToken());

        recyclerView.setAdapter(adapter);

    }

//    private void initPendingList(List<PendingOrder> pendingOrders) {
//
//        adapter2 = new PendingAdapter(getContext(), pendingOrders,apiInterface,this,user_.getUserId());
//
//        pending_list.setAdapter(adapter2);
//
//    }

    @Override
    public void update(int driver_id) {
        getCurrentOrder(driver_id);
        toggle.check(R.id.current_order_btn);;
    }
}
