package com.app.sealteamdelivery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialogClass extends Dialog implements
    android.view.View.OnClickListener {

  public Activity c;

  Button saveLang;

  SharedPreferences mPrefs;
  RadioGroup radioLang;
  RadioButton radioEnglish,radioArabic;

  public CustomDialogClass(Activity a) {
    super(a);
    // TODO Auto-generated constructor stub
    this.c = a;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_settings);
    saveLang = (Button) findViewById(R.id.saveLang);
    saveLang.setOnClickListener(this);

    mPrefs = (SharedPreferences) c.getSharedPreferences("pref", MODE_PRIVATE);

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
    c.getResources().updateConfiguration(config,
            c.getResources().getDisplayMetrics());

    SharedPreferences.Editor prefsEditor = mPrefs.edit();
    prefsEditor.putString("lang", languageToLoad);
    prefsEditor.commit();

    Intent refresh = new Intent(c, LoginActivity.class);
    c.startActivity(refresh);
    c.finish();
  }

  public void saveLang() {

    int checkedRadioButtonId = radioLang.getCheckedRadioButtonId();
    if (checkedRadioButtonId == R.id.radioArabic){
      changeLang("ar");
    }else {
      changeLang("en");
    }

    c.startActivity(new Intent(c,LoginActivity.class));
    c.finish();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.saveLang:
      saveLang();
      dismiss();
      break;
    default:
      break;
    }
    dismiss();
  }
}