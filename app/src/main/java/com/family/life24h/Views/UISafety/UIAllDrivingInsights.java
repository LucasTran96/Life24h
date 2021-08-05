package com.family.life24h.Views.UISafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.family.life24h.Adapter.aRclvAccountDetailDriving;
import com.family.life24h.Adapter.aRclvDrivingDetail;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Models.objApplication.objFamily;
import com.family.life24h.Presenters.Safety.pre_Safety;
import com.family.life24h.Presenters.Safety.view_Safety;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.viewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 *  Date created: 01/02/2020
 *  Last updated: 12/31/2019
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIAllDrivingInsights extends AppCompatActivity implements view_Safety {
    private final Context context = this;

    private SpinKitView SpinKitLoading;

    private TextView tvFamilyName;
    private TextView tvNoDataToDisplay;

    private RecyclerView rclvDrivingReport;
    private RecyclerViewPager rclvAllUser;
    private aRclvAccountDetailDriving adapterAccountDetailDriving;

    private pre_Safety preSafety;

    private ArrayList<objAccount> allAccounts;

    private aRclvDrivingDetail adapterDrivingDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_driving_insights);
        setLayoutTop();

        initView();

        loading(true,0);

        setDataToView();

        setupRclvAllUser();

        setupRclvDrivingDetail();
    }

    private void initView() {
        tvFamilyName = findViewById(R.id.tvFamilyName);
        tvNoDataToDisplay = findViewById(R.id.tvNoDataToDisplay);

        rclvDrivingReport = findViewById(R.id.rclvDrivingReport);
        rclvAllUser = findViewById(R.id.rclvAllUser);
        SpinKitLoading = findViewById(R.id.SpinKitLoading);

        preSafety = new pre_Safety(context,this);

        allAccounts = objFamily.getAllMemberInFamily(context);

        if(allAccounts.size() > 0)
            preSafety.getAllSpeedOfUser(allAccounts.get(0).getId());

        adsUtils.setupAds(context);
    }

    private void setDataToView() {
        tvFamilyName.setText(objFamily.getMyFamily(context).getCommonName());

    }

    private void setupRclvAllUser(){

        adapterAccountDetailDriving = new aRclvAccountDetailDriving(allAccounts,context);
        rclvAllUser.setAdapter(adapterAccountDetailDriving);
        rclvAllUser.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));

        rclvAllUser.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int i, int i1) {
                loading(true,0);
                preSafety.getAllSpeedOfUser(allAccounts.get(i1).getId());
            }
        });
    }

    /**
     * set up show loading and message, recycler view
     * @param isLoad status load
     * @param sizeDrivingDetail size driving detail list
     */
    private void loading(boolean isLoad, int sizeDrivingDetail){
        if(isLoad){
            SpinKitLoading.setVisibility(View.VISIBLE);
            rclvDrivingReport.setVisibility(View.GONE);
            tvNoDataToDisplay.setVisibility(View.GONE);
        }else{
            SpinKitLoading.setVisibility(View.GONE);
            if(sizeDrivingDetail == 0)
                tvNoDataToDisplay.setVisibility(View.VISIBLE);
            else
                rclvDrivingReport.setVisibility(View.VISIBLE);
        }

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

    private void setupRclvDrivingDetail(){
        adapterDrivingDetail = new aRclvDrivingDetail(new ArrayList<>(),context);
        rclvDrivingReport.setHasFixedSize(true);
        rclvDrivingReport.setAdapter(adapterDrivingDetail);
        rclvDrivingReport.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {
        Log.d("CheckApp", message);
        loading(false,0);
    }

    @Override
    public void emergencyContacts(ArrayList<objAccount> accounts) {

    }

    @Override
    public void drivingDetails(objDrivingDetail drivingDetail) {

    }

    @Override
    public void allDrivingDetailOfUser(ArrayList<objDrivingDetail> drivingDetailList) {
        Collections.sort(drivingDetailList, new Comparator<objDrivingDetail>() {
            @Override
            public int compare(objDrivingDetail o1, objDrivingDetail o2) {
                int id1 = Integer.parseInt(o1.getWeek() + "" + o1.getYear());
                int id2 = Integer.parseInt(o2.getWeek() + "" + o2.getYear());
                return id2 - id1;
            }
        });

        adapterDrivingDetail.setDrivingDetailList(drivingDetailList);


        loading(false,drivingDetailList.size());
    }

    @Override
    public void emergencyAssistance(objAccount account, objEmergencyAssistance EmergencyAssistance, boolean isNewEmergency) {

    }

    @Override
    public void allEmergencyAssistance(ArrayList<objEmergencyAssistance> newEmergencyAssistanceList, ArrayList<objEmergencyAssistance> oldEmergencyAssistanceList) {

    }


}
