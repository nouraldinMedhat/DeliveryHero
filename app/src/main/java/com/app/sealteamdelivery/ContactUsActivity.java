package com.app.sealteamdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.sealteamdelivery.chat.ChatActivity;

public class ContactUsActivity extends AppCompatActivity {

    int userID;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        userID = getIntent().getIntExtra("userID",-1);
        name = getIntent().getStringExtra("name");

    }

    public void OpenChatActivity(View view) {

        Intent i = new Intent(ContactUsActivity.this, ChatActivity.class);
        i.putExtra("userID",userID);
        i.putExtra("name",name);
        startActivity(i);

    }

    public void backApp(View view) {
        finish();
    }

    public void sendMail(View view) {
        String text = "admin@sealteamdelivery.tk";

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(ContactUsActivity.this, "Copied Email", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    ContactUsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ContactUsActivity.this, "Copied Email", Toast.LENGTH_SHORT).show();
        }
    }
}
