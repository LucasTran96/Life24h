package com.family.life24h.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Utils.keyUtils;

import static com.family.life24h.Models.objApplication.objAccount.getCurrentUser;
import static com.family.life24h.Utils.keyUtils.GPS;
import static com.family.life24h.Utils.keyUtils.STATUS;

/**
 * Author: Lucaswalker@jexpa.com
 * Class: Distributing
 * History: 1/9/2020
 * Project: SecondClone
 */

public class gpsSwitchStateReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if(objAccount.getCurrentUser() != null) {
            if (objAccount.getAccountFromSQLite(context, objAccount.getCurrentUser().getUid()) != null) {
                if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {

                    //Log.d("ZXZ"," GPS turned on");
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    try {
                        if (isGpsEnabled || isNetworkEnabled) {
                            // Handle Location turned ON
                            Log.d("ZXZ"," GPS turned on");
                            final DatabaseReference mRefAccount = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(keyUtils.accountList)
                                    .child(getCurrentUser().getUid())
                                    .child(GPS)
                                    .child(STATUS);
                            mRefAccount.setValue(true);
                        } else {
                            // Handle Location turned OFF
                            Log.d("ZXZ"," GPS turned off");
                            final DatabaseReference mRefAccount = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(keyUtils.accountList)
                                    .child(getCurrentUser().getUid())
                                    .child(GPS)
                                    .child(STATUS);
                            mRefAccount.setValue(false);
                        }

                    }
                    catch (Exception e)
                    {
                        Log.d("error",e.getMessage());
                    }
                }
            }
        }
    }


}



