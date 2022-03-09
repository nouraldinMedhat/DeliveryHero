package com.app.sealteamdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

public class ChangePasswordActivity extends AppCompatActivity {

    InterfaceRetro apiInterface;

    EditText old_password,new_password,confirm_password;
    Data user_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);

        SharedPreferences appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("user", "");
        user_ = gson.fromJson(json, Data.class);

    }
    AlertDialog dialog;

    public void changeAvailability(int driver_id, String oldpassword, String password, String confirm_password){
        dialog = new SpotsDialog.Builder().setContext(this).build();
//        Log.v("aaaaaaaaaaaaaa","2");
        dialog.show();

        Call<Result> call = apiInterface.changePassword(driver_id, oldpassword, password, confirm_password,user_.getAccessToken());
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                dialog.dismiss();

                try {

                    Log.v("oooooooooooo","done");
                    Log.v("oooooooooooo",response.message());
                    Log.v("oooooooooooo",response.toString());
                    Log.v("oooooooooooo",response.code()+"");
                    if (response.body().getIsSuccess()) {
                        new AlertDialog.Builder(ChangePasswordActivity.this)
                            .setTitle(getString(R.string.change_password))
                            .setMessage(getString(R.string.success_change_password))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    finish();
                                }
                            })

                            .show();

                    }else {
                        new AlertDialog.Builder(ChangePasswordActivity.this)
                            .setTitle(getString(R.string.change_password))
                            .setMessage(getString(R.string.wrong_password))

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
                }catch (Exception e){
                    dialog.dismiss();
                    new AlertDialog.Builder(ChangePasswordActivity.this)
                        .setTitle(getString(R.string.change_password))
                        .setMessage(getString(R.string.wrong_password))

                        .setNegativeButton(getString(R.string.ok), null)

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                new AlertDialog.Builder(ChangePasswordActivity.this)
                    .setTitle(getString(R.string.error_date))
                    .setMessage(getString(R.string.check_your_data))


                    .setNegativeButton(getString(R.string.ok), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            }
        });

    }

    public void changePassword(View view) {
        if (old_password.getText().toString().trim().length() > 5 && new_password.getText().toString().trim().length() > 5 && confirm_password.getText().toString().trim().length() > 5){
            changeAvailability(user_.getUser().getUserId(), old_password.getText().toString().trim(), new_password.getText().toString().trim(), confirm_password.getText().toString().trim());
        }else {
            new AlertDialog.Builder(ChangePasswordActivity.this)
                .setTitle(getString(R.string.short_password))
                .setMessage(getString(R.string.you_enter_short_password))


                .setNegativeButton(getString(R.string.ok), null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (hideSoftKeyboard(ChangePasswordActivity.this))
        super.onBackPressed();

    }
    public boolean hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                return false;
            }
        }

        return true;
    }

    public void closeActivity(View view) {
        finish();
    }
}
