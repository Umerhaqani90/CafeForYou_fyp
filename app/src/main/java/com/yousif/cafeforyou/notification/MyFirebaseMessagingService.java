package com.yousif.cafeforyou.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yousif.cafeforyou.MainActivity;

import java.util.Random;

import yousif.cafeforyou.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FCMToken", "token "+ s);
    }
    private final String ADMIN_CHANNEL_ID ="notification_channel_Bet n Earn";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        final Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.appicon);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get(remoteMessage.getNotification()))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);
//                .setContent(contentView);

//        //Set notification color to match your app color template
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            notificationBuilder.setColor(getResources().getColor(R.color.white));
//        }
        notificationManager.notify(notificationID, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}



//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Log.i("NOTIFICATION" , "NOTIFICATION");
//
//        final Intent intent = new Intent(this, MainActivity.class);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        int notificationID = new Random().nextInt(3000);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setupChannels(notificationManager);
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_launcher_foreground);
//
//        @SuppressLint("RemoteViewLayout") RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_design);
////        contentView.setImageViewResource(R.id.iv_logo_notification, R.mipmap.ic_launcher);
////        contentView.setTextViewText(R.id.tv_notification_title, "Custom notification");
////        contentView.setTextViewText(R.id.tv_notification_desc, "This is a custom layout");
//
//        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                .setSmallIcon(R.drawable.applogo)
//                .setLargeIcon(largeIcon)
//                .setContentTitle(remoteMessage.getData().get(remoteMessage.getNotification()))
//                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
//                .setSound(notificationSoundUri)
//                .setContentIntent(pendingIntent)
//                .setContent(contentView);
//
//
////        //Set notification color to match your app color template
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
////            notificationBuilder.setColor(getResources().getColor(R.color.white));
////        }
//        notificationManager.notify(notificationID, notificationBuilder.build());
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(NotificationManager notificationManager){
//        CharSequence adminChannelName = "New notification";
//        String adminChannelDescription = "Device to devie notification";
//
//        NotificationChannel adminChannel;
//        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
//        adminChannel.setDescription(adminChannelDescription);
//        adminChannel.enableLights(true);
//        adminChannel.setLightColor(Color.RED);
//        adminChannel.enableVibration(true);
//        if (notificationManager != null) {
//            notificationManager.createNotificationChannel(adminChannel);
//        }
//    }
//}
