package com.family.life24h.Utils;

/*
 *  Date created: 03/10/2020
 *  Last updated: 03/10/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.family.life24h.R;

import static com.family.life24h.Utils.keyUtils.isSee;
import static com.family.life24h.Utils.keyUtils.isShow;

public class adsUtils {

    public static void setupAds(Context context) {
        AdView mAdView = ((Activity)context).findViewById(R.id.adView);
        if(adsUtils.isSeeAd(context)){
            mAdView.setVisibility(View.VISIBLE);

            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }else
            mAdView.setVisibility(View.GONE);
    }

    public static void setupAds(Context context, View view) {
        AdView mAdView = view.findViewById(R.id.adView);
        if(adsUtils.isSeeAd(context)){
            mAdView.setVisibility(View.VISIBLE);

            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }else
            mAdView.setVisibility(View.GONE);
    }

    public static InterstitialAd setupInterstitialAd(Context context){
        InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.Key_Interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        return mInterstitialAd;
    }

    public static void showInterstitialAd(Context context, InterstitialAd mInterstitialAd){
        if (mInterstitialAd.isLoaded() && adsUtils.isSeeAd(context)) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public static void loadAd(Context context, AdView mAdView){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     *
     * @param context context
     * @param isSeeAd default see ad
     */
    public static void saveSeeAd(final Context context, boolean isSeeAd){
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyUtils.KEY_AD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(isSee,isSeeAd);
        editor.apply();
    }

    /**
     *
     * @param context context
     * @return default see ad (true)
     */
    public static boolean isSeeAd(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyUtils.KEY_AD, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(isSee,false);
    }

    /**
     *
     * @param context context
     * @param isSeeAd default see ad
     */
    public static void saveShowDialogUpgrade(final Context context, boolean isSeeAd){
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyUtils.KEY_SHOW_DIALOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(isShow,isSeeAd);
        editor.apply();
    }

    /**
     *
     * @param context context
     * @return default see ad (true)
     */
    public static boolean isShowDialogUpgrade(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyUtils.KEY_SHOW_DIALOG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(isShow,true);
    }


}
