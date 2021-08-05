package com.family.life24h.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.family.life24h.Adapter.aRclvPlaceList;
import com.family.life24h.Models.objApplication.objArea;
import com.family.life24h.Models.objectFirebase.family.fb_area;
import com.family.life24h.R;
import com.family.life24h.SQLite.tb_Area;
import com.family.life24h.SQLite.tb_CurrentFamilyID;
import com.family.life24h.Utils.adsUtils;
import com.family.life24h.Utils.directionalController;
import java.util.ArrayList;

import static com.family.life24h.Utils.keyUtils.areaList;

/**
 *  Date created: 12/18/2019
 *  Last updated: 05/01/2020
 *  Name project: life24h
 *  Description: This is a class that displays a list of all the places the people in the group have created.
 *  Auth: Lucas Walker
 */

public class PlacesFragment extends Fragment
{

    private View viewParent;
    private TextView btn_AddPlaceAlert;
    private RecyclerView mRecyclerView;
    private aRclvPlaceList mAdapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.fragment_places, container, false);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        setID();

        setupRecyclerViewPlace();

        setEvent();

        getPlaceList();

        UIMain.setListener(new UIMain.onSelectedChangeFamily() {
            @Override
            public void onChange(String familyID) {
                Log.d("IDfami",familyID );
                getPlaceList();
            }
        });

        adsUtils.setupAds(getContext(),viewParent);

        return viewParent;

    }

    private void setupRecyclerViewPlace(){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new aRclvPlaceList(new ArrayList<>(),getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("RestrictedApi")
    private void getPlaceList() {
        Log.d("IDfami",tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID() );
        mFirebaseDatabase = mFirebaseInstance.getReference().child(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID()).child(areaList);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    ArrayList<objArea> oldAreaList = (ArrayList<objArea>) tb_Area.getInstance(getContext()).getAreaListByIDFamily(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID());
                    ArrayList<objArea> newAreaList = new ArrayList<>();

                    //Init data to newAreaList
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        fb_area area = ds.getValue(fb_area.class);
                        newAreaList.add(new objArea(ds.getKey(), area));
                        tb_Area.getInstance(getContext()).addOrUpdateArea(ds.getKey(), tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID(), area);
                    }

                    ArrayList<objArea> areaListRemove = new ArrayList<>();

                    //Init data to areaListRemove
                    for(objArea oldArea : oldAreaList){
                        boolean flag = true;
                        for(objArea newArea : newAreaList){
                            if(newArea.getId().matches(oldArea.getId())){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            areaListRemove.add(oldArea);
                        }
                    }

                    //Delete area
                    for(objArea area : areaListRemove){
                        tb_Area.getInstance(getContext()).deleteArea(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID(), area.getId());
                    }
                    mAdapter.setAreaList((ArrayList<objArea>) tb_Area.getInstance(getContext()).getAreaListByIDFamily(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID()));
                }
                catch (Exception e) {
                    e.getMessage();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setEvent() {
        btn_AddPlaceAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                directionalController.goToUIPlaceAlertsDetailAdd(getContext());
            }
        });
    }

    private void setID() {
        btn_AddPlaceAlert = viewParent.findViewById(R.id.btn_AddPlaceAlert);
        mRecyclerView = viewParent.findViewById(R.id.rcl_PlaceAlert);

    }

    @Override
    public void onResume() {

        mAdapter.setAreaList((ArrayList<objArea>) tb_Area.getInstance(getContext()).getAreaListByIDFamily(tb_CurrentFamilyID.getInstance(getContext()).getCurrentFamilyID()));

        super.onResume();
    }
}
