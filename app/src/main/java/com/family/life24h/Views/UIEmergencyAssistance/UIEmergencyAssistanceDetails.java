package com.family.life24h.Views.UIEmergencyAssistance;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Presenters.Safety.pre_Safety;
import com.family.life24h.Presenters.Safety.view_Safety;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.directionalController;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.timeUtils;
import com.family.life24h.Utils.viewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*
 *  Date created: 02/06/2020
 *  Last updated: 02/06/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIEmergencyAssistanceDetails extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, view_Safety {
    public static final String TAG = UIEmergencyAssistanceDetails.class.getSimpleName();
    private final Context context = this;

    private GoogleMap mMap;

    private objEmergencyAssistance mEmergencyAssistance;
    private objAccount mAccountNeedHelp;
    private ImageView imvSelectMap;

    private LinearLayout lnlMapTypeSelect;
    private ImageView imvMapTerrain;
    private ImageView imvMapStreetMap;
    private ImageView imvMapSatellite;
    private TextView tvCloseSelectMap;
    private TextView tvNormalMap;
    private TextView tvTerrainMap;
    private TextView tvSatelliteMap;
    private TextView tvNeedsHelp;
    private TextView tvTime;
    private TextView tvAddress;
    private ImageView imvAvatar;

    private TextView tvDetailsOf;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LatLng locationNeedHelp = new LatLng(0.0,0.0);

    private MarkerOptions marker;

    private @ColorInt
    int mPulseEffectColor;
    private int[] mPulseEffectColorElements;
    private ValueAnimator mPulseEffectAnimator;
    private Circle mPulseCircle;

    private pre_Safety preSafety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_assistance_details);

        getDataBundle();

        initView();

        setLayoutTop();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        setDataToView();

        setActionToView();


    }

    private void setActionToView() {
        imvSelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlMapTypeSelect.setVisibility(View.VISIBLE);
                imvSelectMap.setVisibility(View.GONE);
                if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 1)
                {
                    tvNormalMap.setTextColor(getResources().getColor(R.color.borderMap));
                    imvMapStreetMap.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    tvSatelliteMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapSatellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    tvTerrainMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapTerrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
                }
                else if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 2)
                {
                    tvNormalMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapStreetMap.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    tvSatelliteMap.setTextColor(getResources().getColor(R.color.borderMap));
                    imvMapSatellite.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                    tvTerrainMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapTerrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
                }
                else
                {
                    tvNormalMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapStreetMap.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    tvSatelliteMap.setTextColor(getResources().getColor(R.color.black));
                    imvMapSatellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
                    tvTerrainMap.setTextColor(getResources().getColor(R.color.borderMap));
                    imvMapTerrain.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
                }
            }
        });

        imvMapTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    editor.putInt(keyUtils.MAPTYPE,3);
                    editor.apply();
                    lnlMapTypeSelect.setVisibility(View.GONE);
                    imvSelectMap.setVisibility(View.VISIBLE);
                }
            }
        });
        imvMapStreetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    editor.putInt(keyUtils.MAPTYPE,1);
                    editor.apply();
                    lnlMapTypeSelect.setVisibility(View.GONE);
                    imvSelectMap.setVisibility(View.VISIBLE);
                }
            }
        });
        imvMapSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    lnlMapTypeSelect.setVisibility(View.GONE);
                    imvSelectMap.setVisibility(View.VISIBLE);
                    editor.putInt(keyUtils.MAPTYPE,2);
                    editor.apply();
                }
            }
        });

        tvCloseSelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlMapTypeSelect.setVisibility(View.GONE);
                imvSelectMap.setVisibility(View.VISIBLE);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToGoogleMap(context,mEmergencyAssistance.getLongitude(),mEmergencyAssistance.getLatitude());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView() {
        Glide.with(Objects.requireNonNull(context))
                .load(mAccountNeedHelp.getLocalAvatar().matches("") ? mAccountNeedHelp.getAvatar() : mAccountNeedHelp.getLocalAvatar())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_avatar)
                .dontAnimate().apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(imvAvatar);

        tvNeedsHelp.setText(mAccountNeedHelp.getName() + " " + getResources().getString(R.string.needs_help));

        tvTime.setText(timeUtils.getTimeAgo(mEmergencyAssistance.getTimeCreate()));

        tvDetailsOf.setText(getResources().getString(R.string.Details_of) + " " + mAccountNeedHelp.getName() );

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mEmergencyAssistance.getLatitude(), mEmergencyAssistance.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            tvAddress.setText(Html.fromHtml("<u>" + address + "</u>"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle!=null){

            mEmergencyAssistance = (objEmergencyAssistance) bundle.getSerializable(keyUtils.dataEmergencyAssistance);
            mAccountNeedHelp = objAccount.getAccountFromSQLite(context, mEmergencyAssistance.getAuth());

            if(mEmergencyAssistance==null)
                finish();

        }else
            finish();
    }

    private void initView() {
        preSafety = new pre_Safety(context,this);

        ArrayList<String> listSeen = (ArrayList<String>) mEmergencyAssistance.getListSeen();
        listSeen.add(objAccount.getCurrentUser().getUid());
        preSafety.setSeenEmergencyAssistance(mEmergencyAssistance.getId(),listSeen);


        imvSelectMap = findViewById(R.id.imvSelectMap);
        lnlMapTypeSelect = findViewById(R.id.lnlMapTypeSelect);
        imvMapTerrain = findViewById(R.id.imvMapTerrain);
        imvMapStreetMap = findViewById(R.id.imvMapStreetMap);
        imvMapSatellite = findViewById(R.id.imvMapSatellite);
        tvCloseSelectMap = findViewById(R.id.tvCloseSelectMap);
        tvNormalMap = findViewById(R.id.tvNormalMap);
        tvTerrainMap = findViewById(R.id.tvTerrainMap);
        tvSatelliteMap = findViewById(R.id.tvSatelliteMap);
        tvNeedsHelp = findViewById(R.id.tvNeedsHelp);
        tvTime = findViewById(R.id.tvTime);
        tvAddress = findViewById(R.id.tvAddress);
        imvAvatar = findViewById(R.id.imvAvatar);

        tvDetailsOf = findViewById(R.id.tvDetailsOf);

        sharedPreferences = getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        adsUtils.setupAds(context);
    }

    private void setLayoutTop() {
        //Set transparent status bar and padding status bar
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        linearParams.setMargins(0, viewUtils.getStatusBarHeight((AppCompatActivity) context), 0, 0);
        findViewById(R.id.lnlTop).setPadding(0, viewUtils.getStatusBarHeight((AppCompatActivity) context), 0, 0);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        findViewById(R.id.imvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initPulseEffect() {
        mPulseEffectColor = getResources().getColor(R.color.orange);
        mPulseEffectColorElements = new int[] {
                Color.red(mPulseEffectColor),
                Color.green(mPulseEffectColor),
                Color.blue(mPulseEffectColor)
        };

        mPulseEffectAnimator = ValueAnimator.ofFloat(0, calculatePulseRadius(25));
        mPulseEffectAnimator.setStartDelay(1000);
        mPulseEffectAnimator.setDuration(400);
        mPulseEffectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private static float calculatePulseRadius(float zoomLevel) {
        return (float) Math.pow(2, 16 - zoomLevel) * 160 * 15;
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        if (mPulseEffectAnimator != null)
            mPulseEffectAnimator.setFloatValues(0, calculatePulseRadius(cameraPosition.zoom));
    }

    private void startPulseAnimation() {
        if (mPulseCircle != null)
            mPulseCircle.remove();

        if (mPulseEffectAnimator != null) {
            mPulseEffectAnimator.removeAllUpdateListeners();
            mPulseEffectAnimator.removeAllListeners();
            mPulseEffectAnimator.end();
        }

        if (locationNeedHelp != null) {
            mPulseCircle = mMap.addCircle(new CircleOptions()
                    .center(locationNeedHelp)
                    .radius(0).strokeWidth(0)
                    .fillColor(mPulseEffectColor));
            mPulseEffectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (mPulseCircle == null)
                        return;

                    int alpha = (int) ((1 - valueAnimator.getAnimatedFraction()) * 128);
                    mPulseCircle.setFillColor(Color.argb(alpha,
                            mPulseEffectColorElements[0], mPulseEffectColorElements[1], mPulseEffectColorElements[2]));
                    mPulseCircle.setRadius((float) valueAnimator.getAnimatedValue());
                }
            });

            mPulseEffectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mPulseEffectAnimator.setStartDelay(500);
                    mPulseEffectAnimator.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mPulseEffectAnimator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mPulseEffectAnimator.start();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        String title;
        if(mAccountNeedHelp != null){
            title = mAccountNeedHelp.getName() + getResources().getString(R.string.needs_help);
        }else{
            title = getResources().getString(R.string.needs_help);
        }
        locationNeedHelp = new LatLng(mEmergencyAssistance.getLatitude(), mEmergencyAssistance.getLongitude());
        marker = new MarkerOptions()
                .position(locationNeedHelp)
                .title(title);
        mMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationNeedHelp)      // Sets the center of the map to Mountain View
                .zoom(25)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);


        //Set default map type
        if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 1)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 2)
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        initPulseEffect();
        startPulseAnimation();
    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {

    }

    @Override
    public void emergencyContacts(ArrayList<objAccount> accounts) {

    }

    @Override
    public void drivingDetails(objDrivingDetail drivingDetail) {

    }

    @Override
    public void allDrivingDetailOfUser(ArrayList<objDrivingDetail> drivingDetailList) {

    }

    @Override
    public void emergencyAssistance(objAccount account, objEmergencyAssistance EmergencyAssistance, boolean isNewEmergency) {

    }

    @Override
    public void allEmergencyAssistance(ArrayList<objEmergencyAssistance> newEmergencyAssistanceList, ArrayList<objEmergencyAssistance> oldEmergencyAssistanceList) {

    }

}
