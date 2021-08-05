package com.family.life24h.Views.UIWellcome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Views.UILogin;

import java.util.ArrayList;
import java.util.List;

/*
 *  Date created: 02/11/2020
 *  Last updated: 02/11/2020
 *  Name project: life24h
 *  Class name: UIUpgradePremium
 *  Description:
 *  Auth: Lucas Walker
 */

public class UIUpgradePremium extends AppCompatActivity implements view_Account, PurchasesUpdatedListener {

    private static final String TAG = "InAppBilling";
    // In-app products. Currently only selling "ad removal" preminium_6_month preminium_1_year
    public static final String ITEM_SKU_PREMINIUM_1_MONTH = "preminium_1_month";
    public static final String ITEM_SKU_PREMINIUM_6_MONTH = "preminium_6_month";
    public static final String ITEM_SKU_PREMINIUM_1_YEAR = "preminium_1_year";
    private BillingClient mBillingClient;
    private pre_Account mLogin;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrare_preminium);
        context = getApplicationContext();

        adsUtils.setupAds(this);


        Button btn_Subscribe_App = findViewById(R.id.btn_Subscribe_App);


        mBillingClient = BillingClient.newBuilder(UIUpgradePremium.this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //Toast.makeText(getApplicationContext(), "The BillingClient is ready.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "The BillingClient is ready.");
                    List skuList = new ArrayList<>();
                    skuList.add(ITEM_SKU_PREMINIUM_1_MONTH);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    mBillingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                    // Process the result.

                                    if (responseCode == BillingClient.BillingResponse.OK
                                            && skuDetailsList != null) {
                                        for (Object skuDetailsObject : skuDetailsList) {
                                            SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                            String sku = skuDetails.getSku();
                                            String price = skuDetails.getPrice();
                                            if (ITEM_SKU_PREMINIUM_1_MONTH.equals(sku)) {
                                                Log.d(TAG, "price and sku = " + sku + " , " + price);
                                                if (queryPurchases()) {
                                                    finish();
                                                }
                                            }
                                        }
                                    }

                                }
                            });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(getApplicationContext(), "Try to restart the connection on the next request to!", Toast.LENGTH_SHORT).show();

            }
        });
//        }


        btn_Subscribe_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user clicks the buy button, launch the billing flow for an ad removal  purchase
                // Response is handled using onPurchasesUpdated listener
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSku(ITEM_SKU_PREMINIUM_1_MONTH)
                        .setType(BillingClient.SkuType.SUBS)
                        .build();
                int responseCode = mBillingClient.launchBillingFlow(UIUpgradePremium.this, flowParams);
                if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                    Intent intent = new Intent(UIUpgradePremium.this, UILogin.class);
                    startActivity(intent);
                    finish();
                }
                Log.d(TAG, "responseCode = " + responseCode);
            }
        });


    }

    @Override
    protected void onResume() {
        try {
            Log.d(TAG, "responseCode = " + "in onResume");
            if (mBillingClient != null) {
                if (queryPurchases()) {
                    finish();
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        super.onResume();
    }

    private boolean queryPurchases() {

        try {
            Log.d(TAG, "jsonPurchases = " + "in queryPurchases");
            //Method not being used for now, but can be used if purchases ever need to be queried in the future
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
            try {
                Log.d(TAG, purchasesResult.getPurchasesList() + " = purchasesList");
            } catch (Exception e) {
                Log.d(TAG, "null" + " = purchasesList");
                e.getMessage();
            }
            if (purchasesResult != null) {
                List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                if (purchasesList == null) {
                    return false;
                }
                if (!purchasesList.isEmpty()) {
                    for (Purchase purchase : purchasesList) {
                        String jsonPurchases = purchase.getOriginalJson();
                        Log.d(TAG, "jsonPurchases = " + jsonPurchases);
                        if (purchase.getSku().equals(ITEM_SKU_PREMINIUM_1_MONTH)) {
                            Log.d(TAG, "đã đăng ký từ trước!");
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Log.d(TAG, "jsonPurchases = " + "Lỗi ở queryPurchases");
            e.getMessage();
            return false;
        }
    }

    private void handlePurchase(Purchase purchase) {
        Log.d(TAG, "purchase = OK = " + purchase);
        if (purchase.getSku().equals(ITEM_SKU_PREMINIUM_1_MONTH)) {
            mLogin = new pre_Account(context, this);
            mLogin.autoSignIn();
            //btn_price_app.setEnabled(false);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

        //Handle the responseCode for the purchase
        //If response code is OK,  handle the purchase
        //If user already owns the item, then indicate in the shared prefs that item is owned
        //If cancelled/other code, log the error

        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "User Canceled" + responseCode);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "User ALREADY OWNED = " + responseCode);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);

            if ((sharedPreferences.getBoolean(keyUtils.CHECK_WELCOME, false))) {
                try {
                    mLogin = new pre_Account(context, this);
                    mLogin.autoSignIn();
                } catch (Exception e) {
                    Intent intent = new Intent(UIUpgradePremium.this, UILogin.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Intent intent = new Intent(UIUpgradePremium.this, UILogin.class);
                startActivity(intent);
                finish();
            }

        } else {
            Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }
    }

    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

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
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }

    @Override
    public void resultCheckTimeTrial(boolean isExpired) {

    }
}
