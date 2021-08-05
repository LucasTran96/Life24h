package com.family.life24h.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.lbt.waitingdialog.WaitingDialog;
import com.family.life24h.Models.objApplication.objFamily;
import com.family.life24h.Presenters.Family.pre_Family;
import com.family.life24h.Presenters.Family.view_Family;
import com.family.life24h.R;
import com.family.life24h.Utils.keyUtils;

import java.util.ArrayList;
import java.util.Objects;

/*
 *  Date created: 12/26/2019
 *  Last updated: 12/26/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UILoadingData extends AppCompatActivity implements view_Family {
    private final String TAG = "UILoadingData";
    private pre_Family preFamily;
    private final Context context = this;
    private Dialog dialogMember;
    private boolean checkOpenDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_data);
        WaitingDialog.showDialog(context);

        Log.d(TAG, "Loading data");

        preFamily = new pre_Family(context, this);
        preFamily.getAllIDFamilyByUserID();
    }

    @Override
    public void resultOfActionFamily(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.getAllIDFamilyByUserID)){
            Log.d(TAG, "getAllIDFamilyByUserID");
            preFamily.getAllFamilyByUid();
        }
    }

    @Override
    public void resultFamilyList(ArrayList<objFamily> families) {

        WaitingDialog.dismissDialog();
        if(checkGPSStatus())
        {
            Intent intent = new Intent(context, UIMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            finish();
        }
        else {

            showDialogSettingGPS();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void showDialogSettingGPS() {
        try {
                checkOpenDialog = true;
                dialogMember = new Dialog(UILoadingData.this);
                dialogMember.setContentView(R.layout.custom_notification_turn_on_gps);
                Objects.requireNonNull(dialogMember.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogMember.setCancelable(false);
                Objects.requireNonNull(dialogMember.getWindow()).setLayout(getResources().getDisplayMetrics().widthPixels,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                ImageView btn_CloseSettings = dialogMember.findViewById(R.id.img_Close_Dialog);
                Button btn_GoToSetting_GPS = dialogMember.findViewById(R.id.btn_GoToSetting_GPS);



            btn_GoToSetting_GPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            btn_CloseSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UIMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        finish();
                        dialogMember.cancel();
                    }
                });
                dialogMember.show();
        }catch (Exception e)
        {
            Log.e("bug",e.getMessage()+"");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkOpenDialog
        if(checkOpenDialog)
        {
            if(checkGPSStatus())
            {
                dialogMember.dismiss();
                Intent intent = new Intent(context, UIMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        }
    }

    /**
     * checkGPSStatus: is the way to check the status of the current GPS to ask the user to turn on the location.
     * @return GPS Status
     */
    private Boolean checkGPSStatus()
    {
        boolean gpsStatus = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try {
            if (isGpsEnabled || isNetworkEnabled) {
                // Handle Location turned ON
                Log.d("gpsStatus"," GPS turned on");
                gpsStatus = true;

            } else {
                // Handle Location turned OFF
                Log.d("gpsStatus"," GPS turned off");
                gpsStatus = false;

            }
        }
        catch (Exception e)
        {
            Log.d("error",e.getMessage());
        }
        return gpsStatus;
    }
}
