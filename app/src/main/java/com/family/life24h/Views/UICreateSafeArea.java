package com.family.life24h.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.family.life24h.R;
import com.family.life24h.Utils.viewUtils;

/*
 *  Date created: 12/18/2019
 *  Last updated: 12/18/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UICreateSafeArea extends AppCompatActivity implements OnMapReadyCallback {
    private final Context context = this;

    private TextInputLayout tilCommonName;
    private LoaderTextView ltvInviteCode;



    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_safe_area);

        initView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        setActionToView();
    }

    private void setActionToView() {
        ltvInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUtils.copiedToClipboard(context,ltvInviteCode.getText().toString());
            }
        });
    }

    private void initView() {
        tilCommonName = findViewById(R.id.tilCommonName);
        ltvInviteCode = findViewById(R.id.ltvInviteCode);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
