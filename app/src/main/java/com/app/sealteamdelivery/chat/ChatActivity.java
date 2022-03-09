package com.app.sealteamdelivery.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sealteamdelivery.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSend;
    private EditText edtMessage;
    private RecyclerView rvMessage;

    private AppPreference mAppPreference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    int userID;
    String name;

    int f = 0;
    private static final int  PICK_FROM_GALLARY = 1001;
    DatabaseReference mCounterRef;
    private FirebaseRecyclerAdapter<Message, ChatViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userID = getIntent().getIntExtra("userID",-1);
        name = getIntent().getStringExtra("name");

        btnSend =  (ImageButton)findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        edtMessage = (EditText) findViewById(R.id.edt_message);
        rvMessage = (RecyclerView) findViewById(R.id.rv_chat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

//        rvMessage.setHasFixedSize(true);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setSmoothScrollbarEnabled(true);
//        layoutManager.setReverseLayout(true);

        rvMessage.setLayoutManager(layoutManager);
//        rvMessage.scrollToPosition(0);
        mAppPreference = new AppPreference(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();




        mCounterRef = mDatabaseReference.child("Chats").child(userID+"").child("adminUnreadMessages");
        // listen for single change
        mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    mDatabaseReference.child("Chats").child(userID+"").child("name").setValue(name);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("type").setValue("driver");  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("unReadUserMessage").setValue(0);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("user_id").setValue(userID);  // <= Change to ++count
                }catch (Exception e){
                    mDatabaseReference.child("Chats").child(userID+"").child("adminUnreadMessages").setValue(0);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("name").setValue(name);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("type").setValue("driver");  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("unReadUserMessage").setValue(0);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("user_id").setValue(userID);  // <= Change to ++count

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // throw an error if setValue() is rejected
//                throw databaseError.toException();
                try{
                    mDatabaseReference.child("Chats").child(userID+"").child("adminUnreadMessages").setValue(0);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("name").setValue(name);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("type").setValue("driver");  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("unReadUserMessage").setValue(0);  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("user_id").setValue(userID);  // <= Change to ++count

                }catch (Exception e){}
            }
        });









        adapter = new FirebaseRecyclerAdapter<Message, ChatViewHolder>(
                Message.class,
                R.layout.item_row_chat,
                ChatViewHolder.class,
                mDatabaseReference.child("messages").child(userID+"")
        ) {

            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Message model, int position) {
                if (model.sender.equals(name)) {
                    viewHolder.tvMessage.setVisibility(View.VISIBLE);
                    viewHolder.tvMessage.setBackgroundResource(R.drawable.msg_sender);
                    viewHolder.iv_image.setBackgroundResource(R.drawable.msg_sender);
                    viewHolder.tvMessage.setBackgroundResource(R.drawable.msg_sender);
                    viewHolder.tvMessage.setTextColor(Color.WHITE);
                    viewHolder.tv_time.setText(model.time);

                    viewHolder.chat_parent.setGravity(Gravity.CENTER | Gravity.RIGHT);
//                    viewHolder.tvMessage.setGravity(Gravity.CENTER | Gravity.RIGHT);
//                    viewHolder.iv_image.setGravity(Gravity.CENTER | Gravity.RIGHT);
//                    viewHolder.tv_time.setGravity(Gravity.CENTER | Gravity.RIGHT);
                }else {
                    viewHolder.tvMessage.setBackgroundResource(R.drawable.msg_recever);
                    viewHolder.iv_image.setBackgroundResource(R.drawable.msg_recever);
                    viewHolder.tvMessage.setTextColor(Color.BLACK);
                    viewHolder.tv_time.setText(model.time+" | "+model.sender);

                    viewHolder.chat_parent.setGravity(Gravity.CENTER | Gravity.LEFT);
//                    viewHolder.tvMessage.setGravity(Gravity.CENTER | Gravity.LEFT);
//                    viewHolder.iv_image.setGravity(Gravity.CENTER | Gravity.RIGHT);
//                    viewHolder.tv_time.setGravity(Gravity.CENTER | Gravity.LEFT);
                }
//                viewHolder.tvEmail.setText(model.sender);
                if (model.messageType.equals("text")){
                    viewHolder.tvMessage.setText(model.body);
                    viewHolder.iv_image.setVisibility(View.GONE);
                    viewHolder.tvMessage.setVisibility(View.VISIBLE);
                } else{
//                    Bitmap bitmap = BitmapFactory.decodeFile(model.body);
                    viewHolder.tvMessage.setVisibility(View.GONE);
                    viewHolder.iv_image.setVisibility(View.VISIBLE);

                    try {
                        byte[] decodedString = Base64.decode(model.body, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        viewHolder.iv_image.setImageBitmap(decodedByte);
                    }catch (Exception e){}

                    Log.v("xxxxxxxxxxx",model.body);
                }
//                    viewHolder.iv_image.setText(model.time);
//                Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);

//                adapter.notifyItemInserted(adapter.getItemCount()-1);
//                rvMessage.scrollToPosition(adapter.getItemCount()-1);
//                if (f == 0) {
//                    rvMessage.scrollToPosition(adapter.getItemCount() - 1);
//                }
//
//                if (position == adapter.getItemCount()-1)
//                    f=1;

            }

//            @Override
//            public int getItemCount() {
//                return adapter.getItemCount()- 1;
//            }

        };
        rvMessage.setAdapter(adapter);
        rvMessage.scrollToPosition(adapter.getItemCount()-1);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    //Update the value background thread to UI thread
//                    rvMessage.scrollToPosition(adapter.getItemCount() - 1);
//                }
//
//            }).start();
//
//        }catch (Exception e){}
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send){
            String message = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(message)){
                Map<String, Object> param = new HashMap<>();
                param.put("sender", name);
                param.put("body", message);
                param.put("messageType", "text");
                param.put("owner", "driver");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss a");
                String currentDateandTime = sdf.format(new Date());
                param.put("time", currentDateandTime);


                mDatabaseReference.child("Chats").child(userID+"").child("lastMessage").setValue(message);  // <= Change to ++count
                mDatabaseReference.child("Chats").child(userID+"").child("lastMessageDate").setValue(System.currentTimeMillis());  // <= Change to ++count


                mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Long value = dataSnapshot.getValue(Long.class);
                            mDatabaseReference.child("Chats").child(userID+"").child("adminUnreadMessages").setValue(++value);  // <= Change to ++count
                        }catch (Exception e){
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                mDatabaseReference.child("messages").child(userID+"")
                        .push()
                        .setValue(param)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                edtMessage.setText("");
                                if(task.isSuccessful()){
                                    Log.d("SendMessage", "Sukses");
                                }else{
                                    Log.d("SendMessage", "failed ");
                                }
                                rvMessage.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        });
            }


        }
    }

    public void sendPhoto(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_FROM_GALLARY);

    }

    public void backApp(View view) {
        finish();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView  tvMessage,tv_time;
        ImageView iv_image;
        LinearLayout chat_parent;

        public ChatViewHolder(View itemView) {
            super(itemView);

//            tvEmail = (TextView) itemView.findViewById(R.id.tv_sender);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            chat_parent = (LinearLayout) itemView.findViewById(R.id.chat_parent);
        }
    }

    String encodedImage;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_FROM_GALLARY:

                if (resultCode == Activity.RESULT_OK) {
                    //pick image from gallery
//                    Uri selectedImage = data.getData();


                    try {
                        final Uri imageUri = data.getData();
                        String imagepath = getPath(ChatActivity.this,imageUri).toString();


                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imagepath = cursor.getString(columnIndex);
                        cursor.close();
                        //Toast.makeText(PersonalDetails.this,imagepath, Toast.LENGTH_SHORT).show();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        encodedImage = BitMapToString(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

//                    String result = "";
//                    String[] projection = {MediaStore.Images.Media.DATA};
//
//                    try {
//                        Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
//                        cursor.moveToFirst();
//
//                        int columnIndex = cursor.getColumnIndex(projection[0]);
//                        result = cursor.getString(columnIndex);
//                        cursor.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
////                    Bitmap bm = BitmapFactory.decodeFile(selectedImage.getPath());
////                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
////                    byte[] b = baos.toByteArray();
//
//                    String encodedImage = Base64.encodeToString(result, Base64.DEFAULT);


                    Map<String, Object> param = new HashMap<>();
                    param.put("sender", name);
                    param.put("body", encodedImage);
                    param.put("messageType", "image");
                    param.put("owner", "driver");

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss a");
                    String currentDateandTime = sdf.format(new Date());
                    param.put("time", currentDateandTime);

                    mDatabaseReference.child("Chats").child(userID+"").child("lastMessage").setValue("image");  // <= Change to ++count
                    mDatabaseReference.child("Chats").child(userID+"").child("lastMessageDate").setValue(System.currentTimeMillis());  // <= Change to ++count

                    mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Long value = dataSnapshot.getValue(Long.class);
                                mDatabaseReference.child("Chats").child(userID+"").child("adminUnreadMessages").setValue(++value);  // <= Change to ++count
                            }catch (Exception e){
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    mDatabaseReference.child("messages").child(userID+"")
                            .push()
                            .setValue(param)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    edtMessage.setText("");
                                    if(task.isSuccessful()){
                                        Log.d("SendMessage", "Sukses");
                                    }else{
                                        Log.d("SendMessage", "failed ");
                                    }
                                }
                            });
                }
                break;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public String getPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
