package com.family.life24h.Views;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.family.life24h.Models.objApplication.objAccount.getCurrentUser;
import static com.family.life24h.Utils.keyUtils.NETWORK;
import static com.family.life24h.Utils.keyUtils.accountList;

/**
 *  Date created: 01/12/2020
 *  Last updated: 01/12/2020
 *  Name project: life24h
 *  Description: This is the method that will call every 30 minutes to check if the application is still running or not
 * and send battery percentage to FireBase server
 *  Auth: Lucas Walker
 */

public class UIAppWorker extends Worker {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public UIAppWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if(getCurrentUser()!=null){
            Log.d("UIAppWorker", "doWork()");
            Context context = getApplicationContext();
            mFirebaseInstance = FirebaseDatabase.getInstance();
            //Called each time when 30 minutes (1 second) (the period parameter)
            Log.d("UIAppWorker", "timer sent");
            mFirebaseDatabase = mFirebaseInstance.getReference(accountList);
            mFirebaseDatabase.child(getCurrentUser().getUid()).child(NETWORK).setValue(System.currentTimeMillis()+"");
            return Result.success();
        }
        return Result.failure();
    }

}
