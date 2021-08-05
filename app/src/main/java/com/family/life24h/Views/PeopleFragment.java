package com.family.life24h.Views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lbt.waitingdialog.WaitingDialog;
import com.family.life24h.Adapter.aRclvListMember;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objChat;
import com.family.life24h.Models.objApplication.objDetailImage;
import com.family.life24h.Models.objApplication.objMessage;
import com.family.life24h.Models.objectFirebase.account.fb_Location;
import com.family.life24h.Models.objectFirebase.chat.fb_Chat;
import com.family.life24h.Models.objectFirebase.family.fb_area;
import com.family.life24h.Presenters.Account.pre_Account;
import com.family.life24h.Presenters.Account.view_Account;
import com.family.life24h.Presenters.Chat.pre_Chat;
import com.family.life24h.Presenters.Chat.view_Chat;
import com.family.life24h.R;
import com.family.life24h.SQLite.tb_Account;
import com.family.life24h.SQLite.tb_Area;
import com.family.life24h.SQLite.tb_CurrentFamilyID;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.directionalController;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.timeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.family.life24h.Models.objApplication.objAccount.getCurrentUser;
import static com.family.life24h.Utils.keyUtils.accountList;
import static com.family.life24h.Utils.keyUtils.areaList;
import static com.family.life24h.Utils.keyUtils.avatar;
import static com.family.life24h.Utils.keyUtils.location;
import static com.family.life24h.Utils.keyUtils.membersList;
import static com.family.life24h.Utils.timeUtils.getTimeAgo;

/**
 *  Date created: 12/31/2019
 *  Last updated: 12/31/2019
 *  Name project: life24h
 *  Description: This is a processing class that displays the position of all team members on Google Map
 *  and display the places the user has made available to the map.
 *  Auth: Lucas Walker
 */

public class PeopleFragment extends Fragment implements view_Account, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final String TAG = PeopleFragment.class.getSimpleName();

    private View viewParent;
    private MapView mMapView;
    private Bitmap bitmap1;
    private objAccount mAccount;
    private boolean checkMap = false;
    private pre_Account preAccount;
    private MarkerOptions markerOptions;
    private static GoogleMap mMap;
    private Marker[] markerArray;
    private Circle[] circlesArray;
    private ImageView cimg_SelectMap;
    private ImageView cimg_myMapLocation;
    private LinearLayout ln_Map_Type_Select;
    private ImageView img_Map_Terrain,img_Map_Street_Map,img_Map_Satellite, cimg_list_member;
    private TextView txt_Close_SelectMap, txt_Normal_Map,txt_Terrain_Map,txt_Satellite_Map;
    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;
    private List<fb_area> areaListFB;
    private List<objAccount> objAccountListMember;
    private List<String> memberList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int mapLoadCount = 0;
    // sydneyMyLocation is the value to store the current location of this device
    // to help review the device's current location
    private LatLng sydneyMyLocation = new LatLng(0.0,0.0);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          viewParent = inflater.inflate(R.layout.fragment_people, container, false);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        sharedPreferences = getContext().getSharedPreferences(keyUtils.KEY_FAMILY_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        WaitingDialog.showDialog(getActivity(),getResources().getString(R.string.Loading));

        initView();
        setEvent();
        checkMapType();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapPeople);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        adsUtils.setupAds(getContext(),viewParent);

        return viewParent;
    }



    private void setEvent() {

        cimg_list_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialogMemberList();
            }
        });

        // cimg_myMapLocation button helps users to see their current location
        // or when they move the map away and want to return to their position on the map.
        cimg_myMapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap != null)
                {
                    // get my location
                    if(sydneyMyLocation.latitude != 0.0)
                    {
                        Log.d("Texamap", "sydneyMyLocation != 0");
                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(sydneyMyLocation)
                                .bearing(2.5f)
                                .tilt(45)
                                .zoom(19)
                                .build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        mMap.animateCamera(cameraUpdate, 900, null);
                    }
                }
            }
        });

        // cimg_SelectMap this is the button to help handle the selection of map types of google map.
        cimg_SelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_Map_Type_Select.setVisibility(View.VISIBLE);
                checkMapType();
            }
        });

        img_Map_Terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    editor.putInt(keyUtils.MAPTYPE,3);
                    editor.apply();
                    ln_Map_Type_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_Street_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    editor.putInt(keyUtils.MAPTYPE,1);
                    editor.apply();
                    ln_Map_Type_Select.setVisibility(View.GONE);
                }
            }
        });
        img_Map_Satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMap!= null)
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    ln_Map_Type_Select.setVisibility(View.GONE);
                    editor.putInt(keyUtils.MAPTYPE,2);
                    editor.apply();
                }
            }
        });

        txt_Close_SelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_Map_Type_Select.setVisibility(View.GONE);
            }
        });
    }

    /**
     * checkMapType () this is the method to handle what type of map the user has
     * and convert selected color to display.
     */
    private void checkMapType()
    {
        if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 1)
        {
            txt_Normal_Map.setTextColor(getResources().getColor(R.color.borderMap));
            img_Map_Street_Map.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
            txt_Satellite_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
            txt_Terrain_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
        }
        else if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 2)
        {
            txt_Normal_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
            txt_Satellite_Map.setTextColor(getResources().getColor(R.color.borderMap));
            img_Map_Satellite.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
            txt_Terrain_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Terrain.setBackgroundColor(getResources().getColor(R.color.mapselect));
        }
        else
        {
            txt_Normal_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Street_Map.setBackgroundColor(getResources().getColor(R.color.mapselect));
            txt_Satellite_Map.setTextColor(getResources().getColor(R.color.black));
            img_Map_Satellite.setBackgroundColor(getResources().getColor(R.color.mapselect));
            txt_Terrain_Map.setTextColor(getResources().getColor(R.color.borderMap));
            img_Map_Terrain.setBackground(getResources().getDrawable(R.drawable.custom_background_select_map));
        }

    }

    /**
     * showDialogMemberList() This is the method that helps display the list of all team members
     * so users can select a certain member to see location history or average speed running away during the week.
     */
    private void showDialogMemberList() {
        try {

            objAccountListMember = new ArrayList<>();
            if (memberList.size()>0)
            {

                for (int i = 0; i < memberList.size(); i++) {
                    objAccount account = tb_Account.getInstance(getContext()).getAccountByID(memberList.get(i));
                    objAccountListMember.add(account);
                }
                Dialog dialogMember = new Dialog(getContext());
                dialogMember.setContentView(R.layout.custom_dialog_list_member);
                dialogMember.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogMember.setCancelable(true);
                Objects.requireNonNull(dialogMember.getWindow()).setLayout(getResources().getDisplayMetrics().widthPixels,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                ImageView btn_CloseMarker = dialogMember.findViewById(R.id.btn_CloseListMember);
                aRclvListMember mAdapter;
                RecyclerView mRecyclerView = dialogMember.findViewById(R.id.rcl_ListMember);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new aRclvListMember((ArrayList<objAccount>)objAccountListMember,getActivity());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnClickListener(new aRclvListMember.actionClick() {
                    @Override
                    public void onClick(int position) {
                        dialogMember.dismiss();
                        showDialog(mAdapter.getAccount(position).getId(),getContext());
                    }
                });

                btn_CloseMarker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMember.cancel();
                    }
                });
                dialogMember.show();
            }
        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage());

        }
    }

    private void initView() {
        preAccount = new pre_Account(getContext(),this);
        mAccount = objAccount.getAccountFromSQLite(getContext(), FirebaseAuth.getInstance().getUid());

        // get reference to 'accountList' node
        mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(getCurrentUser().getUid());

        // set ID
        cimg_SelectMap = viewParent.findViewById(R.id.cimg_SelectMap);

        cimg_myMapLocation = viewParent.findViewById(R.id.cimg_myMapLocation);
        ln_Map_Type_Select = viewParent.findViewById(R.id.ln_Map_Type_Select);
        ln_Map_Type_Select.setVisibility(View.GONE);
        img_Map_Satellite = viewParent.findViewById(R.id.img_Map_Satellite);
        img_Map_Street_Map = viewParent.findViewById(R.id.img_Map_Street_Map);
        img_Map_Terrain = viewParent.findViewById(R.id.img_Map_Terrain);
        txt_Close_SelectMap = viewParent.findViewById(R.id.txt_Close_SelectMap);
        txt_Normal_Map = viewParent.findViewById(R.id.txt_Normal_Map);
        txt_Satellite_Map = viewParent.findViewById(R.id.txt_Satellite_Map);
        txt_Terrain_Map = viewParent.findViewById(R.id.txt_Terrain_Map);

        cimg_list_member = viewParent.findViewById(R.id.cimg_list_member);

        UIMain.setListener(new UIMain.onSelectedChangeFamily() {
            @Override
            public void onChange(String familyID) {
                mMap.clear();
                markerArray = null;
                addListAreaChangeListener();
                addListMemberListChangeListener();

            }
        });

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check which current users are looking to see which of the three current map types
        if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 1)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if(sharedPreferences.getInt(keyUtils.MAPTYPE,1) == 2)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        addListAreaChangeListener();
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {

        // when the user clicks on a marker, it will be processed
        // display Dialog details of a member marker.
        if(marker.getTitle().startsWith("place"))
        {
            marker.setTitle(marker.getTitle().replace("place ",""));
            marker.showInfoWindow();
            marker.setTitle("place "+marker.getTitle());
        }
        else {
            showDialog(marker.getTitle(),getContext());
        }
        return true;
    }
    @SuppressLint("SetTextI18n")
    /**
     * This is the method to display details of a selected member
     * to see for example GPS status, Internet, latest location update time ...
     */
    public static void showDialog(String Uid, Context context) {

        try {
            if (!Uid.equals("") && (!Uid.startsWith("place")))
            {
                objAccount account = tb_Account.getInstance(context).getAccountByID(Uid);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_alert_marker);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                Objects.requireNonNull(dialog.getWindow()).setLayout(context.getResources().getDisplayMetrics().widthPixels,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                Button btn_CloseMarker = dialog.findViewById(R.id.btn_CloseMarker);
                Button btn_GetGPSNow = dialog.findViewById(R.id.btn_GetGPSNow);
                Button btnChatNow = dialog.findViewById(R.id.btnChatNow);

                CircleImageView profile_image_marker = dialog.findViewById(R.id.profile_image_marker);
                LinearLayout lnl_Daily_History_Speed_Marker = dialog.findViewById(R.id.lnl_Daily_History_Speed_Marker);
                LinearLayout lnl_Daily_History_Location_Marker = dialog.findViewById(R.id.lnl_Daily_History_Location_Marker);
                TextView txt_NameMarker = dialog.findViewById(R.id.txt_NameMarker);
                TextView txt_GPSStatus = dialog.findViewById(R.id.txt_GPSStatus);
                TextView txt_WIFIStatus = dialog.findViewById(R.id.txt_WIFIStatus);
                TextView txt_Daily_Driver = dialog.findViewById(R.id.txt_Daily_Driver);
                TextView txt_Daily_Location = dialog.findViewById(R.id.txt_Daily_Location);
                TextView txt_LastSinceMarker = dialog.findViewById(R.id.txt_LastSinceMarker);
                TextView txt_locationMarkerDetail = dialog.findViewById(R.id.txt_locationMarkerDetail);
                TextView txt_BatteryMarker = dialog.findViewById(R.id.txt_BatteryMarker);

                txt_NameMarker.setText(account.getName());
                txt_Daily_Driver.setText(context.getResources().getString(R.string.view_driving_summary_id,account.getName()));
                txt_Daily_Location.setText(context.getResources().getString(R.string.see_where_today_id,account.getName()));
                txt_LastSinceMarker.setText(context.getResources().getString(R.string.last_since)+" " + (getTimeAgo(account.getLocation().getTimeUpdate())));
                if(account.getGps().getStatus())
                {
                    txt_GPSStatus.setText(context.getResources().getString(R.string.gps_turn_on));
                    txt_GPSStatus.setTextColor(context.getResources().getColor(R.color.black));
                }
                else {
                    txt_GPSStatus.setText(context.getResources().getString(R.string.gps_turn_off));
                    txt_GPSStatus.setTextColor(context.getResources().getColor(R.color.orange));
                }

                txt_BatteryMarker.setText(context.getResources().getString(R.string.battery) + " "+account.getBatteryPercent());
                txt_locationMarkerDetail.setText(context.getResources().getString(R.string.current_location) + " "+ timeUtils.getAddress(account.getLocation().getLatitude(),account.getLocation().getLongitude(),context));
                long timeStatus = Long.parseLong(account.getNetwork());
                Log.d("times",(System.currentTimeMillis() - timeStatus)+"");
                if((System.currentTimeMillis() - timeStatus) < 3*60000)
                {
                    txt_WIFIStatus.setText(context.getResources().getString(R.string.wifi_turn_on));
                }
                else if((System.currentTimeMillis() - timeStatus) > 3*60000 && (System.currentTimeMillis() - timeStatus) < 40*60000)
                {
                    txt_WIFIStatus.setText(context.getResources().getString(R.string.wifi_away));
                }
                else {
                    txt_WIFIStatus.setText(context.getResources().getString(R.string.wifi_turn_off));
                    txt_WIFIStatus.setTextColor(context.getResources().getColor(R.color.orange_red));

                }

                //If local Avatar does not exist then get the avatar URL
                Glide.with(context)
                        .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_avatar)
                        .dontAnimate().apply(RequestOptions.circleCropTransform())
                        .into(profile_image_marker);

                lnl_Daily_History_Location_Marker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(context,UIHistoryLocations.class);
                        intent.putExtra("locationUser", Uid);
                        context.startActivity(intent);
                    }
                });

                lnl_Daily_History_Speed_Marker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        directionalController.goToUIDrivingDetailOfUser(context,Uid);
                    }
                });
                btn_GetGPSNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // show dialog wait Location now
                        LatLng sydney = new LatLng(account.getLocation().getLatitude(), account.getLocation().getLongitude());
                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(sydney)
                                .bearing(2.5f)
                                .tilt(45)
                                .zoom(19)
                                .build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        mMap.animateCamera(cameraUpdate, 900, null);
                        dialog.dismiss();
                    }
                });


                btn_CloseMarker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                btnChatNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pre_Chat preChat = new pre_Chat(context, new view_Chat() {
                            @Override
                            public void resultListChat(ArrayList<objChat> chatList) {

                            }

                            @Override
                            public void resultOfAction(boolean isSuccess, String message, String type) {

                            }

                            @Override
                            public void resultChatDetail(fb_Chat chats) {

                            }

                            @Override
                            public void resultMessage(objMessage newMessage) {

                            }

                            @Override
                            public void resultAllImage(ArrayList<objDetailImage> detailImageList) {

                            }
                        });
                        preChat.findChatIDByUid(Uid, new pre_Chat.onResultFindChatIDByUid() {
                            @Override
                            public void onResult(String idChat, fb_Chat detailChat, String message) {
                                //Chat already exists
                                if(!idChat.matches("") && detailChat != null){
                                    directionalController.goToUIChat(context,idChat, TAG , detailChat);
                                    dialog.dismiss();
                                }
                                //Chat does not exist
                                else{
                                    //set member list
                                    ArrayList<String> memberList = new ArrayList<>();
                                    memberList.add(objAccount.getCurrentUser().getUid());
                                    memberList.add(Uid);
                                    //Set chat detail
                                    final fb_Chat newChatDetail = new fb_Chat(objAccount.getAccountFromSQLite(context,Uid).getName(),
                                            memberList,
                                            keyUtils.PRIVATE);

                                    //Create new message ID
                                    preChat.createNewMessageID(newChatDetail,new pre_Chat.onResultMessageID() {
                                        @Override
                                        public void onResult(String idChat, String message) {

                                            if(!idChat.matches("")){
                                                directionalController.goToUIChat(context,idChat, TAG , newChatDetail);
                                                dialog.dismiss();
                                            }else
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                dialog.show();
            }

        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage()+"");

        }
    }
    /**
     * getMarkerBitmapFromView this is method help you add image for icon marker of map
     * @return bitmap no_avatar
     */
    private Bitmap getMarkerBitmapFromView()
    {
        final Bitmap returnedBitmap;
        if(getContext() != null)
        {
            View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_custom_marker, null);
            CircleImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
            markerImageView.setImageResource(R.drawable.no_avatar);
            customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
            customMarkerView.buildDrawingCache();
            returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
            Drawable drawable = customMarkerView.getBackground();
            if (drawable != null)
                drawable.draw(canvas);
            customMarkerView.draw(canvas);

            return returnedBitmap;
        }
        return null;
    }

    // This is a method to download a list of all family members to display on google map.
    private void addListMemberListChangeListener() {
        // User data change listener
        mFirebaseDatabase = mFirebaseInstance.getReference().child(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID());
        mFirebaseDatabase.child(membersList).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                    memberList = dataSnapshot.getValue(t);
                    final int memberListSize = memberList.size();

                    // Check for null
                    if (memberListSize > 0) {
                        //mMap.clear();
                        Log.d("TAGZ", "size member: " + memberListSize);

                        if (markerArray != null) {
                            markerArray = null;
                        }

                        markerArray = new Marker[memberListSize];

                        for (int i = 0; i < memberListSize; i++) {
                            addLocationChangeListener(memberList.get(i), i);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }



    /**
     * this is the method listen event change location of member in family
     * and custom icon marker of location from image firebase
     * @param Uid Uid
     * @param i location of location in markerArray array
     */
    private void addLocationChangeListener(String Uid, final int i) {
        // User data change listener

        mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(Uid);
        mFirebaseDatabase.child(location).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fb_Location newLocation = dataSnapshot.getValue(fb_Location.class);
                //objAccount account1 = objAccount.getAccountFromSQLite(getContext(),Uid);

                mFirebaseDatabase = mFirebaseInstance.getReference(accountList).child(Uid);
                mFirebaseDatabase.child(avatar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String avatarGPS   = dataSnapshot.getValue(String.class);

                        // Check for null
                        if (newLocation == null) {
                            Log.e(TAG, "User data is null!");
                            return;
                        }
                        LatLng sydney = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

                        if(markerArray.length > i)
                        {
                            if(markerArray[i] != null)
                            {
                                //Toast.makeText(getContext(), "remove marker "+ i, Toast.LENGTH_SHORT).show();
                                markerArray[i].remove();
                            }
                        }

                        if(avatarGPS.equals(keyUtils.NULL))
                        {
                            if(markerArray.length > i)
                            {
                                if(markerArray[i] != null)
                                {
                                    // Toast.makeText(getContext(), "remove marker "+ i, Toast.LENGTH_SHORT).show();
                                    markerArray[i].remove();
                                }
                            }

                            markerOptions = new MarkerOptions();
                            markerOptions.draggable(true);
                            markerOptions.title(Uid);
                            markerOptions.position(sydney);

                            if(getMarkerBitmapFromView() != null)
                            {
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()));
                                markerArray[i] = mMap.addMarker(markerOptions);
                                //markerArray[i].showInfoWindow();
                            }
                        }else {

                            if (getActivity()!=null){
                                View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_custom_marker, null);
                                CircleImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
                                new GetImageFromURL(markerImageView,customMarkerView,newLocation, i, Uid ).execute(avatarGPS);
                            }
                        }

                        if(getCurrentUser() != null)
                        {
                            if(Uid.equals(getCurrentUser().getUid()))
                            {
                                // sydneyMyLocation use to save location current of device
                                sydneyMyLocation = sydney;
                                if(mapLoadCount < 4)
                                {
                                    CameraPosition cameraPosition = CameraPosition.builder()
                                            .target(sydney)
                                            .bearing(2.5f)
                                            .tilt(45)
                                            .zoom(17)
                                            .build();
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                    mMap.animateCamera(cameraUpdate, 900, null);
                                    // get my location
                                    // check map = true so the marker does not need to zoom every time the location of this device changes.
                                    mapLoadCount = mapLoadCount + 1;
                                }

                                Log.e("Texamap", "Location data is changed!" +newLocation.getLatitude() + ", " + newLocation.getLongitude());
                            }
                        }

                        WaitingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    /**
     * addListAreaChangeListener
     */
    private void addListAreaChangeListener() {
        // User data change listener

        Log.d("CurrentZ", "getCurrentFamilyID = "+tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID());

        mFirebaseDatabase = mFirebaseInstance.getReference().child(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID());
        mFirebaseDatabase.child(areaList).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaListFB = new ArrayList<>();

                for (DataSnapshot dataSnapshotArea : dataSnapshot.getChildren())
                {
                    // add in sqlite
                    fb_area fb_area = dataSnapshotArea.getValue(fb_area.class);
                    areaListFB.add(fb_area);
                    tb_Area.getInstance(getContext()).addArea(dataSnapshotArea.getKey(),tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID(),fb_area);
                }
                Log.d("CurrentZ", "areaListFB"+areaListFB.size());
                // Check for null
                if (areaListFB.size() <= 0) {
                    mMap.clear();
                    addListMemberListChangeListener();
                    Log.d("TAGZ", "User data is null!");
                    return;
                }
                else {
                    if(mMap!=null)
                        mMap.clear();
                    addListMemberListChangeListener();

                    for (int i = 0 ; i<areaListFB.size(); i++)
                    {
                        if(getActivity() != null)
                        {
                            int height = 70;
                            int width = 70;
                            BitmapDrawable bitmapdraw =(BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.place_marker1);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            LatLng sydney = new LatLng(areaListFB.get(i).getLatitude(), areaListFB.get(i).getLongitude());
                            Log.d("LatLng", areaListFB.get(i).getLatitude() + "==" + areaListFB.get(i).getLongitude());

                            int finalI = i;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //int numbermarker = i+1;
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title("place "+ areaListFB.get(finalI).getRegionName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                                    CircleOptions circleOptions = new CircleOptions()
                                            .center(sydney)   //set center
                                            .radius(areaListFB.get(finalI).getRadius())   //set radius in meters
                                            .fillColor(getResources().getColor(R.color.color_area))  //semi-transparent
                                            .strokeColor(Color.TRANSPARENT)
                                            .strokeWidth(1);
                                    mMap.addCircle(circleOptions);}
                                // your UI code here
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    //Class for download IMAGE
    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap> {
        CircleImageView imgV;
        View customMarkerView;
        Bitmap returnedBitmap;
        fb_Location newLocation;
        String Uid;
        int marker;

        public GetImageFromURL(CircleImageView imgV, View customMarkerView, fb_Location newLocation, int i, String Uid){
            this.imgV = imgV;
            this.customMarkerView = customMarkerView;
            //this.returnedBitmap = returnedBitmap;
            this.newLocation = newLocation;
            this.marker = i;
            this.Uid = Uid;
        }

        @Override
        protected Bitmap doInBackground(String... url)
        {
            //String urldisplay = url[0];

            InputStream srt = null;
            URL url1 = null;

            bitmap1 = null;
            try {

                url1 = new URL(url[0]);
                String avartar = url[0].replace("https://firebasestorage.googleapis.com/v0/b/life24h-79106.appspot.com/o/","");
                File file = new File(getContext().getFilesDir() +"/" + keyUtils.avatarLife24h + "/"+avartar+".jpg");
                if(file.exists()){
                    //Do something
                    Log.d("textFile","File exist = "+ file.getPath());
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                }
                //The file does not exist
                else
                {
                    Log.d("textFile","File not saved = "+ avartar);
                    try {

                        bitmap1 =   BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                        //Create Path to save Image
                        File path = new File(getContext().getFilesDir(),keyUtils.avatarLife24h);//Creates app specific folder

                        if(!path.exists()) {
                            if(path.mkdirs())
                                Log.i("textFile","Create mkdir success: " + path.getPath());
                            else
                                Log.i("textFile", "Create mkdir fail");
                        }
                        else {
                            Log.i("textFile","App dir already exists");
                        }

                        File imageFile = new File(path, avartar+".jpg");

                        FileOutputStream out = null;

                        try {
                            out = new FileOutputStream(imageFile);
                        } catch (FileNotFoundException e) {
                            Log.e("textFile",e.getMessage());
                        }

                        try{
                            bitmap1.compress(Bitmap.CompressFormat.JPEG, 30, out); // Compress Image
                            out.flush();
                            out.close();

                            //Save image path to SQLite
                            tb_Account.getInstance(getContext()).updateLocalAvatar(Uid,imageFile.getAbsolutePath());

                        } catch(Exception e) {
                            Log.e("textFile",e.getMessage());
                        }
                    } catch (IOException e) {
                        Log.e("textFile",e.getMessage());
                    }
                }

            }catch (Exception e)
            {
                Log.e("textFile",e.getMessage());
            }

            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                if(markerArray[marker] != null)
                {
                    markerArray[marker].remove();
                }
                super.onPostExecute(bitmap);
                imgV.setImageBitmap(bitmap);
                customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
                customMarkerView.buildDrawingCache();

                returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
                Drawable drawable = customMarkerView.getBackground();
                if (drawable != null)
                    drawable.draw(canvas);
                customMarkerView.draw(canvas);
                Log.d("AUDxxx", returnedBitmap.getByteCount()+" bit map Asyntask");

                LatLng sydney = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                Log.d("LatLng", newLocation.getLatitude() + "==" + newLocation.getLongitude());
                markerOptions = new MarkerOptions();
                markerOptions.draggable(true);
                markerOptions.title(Uid);
                markerOptions.snippet("snippet");
                markerOptions.position(sydney);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap));
                markerArray[marker] = mMap.addMarker(markerOptions);

            }catch (Exception e)
            {
                e.getMessage();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addListAreaChangeListener();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
