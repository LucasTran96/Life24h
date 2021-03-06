package com.family.life24h.Views.UISafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.family.life24h.Adapter.aRclvDrivingDetail;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Presenters.Safety.pre_Safety;
import com.family.life24h.Presenters.Safety.view_Safety;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.keyUtils;
import com.family.life24h.Utils.viewUtils;

import java.util.ArrayList;
import java.util.Objects;

/*
 *  Date created: 01/14/2020
 *  Last updated: 01/14/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIDrivingDetailOfUser extends AppCompatActivity implements view_Safety {
    private final Context context = this;

    private String Uid;

    private SpinKitView SpinKitLoading;

    private TextView tvNoDataToDisplay;

    private RecyclerView rclvDrivingReport;

    private pre_Safety preSafety;

    private ImageView imvAvatar;
    private TextView tvUsername;

    private aRclvDrivingDetail adapterDrivingDetail;
    private objAccount mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_detail_of_user);

        getDataBundle();

        initView();

        setupRclvDrivingDetail();

        loading(true,0);

        setDataToView();

    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            Uid = bundle.getString(keyUtils.dataUid,keyUtils.NULL);
            if(Uid.matches(keyUtils.NULL))
                finish();
        }else
            finish();
    }

    private void setDataToView() {
        tvUsername.setText(mAccount.getName());

        Glide.with(Objects.requireNonNull(context))
                .load(mAccount.getLocalAvatar().matches("") ? mAccount.getAvatar() : mAccount.getLocalAvatar())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_avatar)
                .dontAnimate().apply(RequestOptions.circleCropTransform())
                .into(imvAvatar);


        preSafety.getAllSpeedOfUser(Uid);
    }

    private void initView() {
        SpinKitLoading = findViewById(R.id.SpinKitLoading);
        tvNoDataToDisplay = findViewById(R.id.tvNoDataToDisplay);
        rclvDrivingReport = findViewById(R.id.rclvDrivingReport);
        imvAvatar = findViewById(R.id.imvAvatar);
        tvUsername = findViewById(R.id.tvUsername);
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.white,"",R.color.colorStartGradientBlue);
        mAccount = objAccount.getAccountFromSQLite(context, Uid);
        preSafety = new pre_Safety(context,this);

        adsUtils.setupAds(context);

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
        adapterDrivingDetail.setDrivingDetailList(new ArrayList<>());
        for(objDrivingDetail drivingDetail : drivingDetailList){
            adapterDrivingDetail.addItem(drivingDetail);
        }

        loading(false,drivingDetailList.size());
    }

    @Override
    public void emergencyAssistance(objAccount account, objEmergencyAssistance EmergencyAssistance, boolean isNewEmergency) {

    }

    @Override
    public void allEmergencyAssistance(ArrayList<objEmergencyAssistance> newEmergencyAssistanceList, ArrayList<objEmergencyAssistance> oldEmergencyAssistanceList) {

    }

}
