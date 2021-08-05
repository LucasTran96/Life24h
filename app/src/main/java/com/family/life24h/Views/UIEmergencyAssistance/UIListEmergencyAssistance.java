package com.family.life24h.Views.UIEmergencyAssistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.family.life24h.Adapter.aRclvEmergency;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Presenters.Safety.pre_Safety;
import com.family.life24h.Presenters.Safety.view_Safety;
import com.family.life24h.R;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.directionalController;
import com.family.life24h.Utils.viewUtils;

import java.util.ArrayList;

/*
 *  Date created: 02/05/2020
 *  Last updated: 02/05/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIListEmergencyAssistance extends AppCompatActivity implements view_Safety {

    public static final String TAG = UIListEmergencyAssistance.class.getSimpleName();
    private final Context context = this;

    private SpinKitView SpinKitLoadingNew;
    private TextView tvNoDataToDisplayNew;
    private RecyclerView rclvListEmergencyNew;

    private SpinKitView SpinKitLoadingOld;
    private TextView tvNoDataToDisplayOld;
    private RecyclerView rclvListEmergencyOld;

    private aRclvEmergency adapterNew;
    private aRclvEmergency adapterOld;

    private pre_Safety preSafety;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_emergency_assistance);

        setLayoutTop();
        initView();
    }

    private void initView() {
        rclvListEmergencyNew = findViewById(R.id.rclvListEmergencyNew);
        SpinKitLoadingNew = findViewById(R.id.SpinKitLoadingNew);
        tvNoDataToDisplayNew = findViewById(R.id.tvNoDataToDisplayNew);

        rclvListEmergencyOld = findViewById(R.id.rclvListEmergencyOld);
        SpinKitLoadingOld = findViewById(R.id.SpinKitLoadingOld);
        tvNoDataToDisplayOld = findViewById(R.id.tvNoDataToDisplayOld);

        preSafety = new pre_Safety(context,this);
        preSafety.getAllEmergencyAssistance();

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

    private void setupRecyclerEmergencyNew(ArrayList<objEmergencyAssistance> emergencyAssistanceList){
        adapterNew = new aRclvEmergency(emergencyAssistanceList, context);
        rclvListEmergencyNew.setAdapter(adapterNew);
        rclvListEmergencyNew.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        adapterNew.setOnClickListener(new aRclvEmergency.actionClick() {
            @Override
            public void onClick(int position) {
                directionalController.goToUIEmergencyAssistanceDetails(context, adapterNew.getItemByPosition(position));
            }
        });
    }

    private void setupRecyclerEmergencyOld(ArrayList<objEmergencyAssistance> emergencyAssistanceList){
        adapterOld = new aRclvEmergency(emergencyAssistanceList, context);
        rclvListEmergencyOld.setAdapter(adapterOld);
        rclvListEmergencyOld.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        adapterOld.setOnClickListener(new aRclvEmergency.actionClick() {
            @Override
            public void onClick(int position) {
                directionalController.goToUIEmergencyAssistanceDetails(context, adapterOld.getItemByPosition(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpinKitLoadingNew.setVisibility(View.VISIBLE);
        SpinKitLoadingOld.setVisibility(View.VISIBLE);

        setupRecyclerEmergencyNew(new ArrayList<>());
        setupRecyclerEmergencyOld(new ArrayList<>());

        preSafety.getAllEmergencyAssistance();

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
        tvNoDataToDisplayNew.setVisibility(newEmergencyAssistanceList.size() == 0 ? View.VISIBLE : View.GONE);
        SpinKitLoadingNew.setVisibility(View.GONE);

        tvNoDataToDisplayOld.setVisibility(oldEmergencyAssistanceList.size() == 0 ? View.VISIBLE : View.GONE);
        SpinKitLoadingOld.setVisibility(View.GONE);

        setupRecyclerEmergencyNew(newEmergencyAssistanceList);
        setupRecyclerEmergencyOld(oldEmergencyAssistanceList);

    }
}
