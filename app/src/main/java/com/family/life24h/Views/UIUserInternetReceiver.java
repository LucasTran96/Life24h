package com.family.life24h.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.timeUtils;

import static com.family.life24h.Models.objApplication.objAccount.getCurrentUser;
import static com.family.life24h.Utils.keyUtils.STATUS;

/**
 * Author: Lucaswalker@jexpa.com
 * Class: Distributing
 * History: 1/9/2020
 * Project: life24h
 */


public class UIUserInternetReceiver extends BroadcastReceiver {

    //    public  static  boolean  checkConnect = true;
    //    public  static boolean checkOffline = false;
    private static final String TAG = "UIUserInternetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // create Log4j

        if(objAccount.getCurrentUser() != null)
        {
            if(objAccount.getAccountFromSQLite(context, objAccount.getCurrentUser().getUid()) != null){
                Log.d(TAG, "ACTION_UNLOCK_Internet");

                try {
                    if(timeUtils.isNetworkAvailable(context))
                    {
                        //when there is a new event and the wifi is off, save the value = true
                        // to wait when the user turns off the screen,
                        // turn on the wifi and send it in 30 seconds and turn off the wifi as before.
                        // when wifi turn on, turn the event to false
                        //newEvent_wifi_is_Off = false;
                        Log.d("ckinetrnet","online Internet");
                        final DatabaseReference mRefAccount = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child(keyUtils.accountList)
                                .child(getCurrentUser().getUid())
                                .child(STATUS);
                        mRefAccount.setValue("Online");

                        //checkConnect = true;


                    }else {
                        //checkConnect = false;
                        // checkOffline = true;
                        final DatabaseReference mRefAccount = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child(keyUtils.accountList)
                                .child(getCurrentUser().getUid())
                                .child(STATUS);
                        mRefAccount.setValue("Offline");
                        Log.d("ckinetrnet","Offline Internet");


                    }

                }catch (Exception e)
                {
                    Log.d("error", e.getMessage());
                }

            }
        }


    }

}
