package com.app.sealteamdelivery.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.sealteamdelivery.MainActivity;
import com.app.sealteamdelivery.NewOrder;
import com.app.sealteamdelivery.R;
import com.app.sealteamdelivery.model.DeviceTokenInformation;
import com.app.sealteamdelivery.model.result.Result;
import com.app.sealteamdelivery.model.user.Data;
import com.app.sealteamdelivery.model.user.User_;
import com.app.sealteamdelivery.network.APIClient;
import com.app.sealteamdelivery.network.InterfaceRetro;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    NotificationManager notificationManager;
    Notification myNotification;
    Data user_;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.v("kkkkkkkkkkkkkkkk","notification");
        Log.v("kkkkkkkkkkkkkkkk",remoteMessage.getMessageId());

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();
            try {
                Log.v("kkkkkkkkkkkkkkkk", "Data = " + data.get("Data"));
            }catch (Exception e){}

            if ( data.get("NotIficationType").equals("neworder")) {





////////////////////////////////////////////////////////////////////////////////









//                Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
//                myIntent.putExtra("data",data.get("Data"));
//
//                @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
//                    getApplicationContext(),
//                    0,
//                    myIntent,
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                myNotification = new NotificationCompat.Builder(getApplicationContext())
//                    .setContentTitle("Exercise of Notification!")
//                    .setContentText("Do Something...")
//                    .setTicker("Notification!")
//                    .setWhen(System.currentTimeMillis())
//                    .setContentIntent(pendingIntent)
//                    .setDefaults(Notification.DEFAULT_SOUND)
//                    .setAutoCancel(true)
//                    .setSmallIcon(R.drawable.ic_home_black_24dp)
//                    .build();
//
//                notificationManager =
//                    (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(new Random().nextInt(1000), myNotification);


//                NotificationCompat.Builder builder =
//                    new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.history)
//                        .setContentTitle("Delivary App")
//                        .setContentText("You get new order");
//                int NOTIFICATION_ID = 12345;

                Intent targetIntent = new Intent(this, NewOrder.class);
//                targetIntent.putExtra("data",data.get("Data"));
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                nManager.notify(NOTIFICATION_ID, builder.build());

//                NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.history)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!")
//                        .setContentIntent(contentIntent); //Required on Gingerbread and below


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("data",data.get("Data"));
                i.putExtra("newOrder",true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                sendMyNotification(getApplicationContext().getString(R.string.you_get_new_order));
///////////////////////////////////////////////////////

//                String s="sound"; // you can change it dynamically
//                int res_sound_id = getApplicationContext().getResources().getIdentifier(s, "raw", getApplicationContext().getPackageName());
//                Uri u= Uri.parse("android.resource://" + con.getPackageName() + "/" +res_sound_id );

//                Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sound);
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CH_ID")
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(getString(R.string.app_name))
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setSound(soundUri)
//                        .setContentIntent(pendingIntent);

//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                    if(soundUri != null){
//                        // Changing Default mode of notification
//                        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//                        // Creating an Audio Attribute
//                        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                                .setUsage(AudioAttributes.USAGE_ALARM)
//                                .build();
//
//                        // Creating Channel
//                        NotificationChannel notificationChannel = new NotificationChannel("CH_ID","Testing_Audio",NotificationManager.IMPORTANCE_HIGH);
//                        notificationChannel.setSound(soundUri,audioAttributes);
//                        mNotificationManager.createNotificationChannel(notificationChannel);
//                    }
//                }
//                mNotificationManager.notify(0, notificationBuilder.build());

///////////////////////////////////////////////////////

//                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//                int notificationId = 1;
//                String channelId = "channel-01";
//                String channelName = "Channel Name";
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//
//                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                            .setUsage(AudioAttributes.USAGE_ALARM)
//                            .build();
//
//                    NotificationChannel mChannel = new NotificationChannel(
//                        channelId, channelName, importance);
//                    mChannel.setLockscreenVisibility(NotificationCompat.PRIORITY_HIGH);
//                    mChannel.setVibrationPattern(new long[] {2000});
//                    mChannel.enableVibration(true);
//
//                    mChannel.setSound(soundUri, audioAttributes);
//
//
//                    notificationManager.createNotificationChannel(mChannel);
//                }
//
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(getApplicationContext().getString(R.string.app_name))
//                    .setContentText(getApplicationContext().getString(R.string.you_get_new_order));
//
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//                stackBuilder.addNextIntent(targetIntent);
//                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                    0,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                );
//                mBuilder.setContentIntent(resultPendingIntent);
//
//
//                mBuilder.setSound(soundUri);
//
//                try {
//                    mBuilder.setAutoCancel(true);
//                }catch (Exception e){}
//                try {
//                    mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//                }catch (Exception e){}
//
//                try {
//                    mBuilder.setLights(Color.RED, 3000, 3000);
//                }catch (Exception e){}
////                try {
////                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////                    mBuilder.setSound(alarmSound);
////
////                }catch (Exception e){}
//                mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
//
//                notificationManager.notify(notificationId, mBuilder.build());

//                Intent intent = new Intent(this, ChatActivity.class);
//                intent.putExtra("data",data.get("Data"));
//                PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//                NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this)
//                    .setContentTitle("Delivary App")
//                    .setSmallIcon(R.drawable.ic_home_black_24dp)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("You get new order"))
////                    .addAction(getNotificationIcon(), "Action Button", pIntent)
//                    .setContentIntent(pIntent)
//                    .setContentText("")
//                    .setOngoing(true);
//                mNotificationManager.notify(new Random().nextInt(), mBuilder.build());










////////////////////////////////////////////////////////////////////////////////

//                Log.v("kkkkkkkkkkkkkkkk", "open");

            }else{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("accept",true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            try {
//                Log.v("kkkkkkkkkkkkkkkk", "Data = " + data.get("Data"));
            }catch (Exception e){}

//            try {
//                Log.v("kkkkkkkkkkkkkkkk", "NotIficationType = " + data.get("NotIficationType"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk", "title = " + remoteMessage.getNotification().getTitle());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk", "NotIficationType = " + remoteMessage.getNotification().toString());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","body = "+remoteMessage.getNotification().getBody());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","message type = "+remoteMessage.getMessageType());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","remoteMessage = "+remoteMessage.getData().toString());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","size = "+remoteMessage.getData().size());
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","ResturantName = "+data.get("ResturantName"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","ResturantLocation = "+data.get("ResturantLocation"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","ResturantLat = "+data.get("ResturantLat"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","ResturantLng = "+data.get("Resturantlng"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","OrderId = "+data.get("OrderId"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","OrderLocation = "+data.get("OrderLocation"));
//            }catch (Exception e){}
//            try {
//                Log.v("kkkkkkkkkkkkkkkk","OrderCost = "+data.get("OrderCost"));
//            }catch (Exception e){}

//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//            Toast.makeText(this, "remoteMessage.getData().size() > 0", Toast.LENGTH_SHORT).show();

        }else {
//            Log.v("kkkkkkkkkkkkkkkk","remoteMessage.getData().size() = 0");
        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Toast.makeText(this, "Check if message contains a notification payload", Toast.LENGTH_SHORT).show();
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        try {

            SharedPreferences appSharedPrefs = (SharedPreferences) getSharedPreferences("pref", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("user", null);
            user_ = gson.fromJson(json, Data.class);

            addDeviceToken(user_ .getUser().getUserId(),token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    InterfaceRetro apiInterface;

    public void addDeviceToken(int driver_id,String deviceToken)throws Exception{

        apiInterface = APIClient.getClient().create(InterfaceRetro.class);

        DeviceTokenInformation deviceTokenInformation = new DeviceTokenInformation();
        deviceTokenInformation.setDriver_id(driver_id);
        deviceTokenInformation.setDeviceToken(deviceToken);

        Call<Result> call = apiInterface.addDeviceToken(deviceTokenInformation,user_.getAccessToken());
//        Log.v("aaaaaaaaaaaaaa","4");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

//                Log.v("aaaaaaaaaaaaaa","done");

                Result resource = response.body();

//                Log.v("aaaaaaaaaaaaaa",response.message());
//                Log.v("aaaaaaaaaaaaaa","FaildReason = "+resource.getFaildReason());
//                Log.v("aaaaaaaaaaaaaa",resource.getData()+"");
//                Log.v("aaaaaaaaaaaaaa",resource.getIsSuccess()+"");


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

//                Log.v("aaaaaaaaaaaaaa","error");
//                Log.v("aaaaaaaaaaaaaa",t.getMessage());

            }
        });
//        Log.v("aaaaaaaaaaaaaa","finish");

    }


    private void sendMyNotification(String message) {

        Intent intent = new Intent(this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.ring);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CH_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if(soundUri != null){
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel("CH_ID","Testing_Audio",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(soundUri,audioAttributes);

                notificationChannel.setLockscreenVisibility(NotificationCompat.PRIORITY_HIGH);
                notificationChannel.setVibrationPattern(new long[] {10000});
                notificationChannel.enableVibration(true);

                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationBuilder.setSmallIcon(R.drawable.logo);

        mNotificationManager.notify(0, notificationBuilder.build());

        try {
            Vibrator v = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(10000);

            long[] pattern = { 0, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500};
            v.vibrate(pattern , -1);

        }catch (Exception e){}
    }

}
