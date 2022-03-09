package com.app.sealteamdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.app.sealteamdelivery.model.LoginInformation;
import com.app.sealteamdelivery.model.user.User;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    InterfaceRetro apiInterface;
    TextView user_email,user_passwoed;
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
        setContentView(R.layout.activity_login);

        mPrefs = (SharedPreferences) getSharedPreferences("pref", MODE_PRIVATE);
        user_email = findViewById(R.id.user_email);
        user_passwoed = findViewById(R.id.user_passwoed);

        if (mPrefs.getString("lang","").length() > 0) {
            Locale locale = new Locale(mPrefs.getString("lang",""));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

            try {
                if (mPrefs.getString("lang","").contains("ar")){
                    user_email.setGravity(Gravity.CENTER | Gravity.RIGHT);
                    user_passwoed.setGravity(Gravity.CENTER | Gravity.RIGHT);
                }else {
                    user_email.setGravity(Gravity.CENTER | Gravity.LEFT);
                    user_passwoed.setGravity(Gravity.CENTER | Gravity.LEFT);
                }


            }catch (Exception e){
                user_email.setGravity(Gravity.CENTER | Gravity.LEFT);
                user_passwoed.setGravity(Gravity.CENTER | Gravity.LEFT);

            }
        }else {
            if (getCurrentLocale(this).getLanguage().contains("ar")) {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("lang", "ar");
                prefsEditor.commit();
                user_email.setGravity(Gravity.CENTER | Gravity.RIGHT);
                user_passwoed.setGravity(Gravity.CENTER | Gravity.RIGHT);

                Locale locale = new Locale(mPrefs.getString("lang","ar"));
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,
                        getResources().getDisplayMetrics());


            } else {
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("lang", "en");
                prefsEditor.commit();
                user_email.setGravity(Gravity.CENTER | Gravity.LEFT);
                user_passwoed.setGravity(Gravity.CENTER | Gravity.LEFT);

                Locale locale = new Locale(mPrefs.getString("lang","en"));
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,
                        getResources().getDisplayMetrics());

            }

        }


//        SharedPreferences appSharedPrefs = PreferenceManager
//            .getDefaultSharedPreferences(this.getApplicationContext());
        String json = mPrefs.getString("user", "");

        if (json.equals(""))
            apiInterface = APIClient.getClient().create(InterfaceRetro.class);
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
//        Log.v("aaaaaaaaaaaaaa","onCreate");
//        cancelOrder(1,1);


//        Log.v("aaaaaaaaaaaaaa","0");

//        checkLogin("sealteamdelivery@sealteamdelivery.com","123456789");
//        addLocation(1.2,1.2,1);
//        addDeviceToken(1,"");
//        driver_id1357
//            deviceTokenwwifhgjfo0t34joi53hjfjzbjcbJdjKJDJWBDHJWBEH
    }

    AlertDialog dialog;
    LoginInformation loginInformation;


    public void checkLogin(String email,String password){
//        Log.v("aaaaaaaaaaaaaa","1");

        dialog = new SpotsDialog.Builder().setContext(this).build();
//        Log.v("aaaaaaaaaaaaaa","2");
        dialog.show();

//        Log.v("aaaaaaaaaaaaaa","3");
//        Log.v("aaaaaaaaaaaaaa",email);
//        Log.v("aaaaaaaaaaaaaa",password);

        loginInformation = new LoginInformation();
        loginInformation.setEmail(email);
        loginInformation.setPassword(password);
        Call<User> call = apiInterface.login(loginInformation);
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                dialog.dismiss();

                User resource = response.body();

                if (resource.getIsSuccess() == false || resource.getData().equals(null)){
                    checkYourData();
                    return;
                }

//                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(resource.getData());
                prefsEditor.putString("user", json);
                prefsEditor.putString("auth", resource.getData().getAccessToken());
                prefsEditor.commit();

                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
//                Log.v("aaaaaaaaaaaaaa",response.message());
//                Log.v("aaaaaaaaaaaaaa",resource.getData().getUser().getEmail());


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
//                Log.v("aaaaaaaaaaaaaa","error");
//                Log.v("aaaaaaaaaaaaaa",t.getMessage());
                checkYourInternet();
            }
        });

    }

    public void checkLogin(View view) {
        if (user_email.getText().toString().trim().length() > 6 || user_passwoed.getText().toString().trim().length() > 5)
            checkLogin(user_email.getText().toString().trim(),user_passwoed.getText().toString().trim());
        else
            checkYourData();
    }

    private void checkYourData(){
        new AlertDialog.Builder(LoginActivity.this)
            .setTitle(getString(R.string.error_date))
            .setMessage(getString(R.string.please_check_your_email_and_password))

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
//            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Continue with delete operation
//                }
//            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(getString(R.string.ok), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }
    private void checkYourInternet(){
        new AlertDialog.Builder(LoginActivity.this)
            .setTitle(getString(R.string.no_internet))
            .setMessage(getString(R.string.please_check_your_internet))

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
//            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Continue with delete operation
//                }
//            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(getString(R.string.ok), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }

    public void openBroser(View view) {
        try{
            String url = "https://docs.google.com/forms/d/e/1FAIpQLScvnvdpAF_4JqBN2wVxYpqAFXxzEIOkpnyuG8vacPioboKDcw/viewform";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }catch (Exception e){}
    }

    public void changeLang(View view) {
        CustomDialogClass cdd=new CustomDialogClass(LoginActivity.this);
        cdd.show();

    }
}
