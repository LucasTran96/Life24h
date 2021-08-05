package com.family.life24h.SQLite;

/*
 *  Date created: 02/05/2020
 *  Last updated: 02/05/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.family.life24h.Models.objApplication.objEmergencyAssistance;

import java.util.ArrayList;
import java.util.List;

public class tb_EmergencyAssistance {
    public static final String TAG = tb_EmergencyAssistance.class.getSimpleName();
    private final Context context;

    private db_life24h mDatabase;

    private static final String TABLE_EMERGENCY = "tb_EmergencyAssistance";
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "Uid";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LIST_SEEN = "listSeen";
    private static final String KEY_LIST_RECEIVED = "listReceived";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME_CREATE = "timeCreate";

    private static tb_EmergencyAssistance instance;

    public static tb_EmergencyAssistance getInstance(Context context){
        if(instance == null)
            instance = new tb_EmergencyAssistance(context);
        return instance;
    }

    private tb_EmergencyAssistance(Context context) {
        this.context = context;
        this.mDatabase = db_life24h.getInstance(context);
        if(!mDatabase.checkTableExist(TABLE_EMERGENCY))
            createTable();
    }

    private void createTable() {
        String create_table = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s INTEGER )",
                TABLE_EMERGENCY,
                KEY_ID,
                KEY_LATITUDE,
                KEY_UID,
                KEY_LIST_RECEIVED,
                KEY_LIST_SEEN,
                KEY_LONGITUDE,
                KEY_MESSAGE,
                KEY_TYPE,
                KEY_TIME_CREATE);

        mDatabase.getWritableDatabase().execSQL(create_table);
    }

    public void addOrUpdateEmergency(objEmergencyAssistance emergency) {
        if(!checkItemExist(emergency.getId()))
            addEmergencyAssistance(emergency);
        else
            updateEmergencyAssistance(emergency);

    }

    /**
     *
     * @param emergency object emergency add
     * @return status
     */
    public boolean addEmergencyAssistance(objEmergencyAssistance emergency) {
        if(!checkItemExist(emergency.getId())){
            ContentValues values = new ContentValues();

            values.put(KEY_ID,emergency.getId());
            values.put(KEY_LATITUDE,String.valueOf(emergency.getLatitude()));
            values.put(KEY_LIST_RECEIVED, new Gson().toJson(emergency.getListReceived()));
            values.put(KEY_LIST_SEEN, new Gson().toJson(emergency.getListSeen()));
            values.put(KEY_LONGITUDE,String.valueOf(emergency.getLongitude()));
            values.put(KEY_MESSAGE,String.valueOf(emergency.getMessage()));
            values.put(KEY_TYPE,String.valueOf(emergency.getType()));
            values.put(KEY_TIME_CREATE, emergency.getId());

            SQLiteDatabase db = mDatabase.getWritableDatabase();
            long result = db.insert(TABLE_EMERGENCY, null, values);

            return result > 0;
        }else
            return false;
    }

    /**
     *
     * @param emergency object emergency update
     * @return status
     */
    public boolean updateEmergencyAssistance(objEmergencyAssistance emergency) {
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE,String.valueOf(emergency.getLatitude()));
        values.put(KEY_LIST_RECEIVED, new Gson().toJson(emergency.getListReceived()));
        values.put(KEY_LIST_SEEN, new Gson().toJson(emergency.getListSeen()));
        values.put(KEY_LONGITUDE,String.valueOf(emergency.getLongitude()));
        values.put(KEY_MESSAGE,String.valueOf(emergency.getMessage()));
        values.put(KEY_TYPE,String.valueOf(emergency.getType()));
        values.put(KEY_TIME_CREATE, emergency.getId());

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int result = db.update(TABLE_EMERGENCY, values, KEY_ID + " = ? ", new String[] { emergency.getId() });

        Log.d(TAG, "Update emergency to SQLite");
        return result > 0;
    }

    /**
     *
     * @param emergencyID list Uid seen
     * @param listSeen list Uid seen
     * @return status
     */
    public boolean updateListSeen(String emergencyID, ArrayList<String> listSeen) {
        ContentValues values = new ContentValues();
        values.put(KEY_LIST_SEEN, new Gson().toJson(listSeen));

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int result = db.update(TABLE_EMERGENCY, values, KEY_ID + " = ? ", new String[] { emergencyID });

        Log.d(TAG, "Update list seen emergency to SQLite");
        return result > 0;
    }

    /**
     *
     * @param emergencyID list Uid seen
     * @param listReceived list Uid received
     * @return status
     */
    public boolean updateListReceived(String emergencyID, ArrayList<String> listReceived) {
        ContentValues values = new ContentValues();
        values.put(KEY_LIST_RECEIVED, new Gson().toJson(listReceived));

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int result = db.update(TABLE_EMERGENCY, values, KEY_ID + " = ? ", new String[] { emergencyID });

        Log.d(TAG, "Update list received emergency to SQLite");
        return result > 0;
    }

    /**
     *
     * @param emergencyID id of family
     * @return exist or not exist
     */
    private boolean checkItemExist(String emergencyID){
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_EMERGENCY, KEY_ID, emergencyID);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     *
     * @param Uid user id
     * @return All Emergency of user id
     */
    public ArrayList<objEmergencyAssistance> getAllEmergencyByUid(String Uid){
        //Get all Emergency
        ArrayList<objEmergencyAssistance> allEmergency = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE_EMERGENCY);

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {

            objEmergencyAssistance EmergencyAssistance = new objEmergencyAssistance(
                    cursor.getString(0),
                    Double.parseDouble(cursor.getString(1)),
                    cursor.getString(2),
                    new Gson().fromJson(cursor.getString(3),new TypeToken<List<String>>(){}.getType()),
                    new Gson().fromJson(cursor.getString(4),new TypeToken<List<String>>(){}.getType()),
                    Double.parseDouble(cursor.getString(5)),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getInt(8)
            );

            allEmergency.add(EmergencyAssistance);

            cursor.moveToNext();
        }
        cursor.close();

        return allEmergency;
    }

    public objEmergencyAssistance getEmergencyByID(String emergencyID){

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_EMERGENCY, KEY_ID, emergencyID);
        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            if(cursor.moveToFirst()){

                objEmergencyAssistance EmergencyAssistance = new objEmergencyAssistance(
                        cursor.getString(0),
                        Double.parseDouble(cursor.getString(1)),
                        cursor.getString(2),
                        new Gson().fromJson(cursor.getString(3),new TypeToken<List<String>>(){}.getType()),
                        new Gson().fromJson(cursor.getString(4),new TypeToken<List<String>>(){}.getType()),
                        Double.parseDouble(cursor.getString(5)),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getInt(8)
                );

                cursor.close();
                return EmergencyAssistance;
            }
        }
        return null;
    }

}
