/*
 *  Date created: 12/04/2019
 *  Last updated: 12/04/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

package com.family.life24h.Views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;


import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.Presenters.Family.pre_Family;
import com.family.life24h.R;
import com.family.life24h.Utils.directionalController;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.viewUtils;
import com.family.life24h.Views.UIWellcome.UITutorial;
import com.family.life24h.Views.UIWellcome.UIWelcome_first;

import java.util.List;
import java.util.Objects;

import static com.family.life24h.Utils.timeUtils.isNetworkAvailable;

public class UISplashScreen extends AppCompatActivity implements view_Account, PurchasesUpdatedListener {
    private final Context context = this;
    private BillingClient mBillingClient;
    private pre_Account mLogin;
    // check_BillingResponse is the value used to check if google play is available or defective.
    private boolean check_BillingResponse = false;
    // check_SUK_Register is the value used to check whether a user has a monthly billing subscription or not.
    private boolean check_SUK_Register = false;
    private boolean checkOpenDialogInternet = false;
    private Dialog dialogInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);

        mLogin = new pre_Account(context, this);
    }


    private void showDialogSettingInternet() {
        try {
            checkOpenDialogInternet = true;
            dialogInternet = new Dialog(UISplashScreen.this);
            dialogInternet.setContentView(R.layout.custom_notification_turn_on_internet);
            Objects.requireNonNull(dialogInternet.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogInternet.setCancelable(false);
            Objects.requireNonNull(dialogInternet.getWindow()).setLayout(getResources().getDisplayMetrics().widthPixels,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            ImageView btn_CloseSettings = dialogInternet.findViewById(R.id.img_Close_Dialog_Internet);

            btn_CloseSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogInternet.cancel();
                    if (isNetworkAvailable(context)) {
                        mLogin.autoSignIn();
                    }
                }
            });
            dialogInternet.show();
        } catch (Exception e) {
            Log.e("bug", e.getMessage() + "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkOpenDialog
        if (checkOpenDialogInternet) {
            if (isNetworkAvailable(context)) {
                dialogInternet.dismiss();
            }
        }

        if (isNetworkAvailable(context)) {
            SharedPreferences sharedPreferences = getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
            if (!(sharedPreferences.getBoolean(keyUtils.CHECK_WELCOME, false))) {
                startActivity(new Intent(UISplashScreen.this, UIWelcome_first.class));
                finish();
            }else
                mLogin.autoSignIn();

        } else {
            showDialogSettingInternet();
        }
    }

    @Override
    public void errorEmailOrPassword(String ErrorEmail, String ErrorPassword) {

    }

    @Override
    public void resultOfActionAccount(final boolean isSuccess, String message, String type) {

        if (type.matches(keyUtils.ResultAutoLogin)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isSuccess) {
                        pre_Family.checkFamilyID(new pre_Family.onResultCheckFamilyID() {
                            @Override
                            public void onResult(boolean isSuccess, long size, String message) {
                                if (isSuccess && size > 0) {
                                    if(!UITutorial.isFlagTutorial(context))
                                        startActivity(new Intent(context, UITutorial.class));
                                    else{
                                        directionalController.goToUILoadingData(context);
                                    }
                                    finish();
                                } else {
                                    directionalController.goToUISetupProfile(context);
                                    finish();
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(context, UILogin.class));
                        finish();
                    }
                }
            }, 200);
        }
    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }

    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }

    @Override
    public void resultCheckTimeTrial(boolean isExpired) {

    }

}
