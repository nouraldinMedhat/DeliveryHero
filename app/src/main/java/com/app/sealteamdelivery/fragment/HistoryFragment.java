package com.app.sealteamdelivery.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.support.v4.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.adapters.HistoryAdapter;
import com.app.sealteamdelivery.model.HistoryInformation;
import com.app.sealteamdelivery.model.historyResult.HistoryResult;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {


//    private DatePicker datePicker;
//    private Calendar calendar;
//    private int year, month, day;
//    DatePickerDialog  StartTime;
    private InterfaceRetro apiInterface;
    LinearLayout no_history;
    RecyclerView history_list;

//    ImageView back_day,next_day;
    private TextView dateView;

    Data user_;
    AlertDialog dialog;
    int selected = 0;

    TextView orders_cost,orders_num,app_cost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        user_ = (Data) getArguments().getSerializable("user");

        orders_cost = view.findViewById(R.id.orders_cost);
        app_cost = view.findViewById(R.id.app_cost);
        orders_num = view.findViewById(R.id.orders_num);


        ////////////////////////////////////////////
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        Date date = cal.getTime();
        final String[] days = new String[31];
        days[0] = sdf.format(date);

        for(int i = 1; i < 31; i++){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date = cal.getTime();
            days[i] = sdf.format(date);
        }

//        for(String x: days){
//            System.out.println(x);
//        }
//
//        ////////////////////////////////////////////
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//
//        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//
//        final String[] days = new String[7];
//
//        for (int i = 0; i < 7; i++) {
//            days[i] = format.format(calendar.getTime());
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }



        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        dateView = view.findViewById(R.id.dateView);
//        back_day = view.findViewById(R.id.back_day);
//        next_day = view.findViewById(R.id.next_day);
//
//        if (getCurrentLocale(getContext()).getLanguage().contains("ar")) {
//            next_day.setBackgroundResource(R.drawable.ic_chevron_left_black_24dp);
//            back_day.setBackgroundResource(R.drawable.ic_chevron_right_black_24dp);
//
//        } else {
//            back_day.setBackgroundResource(R.drawable.ic_chevron_left_black_24dp);
//            next_day.setBackgroundResource(R.drawable.ic_chevron_right_black_24dp);
//
//        }
//

        dateView.setText(days[selected]);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
//                                dateboughtbookButton.setText(dayOfMonth + "-"
//                                        + (monthOfYear + 1) + "-" + year);

                                String d=dayOfMonth+"";
                                String m=monthOfYear+"";

                                if (dayOfMonth < 10) d="0"+dayOfMonth+"";
                                if (monthOfYear < 10) m="0"+monthOfYear+"";

                                dateView.setText(year+"-"+m+"-"+d);
                                initData(user_.getUser().getUserId(),year+"-"+m+"-"+d);

                            }
                        }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();

            }
        });
        no_history = view.findViewById(R.id.no_history);
        history_list = view.findViewById(R.id.history_list);
        history_list.setLayoutManager(new LinearLayoutManager(getContext()));


        initData(user_.getUser().getUserId(),days[selected]);
//        back_day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selected --;
//                if (selected < 0)
//                    selected = 6;
//                dateView.setText(days[selected]);
//
//                initData(user_.getUserId(),days[selected]);
//            }
//        });
//
//        next_day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selected ++;
//                if (selected > 6)
//                    selected = 0;
//                dateView.setText(days[selected]);
//
//                initData(user_.getUserId(),days[selected]);
//            }
//        });
//

        return view;
    }
    Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    HistoryResult resource = new HistoryResult();
    private void initData(int driver_id,String date) {
        dialog = new SpotsDialog.Builder().setContext(getContext()).build();
//        Log.v("aaaaaaaaaaaaaa","2");
        dialog.show();

        HistoryInformation historyInformation = new HistoryInformation();
        historyInformation.setDate(date);
        historyInformation.setDriver_id(driver_id);
//        Log.v("aaaaaaaaaaaaaa","3");

        Call<HistoryResult> call = apiInterface.getHistory(historyInformation,user_.getAccessToken());
        call.enqueue(new Callback<HistoryResult>() {
            @Override
            public void onResponse(Call<HistoryResult> call, Response<HistoryResult> response) {


                dialog.dismiss();

                try {
                    resource = response.body();
                    resource.getData().getCurrentBalance();
                    resource.getData().getOrdersCount();

                    if (resource.getData() != null && resource.getData().getOrderHistory().size() > 0) {
                        Log.v("LLLLLLLLLLL", "yes1");
                        no_history.setVisibility(View.GONE);
                        Log.v("LLLLLLLLLLL", "yes2");
                        history_list.setVisibility(View.VISIBLE);
                        Log.v("LLLLLLLLLLL", "yes3");

                        HistoryAdapter adapter = new HistoryAdapter(getContext(), resource.getData().getOrderHistory());
                        Log.v("LLLLLLLLLLL", "yes4");

                        history_list.setAdapter(adapter);
                        Log.v("LLLLLLLLLLL", "yes5");
                        orders_num.setText(resource.getData().getOrdersCount() + "");
                        Log.v("LLLLLLLLLLL", "yes6");
//                        double totalCost = 0;
//                        Log.v("LLLLLLLLLLL", "yes7");
//                        for (int i = 0; i < resource.getData().getOrderHistory().size(); i++) {
//                            totalCost += resource.getData().getOrderHistory().get(i).getDeliveryCost();
//                        }
                        orders_cost.setText(resource.getData().getCurrentBalance() + getContext().getString(R.string.egp));
                        app_cost.setText(resource.getData().getCurrentBalance()/4 + getContext().getString(R.string.egp));
                        Log.v("LLLLLLLLLLL", "yes");
                    } else {
                        Log.v("LLLLLLLLLLL", "1");
                        no_history.setVisibility(View.VISIBLE);
                        Log.v("LLLLLLLLLLL", "2");
                        history_list.setVisibility(View.GONE);
                        Log.v("LLLLLLLLLLL", "3");
                        orders_num.setText(resource.getData().getOrdersCount() + "");
                        Log.v("LLLLLLLLLLL", "4");
                        orders_cost.setText(resource.getData().getCurrentBalance() + getContext().getString(R.string.egp));
                        app_cost.setText(resource.getData().getCurrentBalance()/4 + getContext().getString(R.string.egp));
                        Log.v("LLLLLLLLLLL", "no");
                    }
                }catch (Exception e){
//                    Log.v("LLLLLLLLLLL", "1");
                    no_history.setVisibility(View.VISIBLE);
//                    Log.v("LLLLLLLLLLL", "2");
                    history_list.setVisibility(View.GONE);
//                    Log.v("LLLLLLLLLLL", "3");
//                    orders_num.setText("0");
//                    Log.v("LLLLLLLLLLL", "4");
//                    orders_cost.setText("0" + getContext().getString(R.string.egp));
                    Log.v("LLLLLLLLLLL", "no");

                    try{
                        orders_num.setText(resource.getData().getOrdersCount() + "");
                        orders_cost.setText(resource.getData().getCurrentBalance() + getContext().getString(R.string.egp));
                        app_cost.setText(resource.getData().getCurrentBalance()/4 + getContext().getString(R.string.egp));
                    }catch (Exception ex){
                        orders_num.setText("0");
                        orders_cost.setText("0" + getContext().getString(R.string.egp));
                        app_cost.setText("0" + getContext().getString(R.string.egp));
                    }

                }


            }

            @Override
            public void onFailure(Call<HistoryResult> call, Throwable t) {
                call.cancel();
                dialog.dismiss();

                no_history.setVisibility(View.VISIBLE);
                no_history.setVisibility(View.VISIBLE);
                history_list.setVisibility(View.GONE);
                orders_num.setText("0");
//                orders_cost.setText("0" + getContext().getString(R.string.egp));

            }
        });

    }


}
