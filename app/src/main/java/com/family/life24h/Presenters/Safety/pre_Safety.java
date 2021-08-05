package com.family.life24h.Presenters.Safety;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.family.life24h.Models.objApplication.objAccount;
import com.family.life24h.Models.objApplication.objDrivingDetail;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;
import com.family.life24h.Models.objApplication.objFamily;
import com.family.life24h.Models.objectFirebase.drivingDetail.fb_drivingDetail;
import com.family.life24h.Models.objectFirebase.fb_emergencyAssistance;
import com.family.life24h.R;
import com.family.life24h.SQLite.tb_Account;
import com.family.life24h.SQLite.tb_EmergencyAssistance;
import com.family.life24h.SQLite.tb_Family;
import com.family.life24h.Utils.keyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
 *  Date created: 01/09/2020
 *  Last updated: 01/08/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class pre_Safety {
    private static final String TAG = pre_Safety.class.getSimpleName();
    private final Context context;
    private view_Safety mView;
    private FirebaseDatabase mDatabase;

    private int count_emergency;

    public pre_Safety(Context context, view_Safety mView) {
        this.context = context;
        this.mView = mView;
        this.mDatabase = FirebaseDatabase.getInstance();
    }

    public void getEmergencyContactsByFamilyID() {

        objFamily family = objFamily.getMyFamily(context);
        ArrayList<objAccount> accounts = new ArrayList<>();

        for (String accountID : family.getMembersList()) {
            if (!accountID.matches(objAccount.getCurrentUser().getUid()))
                accounts.add(objAccount.getAccountFromSQLite(context, accountID));
        }

        mView.emergencyContacts(accounts);
    }

    public void getCurrentSpeedOfUser(String Uid) {
        String week = String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        if (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) < 10) {
            week = "0" + week;
        }
        String id = week + Calendar.getInstance().get(Calendar.YEAR);

        mDatabase.getReference().child(keyUtils.drivingDetailHistory)
                .child(Uid)
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.d("CheckApp", dataSnapshot.getRef().getPath().toString());
                        if (dataSnapshot.getValue() != null) {
                            int week = Integer.parseInt(id.substring(0, 2));
                            int year = Integer.parseInt(id.substring(2));
                            mView.drivingDetails(new objDrivingDetail(dataSnapshot.getRef().getPath().toString(), week, year, dataSnapshot.getValue(fb_drivingDetail.class)));
                        } else
                            mView.resultOfAction(false, "dataSnapshot null", keyUtils.getSpeedOfUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mView.resultOfAction(false, databaseError.getMessage(), keyUtils.getSpeedOfUser);
                    }
                });

    }

    public void getAllSpeedOfUser(String Uid) {

        mDatabase.getReference().child(keyUtils.drivingDetailHistory)
                .child(Uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<objDrivingDetail> drivingDetailList = new ArrayList<>();
                        // Log.d("driving", new Gson().toJson(dataSnapshot.getValue()));

                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                if (data.getValue() != null) {
                                    try{
                                        final String id = data.getKey();
                                        int week = Integer.parseInt(id.substring(0, 2));
                                        int year = Integer.parseInt(id.substring(2));
                                        objDrivingDetail drivingDetail = new objDrivingDetail(data.getRef().getPath().toString(),
                                                week,
                                                year,
                                                data.getValue(fb_drivingDetail.class));
                                        drivingDetailList.add(drivingDetail);
                                    }catch (Exception e){
                                        Log.e(TAG,e.getMessage());
                                    }
                                }
                            }
                        }

                        mView.allDrivingDetailOfUser(drivingDetailList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mView.resultOfAction(false, databaseError.getMessage(), keyUtils.getAllSpeedOfUser);
                    }
                });
    }

    public static void setDrivingDetails(double newTopSpeed, double newAvgSpeed, @NonNull onResultDrivingDetails result) {
        if(FirebaseAuth.getInstance().getCurrentUser() !=null && newAvgSpeed > 0 && newTopSpeed > 0){
            final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String week = String.valueOf(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
            final String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            if (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) < 10) {
                week = "0" + week;
            }

            final String id = week + year;
            DatabaseReference mRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(keyUtils.drivingDetailHistory)
                    .child(Uid)
                    .child(id);

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        fb_drivingDetail valueInsert = new fb_drivingDetail();

                        fb_drivingDetail oldData = dataSnapshot.getValue(fb_drivingDetail.class);

                        double oldTopSpeed = Double.parseDouble(oldData.getTopSpeed().replace(" km/hr", ""));

                        //set top speed
                        if (oldTopSpeed < newTopSpeed) {
                            valueInsert.setTopSpeed(newTopSpeed + " km/hr");
                            valueInsert.setTimeUpdateTopSpeed(Calendar.getInstance().getTimeInMillis());
                        } else {
                            valueInsert.setTopSpeed(oldData.getTopSpeed());
                            valueInsert.setTimeUpdateTopSpeed(oldData.getTimeUpdateTopSpeed());
                        }

                        valueInsert.setAverageSpeed(oldData.getAverageSpeed());
                        valueInsert.getAverageSpeed().getListAverageSpeed().add(newAvgSpeed);
                        valueInsert.getAverageSpeed().setTimeUpdateAverageSpeed(Calendar.getInstance().getTimeInMillis());

                        mRef.setValue(valueInsert)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @SuppressLint("RestrictedApi")
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        result.onResult(true, new objDrivingDetail(mRef.getPath().toString(),
                                                        Calendar.getInstance().get(Calendar.WEEK_OF_YEAR),
                                                        Calendar.getInstance().get(Calendar.YEAR),
                                                        valueInsert),
                                                "Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        result.onResult(false, null, e.getMessage());
                                    }
                                });
                    }
                    //No data
                    else {
                        List<Double> listAverageSpeed = new ArrayList<>();
                        listAverageSpeed.add(newAvgSpeed);
                        fb_drivingDetail drivingDetail = new fb_drivingDetail(listAverageSpeed, newTopSpeed + " km/hr");

                        mRef.setValue(drivingDetail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @SuppressLint("RestrictedApi")
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        result.onResult(true, new objDrivingDetail(mRef.getPath().toString(),
                                                        Calendar.getInstance().get(Calendar.WEEK_OF_YEAR),
                                                        Calendar.getInstance().get(Calendar.YEAR),
                                                        drivingDetail),
                                                "Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        result.onResult(false, null, e.getMessage());
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    result.onResult(false, null, databaseError.getMessage());
                }
            });
        }

    }

    public interface onResultDrivingDetails {
        void onResult(boolean isSuccess, objDrivingDetail drivingDetail, String message);
    }


    /**
     * Write data to database type help
     * @param context context
     * @param result interface result
     */
    public static void sendEmergencyAssistanceHelp(Context context, @NonNull iEmergencyAssistanceHelp result) {
        final String Uid = objAccount.getCurrentUser().getUid();
        final objAccount currentAccount = objAccount.getAccountFromSQLite(context,Uid);

        final DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.emergencyAssistanceList)
                .child(Uid)
                .push();

        //Get current location
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    ArrayList<String> listSeen = new ArrayList<>();
                    listSeen.add(objAccount.getCurrentUser().getUid());

                    ArrayList<String> listReceived = new ArrayList<>();
                    listReceived.add(objAccount.getCurrentUser().getUid());

                    String message = String.format(context.getResources().getString(R.string.announces_that_he_has_a_problem_to_help),
                            currentAccount.getName(),
                            currentAccount.getGender().toLowerCase().matches(keyUtils.MALE) ? context.getResources().getString(R.string.he) : context.getResources().getString(R.string.she),
                            currentAccount.getGender().toLowerCase().matches(keyUtils.MALE) ? context.getResources().getString(R.string.him) : context.getResources().getString(R.string.her));

                    fb_emergencyAssistance EmergencyAssistance = new fb_emergencyAssistance(location.getLatitude(),
                            Uid,
                            listReceived,
                            listSeen,
                            location.getLongitude(),
                            message,
                            keyUtils.TYPE_HELP,
                            Calendar.getInstance().getTimeInMillis());

                    mRef.setValue(EmergencyAssistance)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    result.onResult(true, "Success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    result.onResult(false, e.getMessage());
                                }
                            });
                }else
                    result.onResult(false,"Null location");
            }
        });
    }

    public interface iEmergencyAssistanceHelp {
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Write data to database type Abnormal Speed
     * @param context context
     * @param result interface result
     */
    public static void sendEmergencyAssistanceAbnormalSpeed(Context context, @NonNull iEmergencyAssistanceAbnormalSpeed result) {
        final String Uid = objAccount.getCurrentUser().getUid();
        final objAccount currentAccount = objAccount.getAccountFromSQLite(context,Uid);

        final DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.emergencyAssistanceList)
                .child(Uid)
                .push();

        //Get current location
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    ArrayList<String> listSeen = new ArrayList<>();
                    listSeen.add(objAccount.getCurrentUser().getUid());

                    ArrayList<String> listReceived = new ArrayList<>();
                    listReceived.add(objAccount.getCurrentUser().getUid());

                    String message = String.format(context.getResources().getString(R.string.We_find_driving_speed_unusually_low),
                            currentAccount.getName(),
                            currentAccount.getGender().toLowerCase().matches(keyUtils.MALE) ? context.getResources().getString(R.string.him) : context.getResources().getString(R.string.her));

                    fb_emergencyAssistance emergencyAssistance = new fb_emergencyAssistance(location.getLatitude(),
                            Uid,
                            listReceived,
                            listSeen,
                            location.getLongitude(),
                            message,
                            keyUtils.TYPE_ABNORMAL_SPEED,
                            Calendar.getInstance().getTimeInMillis());


                    mRef.setValue(emergencyAssistance)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    result.onResult(true, "Success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    result.onResult(false, e.getMessage());
                                }
                            });
                }else
                    result.onResult(false,"Null location");
            }
        });
    }

    public interface iEmergencyAssistanceAbnormalSpeed{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Get all the latest emergency notifications from family members
     */
    @SuppressLint("RestrictedApi")
    public void getEmergencyAssistance(){
        final String uID = objAccount.getCurrentUser().getUid();
        ArrayList<String> allMemberID = new ArrayList<>();

        ArrayList<objFamily> allFamily = tb_Family.getInstance(context).getAllFamilyByUid(uID);
        for(objFamily family : allFamily){
            for(String memberID : family.getMembersList()){
                if(!memberID.matches(uID)
                        && !allMemberID.contains(memberID))
                    allMemberID.add(memberID);
            }
        }

        //Add listening events for each member
        for(String id : allMemberID){
            Query mRef = mDatabase.getReference()
                    .child(keyUtils.emergencyAssistanceList)
                    .child(id)
                    .limitToLast(1);

            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getValue() != null){

                        boolean isNewEmergency = false;

                        fb_emergencyAssistance fireBase_EmergencyAssistance = dataSnapshot.getValue(fb_emergencyAssistance.class);

                        if(fireBase_EmergencyAssistance != null){

                            if(!fireBase_EmergencyAssistance.getListSeen().contains(uID))
                                isNewEmergency = true;

                            final String pathEmergency = dataSnapshot.getRef().getPath().toString();
                            objEmergencyAssistance emergencyAssistance = new objEmergencyAssistance(pathEmergency, fireBase_EmergencyAssistance);

                            boolean flag = false;
                            //If not received, notice and write in SQLite
                            if(!emergencyAssistance.getListReceived().contains(uID)){
                                //Set received
                                List<String> listReceived = emergencyAssistance.getListReceived();
                                listReceived.add(uID);
                                setReceivedEmergencyAssistance(pathEmergency,listReceived);
                                tb_EmergencyAssistance.getInstance(context).addEmergencyAssistance(emergencyAssistance);

                                flag = true;
                            }

                            //Notifications have news
                            if(flag)
                                mView.emergencyAssistance(tb_Account.getInstance(context).getAccountByID(id), emergencyAssistance, isNewEmergency);
                            else
                                mView.emergencyAssistance(null, null, isNewEmergency);

                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @SuppressLint("RestrictedApi")
    public void getAllEmergencyAssistance(){
        count_emergency = 0;

        ArrayList<objEmergencyAssistance> newEmergencyAssistanceList = new ArrayList<>();
        ArrayList<objEmergencyAssistance> oldEmergencyAssistanceList = new ArrayList<>();

        //Current user id
        final String uID = objAccount.getCurrentUser().getUid();

        ArrayList<String> allMemberID = new ArrayList<>();

        //Get all id of family members
        ArrayList<objFamily> allFamily = tb_Family.getInstance(context).getAllFamilyByUid(uID);
        for(objFamily family : allFamily){
            for(String memberID : family.getMembersList()){
                if(!memberID.matches(uID)
                        && !allMemberID.contains(memberID))
                    allMemberID.add(memberID);
            }
        }

        final int memberSize = allMemberID.size();

        //If there are no members then display no data
        if(memberSize == 0){
            mView.allEmergencyAssistance(newEmergencyAssistanceList, oldEmergencyAssistanceList);
        }

        for(int i = 0; i < memberSize; i++){
            final String id = allMemberID.get(i);

            Query mRef = mDatabase.getReference()
                    .child(keyUtils.emergencyAssistanceList)
                    .child(id)
                    .limitToLast(1);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null){

                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            fb_emergencyAssistance fireBase_EmergencyAssistance = data.getValue(fb_emergencyAssistance.class);

                            if(fireBase_EmergencyAssistance != null){
                                final String pathEmergency = data.getRef().getPath().toString();
                                objEmergencyAssistance EmergencyAssistance = new objEmergencyAssistance(pathEmergency, fireBase_EmergencyAssistance);

                                //If not seen, notice and record with SQLite
                                if(!fireBase_EmergencyAssistance.getListSeen().contains(uID)){
                                    tb_EmergencyAssistance.getInstance(context).addOrUpdateEmergency(EmergencyAssistance);
                                    newEmergencyAssistanceList.add(EmergencyAssistance);
                                }

                                else{
                                    tb_EmergencyAssistance.getInstance(context).addOrUpdateEmergency(EmergencyAssistance);
                                    oldEmergencyAssistanceList.add(EmergencyAssistance);
                                }
                            }
                        }
                    }

                    count_emergency++;

                    //Return to UI
                    if(count_emergency == memberSize){
                        mView.allEmergencyAssistance(newEmergencyAssistanceList, oldEmergencyAssistanceList);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,databaseError.getMessage());
                    mView.allEmergencyAssistance(new ArrayList<>(), new ArrayList<>());
                }
            });

        }
    }

    /**
     * Listen for all emergency announcements
     * @param context context
     * @param result interface result
     */
    @SuppressLint("RestrictedApi")
    public static void listenerEmergencyAssistance(Context context, @NonNull interListenerEmergency result){
        final String uID = objAccount.getCurrentUser().getUid();
        ArrayList<String> allMemberID = new ArrayList<>();

        ArrayList<objFamily> allFamily = tb_Family.getInstance(context).getAllFamilyByUid(uID);
        for(objFamily family : allFamily){
            for(String memberID : family.getMembersList()){
                if(!memberID.matches(uID)
                        && !allMemberID.contains(memberID))
                    allMemberID.add(memberID);
            }
        }

        //Add listening events for each member
        for(String id : allMemberID){

            Query mRef = FirebaseDatabase.getInstance().getReference()
                    .child(keyUtils.emergencyAssistanceList)
                    .child(id)
                    .limitToLast(1);

            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    fb_emergencyAssistance fireBase_EmergencyAssistance = dataSnapshot.getValue(fb_emergencyAssistance.class);

                    if(fireBase_EmergencyAssistance != null){

                        //If not received, notice and record in SQLite
                        if(!fireBase_EmergencyAssistance.getListReceived().contains(uID)){
                            final String pathEmergency = dataSnapshot.getRef().getPath().toString();

                            objEmergencyAssistance emergencyAssistance = new objEmergencyAssistance(pathEmergency, fireBase_EmergencyAssistance);

                            //Set received
                            List<String> listReceived = emergencyAssistance.getListReceived();

                            //Add Uid to the received list
                            if(!listReceived.contains(uID))
                                listReceived.add(uID);

                            //Update received list
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(pathEmergency)
                                    .child(keyUtils.listReceived)
                                    .setValue(listReceived);

                            //Add emergency assistance if exist
                            tb_EmergencyAssistance.getInstance(context).addEmergencyAssistance(emergencyAssistance);

                            //Update received list to SQLite
                            tb_EmergencyAssistance.getInstance(context).updateListReceived(pathEmergency, (ArrayList<String>) listReceived);

                            result.EmergencyAssistance(tb_Account.getInstance(context).getAccountByID(id), emergencyAssistance);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public interface interListenerEmergency{
        void EmergencyAssistance(objAccount account, objEmergencyAssistance EmergencyAssistance);
    }

    /**
     * Update received list
     * @param pathEmergency path emergency
     * @param listReceived list received
     */
    public void setReceivedEmergencyAssistance(String pathEmergency,List<String> listReceived){

        //Set value received to fireBase
        mDatabase.getReference()
                .child(pathEmergency)
                .child(keyUtils.listReceived)
                .setValue(listReceived);

        //Update received list to SQLite
        tb_EmergencyAssistance.getInstance(context).updateListReceived(pathEmergency, (ArrayList<String>) listReceived);

    }

    /**
     * Update seen list
     * @param pathEmergency path emergency
     * @param listSeen list seen
     */
    public void setSeenEmergencyAssistance(String pathEmergency, List<String>listSeen){

        //Set value seen to fireBase
        mDatabase.getReference()
                .child(pathEmergency)
                .child(keyUtils.listSeen)
                .setValue(listSeen);

        //Update seen list to SQLite
        tb_EmergencyAssistance.getInstance(context).updateListSeen(pathEmergency, (ArrayList<String>) listSeen);

    }
}
