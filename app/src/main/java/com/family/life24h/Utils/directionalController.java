package com.family.life24h.Utils;

/*
 *  Date created: 12/24/2019
 *  Last updated: 12/24/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Models.objectFirebase.chat.fb_Chat;
import com.family.life24h.Models.objectFirebase.chat.fb_Message;
import com.family.life24h.R;
import com.family.life24h.Views.PlaceAlertsDetail;
import com.family.life24h.Views.UIChat.UINewMessage;
import com.family.life24h.Views.UIEmergencyAssistance.UIEmergencyAssistance;
import com.family.life24h.Views.UIEmergencyAssistance.UIEmergencyAssistanceDetails;
import com.family.life24h.Views.UIEmergencyAssistance.UIListEmergencyAssistance;
import com.family.life24h.Views.UILoadingData;
import com.family.life24h.Views.UISafety.UIAllDrivingInsights;
import com.family.life24h.Views.UIChat.UIChat;
import com.family.life24h.Views.UIChat.UIViewImageMessage;
import com.family.life24h.Views.UISafety.UIDrivingDetailOfUser;
import com.family.life24h.Views.UISelectCountries;
import com.family.life24h.Views.UISettings.UIEditProfile;
import com.family.life24h.Views.UISettings.UIInviteFriends;
import com.family.life24h.Views.UISettings.UIJoinOtherFamilies;
import com.family.life24h.Views.UISetupProfile;

public class directionalController {

    /**
     *  @param context context
     * @param idChat id of chat
     * @param type
     * @param chatDetail chat detail
     */
    public static void goToUIChat(Context context, String idChat, String type, fb_Chat chatDetail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyUtils.dataChat, chatDetail);
        bundle.putString(keyUtils.dataIDChat, idChat);
        bundle.putString(keyUtils.dataType, type);

        Intent intent = new Intent(context, UIChat.class);

        intent.putExtra(keyUtils.data, bundle);

        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param idChat id of chat
     * @param url url of image
     * @param message object message
     */
    public static void goToUIImageViewer(Context context, String idChat, String url, fb_Message message) {
        Bundle bundle = new Bundle();
        bundle.putString(keyUtils.dataIDChat, idChat);
        bundle.putSerializable(keyUtils.dataMessage, message);
        bundle.putString(keyUtils.dataURLImage, url);

        Intent intent = new Intent(context, UIViewImageMessage.class);
        intent.putExtra(keyUtils.data, bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param phoneNumber phone number
     */
    public static void callThePhoneNumber(Context context, String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("callThePhoneNumber", "Permission not granted");
                return;
            }
        }
        context.startActivity(intent);
    }


    /**
     *
     * @param context context
     * @param type type of action
     */
    public static void goToUINewMessage(Context context, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(keyUtils.dataType,type);
        Intent intent = new Intent(context, UINewMessage.class);
        intent.putExtra(keyUtils.data,bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param Uid user id
     */
    public static void goToUIDrivingDetailOfUser(Context context, String Uid) {
        Bundle bundle = new Bundle();
        bundle.putString(keyUtils.dataUid,Uid);
        Intent intent = new Intent(context, UIDrivingDetailOfUser.class);
        intent.putExtra(keyUtils.data,bundle);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIAllDrivingInsights(Context context) {
        Intent intent = new Intent(context, UIAllDrivingInsights.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIInviteFriends(Context context) {
        Intent intent = new Intent(context, UIInviteFriends.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIEditProfile(Context context) {
        Intent intent = new Intent(context, UIEditProfile.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIJoinOtherFamilies(Context context) {
        Intent intent = new Intent(context, UIJoinOtherFamilies.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUISetupProfile(Context context) {
        Intent intent = new Intent(context, UISetupProfile.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     */
    public static void goToUILoadingData(Context context) {
        Intent intent = new Intent(context, UILoadingData.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param TAG  TAG activity
     */
    public static void goToUISelectCountry(Context context, String TAG) {
        Bundle bundle = new Bundle();
        bundle.putString(keyUtils.dataType,TAG);
        Intent intent = new Intent(context, UISelectCountries.class);
        intent.putExtra(keyUtils.data,bundle);
        context.startActivity(intent);
    }


    /**
     *
     * @param context context
     */
    public static void goToUIPlaceAlertsDetailAdd(Context context) {
        Intent intentAlert = new Intent(context, PlaceAlertsDetail.class);
        intentAlert.putExtra(keyUtils.Place_Item_Add,true);
        context.startActivity(intentAlert);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIEmergencyAssistance(Context context) {
        Intent intentAlert = new Intent(context, UIEmergencyAssistance.class);
        context.startActivity(intentAlert);
    }

    /**
     *
     * @param context context
     */
    public static void goToUIListEmergencyAssistance(Context context) {
        Intent intent = new Intent(context, UIListEmergencyAssistance.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param context context
     * @param emergencyAssistance object emergency assistance
     */
    public static void goToUIEmergencyAssistanceDetails(Context context, objEmergencyAssistance emergencyAssistance) {
        Intent intent = new Intent(context, UIEmergencyAssistanceDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyUtils.dataEmergencyAssistance,emergencyAssistance);
        intent.putExtra(keyUtils.data,bundle);
        context.startActivity(intent);
    }

    public static void goToGoogleMap(Context context, double longitude, double latitude){
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr="+latitude +","+ longitude);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            context.startActivity(mapIntent);
        }else{
            Toast.makeText(context, R.string.Please_install_the_Google_Map_application, Toast.LENGTH_SHORT).show();
        }

    }
}
