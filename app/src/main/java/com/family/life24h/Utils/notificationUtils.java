package com.family.life24h.Utils;

/*
 *  Date created: 02/05/2020
 *  Last updated: 02/05/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.family.life24h.Models.objectFirebase.chat.fb_Chat;
import com.family.life24h.R;
import com.family.life24h.Views.UIChat.UIChat;
import com.family.life24h.Views.UIChat.UIListChat;
import com.family.life24h.Views.UIEmergencyAssistance.UIListEmergencyAssistance;
import com.family.life24h.Views.UIMain;
import com.family.life24h.Views.notifications.directReplyReceiver;

import java.util.Objects;
import java.util.Random;

import static com.family.life24h.Utils.keyUtils.CHANNEL_ID;
import static com.family.life24h.Utils.keyUtils.KEY_TEXT_REPLY;
import static com.family.life24h.Utils.keyUtils.TAG_NOTIFICATION;

public class notificationUtils {


    // GPSNotification
    public static void buildNotificationGPS(Context context, int notificationID, String title, String message) {

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Create GPSNotification builder
        NotificationCompat.Builder mBuilder;

        //Initialise ContentIntent
        Intent ContentIntent = new Intent(context, UIMain.class);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(context,
                0,
                ContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(context.getResources().getColor(R.color.colorThemePurple))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.quite_impressed))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    "Notification GPS",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Notification of GPS");

            mBuilder.setChannelId(CHANNEL_ID);
            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }

        Objects.requireNonNull(mNotificationManager).notify(TAG_NOTIFICATION,notificationID, mBuilder.build());
    }

    //Emergency notification
    public static void buildNotificationEmergencyAssistance(Context context, String title, String message) {
        final int notificationID = Math.abs(new Random().nextInt());

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Create GPSNotification builder
        NotificationCompat.Builder mBuilder;

        //Initialise ContentIntent
        Intent ContentIntent = new Intent(context, UIListEmergencyAssistance.class);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(context,
                0,
                ContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_help))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setContentText(message)
                .setColor(context.getResources().getColor(R.color.colorThemePurple))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    "Notification Emergency",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Notification Emergency");

            mBuilder.setChannelId(CHANNEL_ID);
            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }


        Objects.requireNonNull(mNotificationManager).notify(TAG_NOTIFICATION,notificationID, mBuilder.build());
    }


    // messageNotification
    public static void buildNotificationMessage(Context context, int notificationID, String title, String message, String idChat, fb_Chat chatDetail) {

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Create messageNotification builder
        NotificationCompat.Builder mBuilder;


        //Initialise RemoteInput
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Reply").build();
        Bundle bundle = new Bundle();

        bundle.putString(keyUtils.dataIDChat,idChat);
        bundle.putSerializable(keyUtils.dataChat,chatDetail);
        bundle.putSerializable(keyUtils.dataType, UIListChat.TAG);

        Intent replyIntent = new Intent(context, directReplyReceiver.class);
        replyIntent.putExtra(keyUtils.data,bundle);
        replyIntent.putExtra(keyUtils.notificationId, notificationID);
        replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context,
                0,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.ic_chat_black_24dp, "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .setShowsUserInterface(true)
                .build();


        //Initialise ContentIntent
        Intent ContentIntent = new Intent(context, UIChat.class);
        ContentIntent.putExtra(keyUtils.data,bundle);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(context,
                0,
                ContentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(title)
                .setContentText(message)
                .addAction(replyAction)
                .setColor(context.getResources().getColor(R.color.colorThemePurple))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.quite_impressed))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    "Notification message",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Notification");

            mBuilder.setChannelId(CHANNEL_ID);
            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }

        Objects.requireNonNull(mNotificationManager).notify(TAG_NOTIFICATION,notificationID, mBuilder.build());
    }

    public static Notification buildNotificationService(Context context){
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "Notification_Life24h";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Notification_Life24h",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .build();
        }

        return notification;
    }
}
