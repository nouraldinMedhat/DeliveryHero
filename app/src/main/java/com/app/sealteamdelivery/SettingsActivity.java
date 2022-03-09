package com.app.sealteamdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {


    SharedPreferences mPrefs;
    RadioGroup radioLang;
    RadioButton radioEnglish,radioArabic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = (SharedPreferences) getSharedPreferences("pref", MODE_PRIVATE);

        radioLang = findViewById(R.id.radioLang);
        radioEnglish = findViewById(R.id.radioEnglish);
        radioArabic = findViewById(R.id.radioArabic);

        if (mPrefs.getString("lang","").equals("ar")){
            radioArabic.setChecked(true);

        }else {
            radioEnglish.setChecked(true);
        }
    }

    private void changeLang(String languageToLoad){

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
            getResources().getDisplayMetrics());

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("lang", languageToLoad);
        prefsEditor.commit();

        Intent refresh = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    public void saveLang(View view) {

        int checkedRadioButtonId = radioLang.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioArabic){
            changeLang("ar");
        }else {
            changeLang("en");
        }

        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        finish();
    }
}
