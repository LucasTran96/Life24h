package com.family.life24h.Models.objApplication;

/*
 *  Date created: 02/05/2020
 *  Last updated: 02/05/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import com.family.life24h.Models.objectFirebase.fb_emergencyAssistance;

import java.util.List;

public class objEmergencyAssistance extends fb_emergencyAssistance {

    private String id;

    public objEmergencyAssistance(String id) {
        this.id = id;
    }

    public objEmergencyAssistance(String id, fb_emergencyAssistance emergencyAssistance) {
        super(emergencyAssistance.getLatitude(), emergencyAssistance.getAuth(), emergencyAssistance.getListReceived(), emergencyAssistance.getListSeen(), emergencyAssistance.getLongitude(),emergencyAssistance.getMessage(), emergencyAssistance.getType(), emergencyAssistance.getTimeCreate());
        this.id = id;
    }

    public objEmergencyAssistance(String id, double latitude, String Uid, List<String> listReceived, List<String> listSeen, double longitude, String message, String type,  long timeCreate) {
        super(latitude, Uid, listReceived, listSeen, longitude, message, type, timeCreate);
        this.id = id;
    }

    @Override
    public double getLatitude() {
        return super.getLatitude();
    }

    @Override
    public void setLatitude(double latitude) {
        super.setLatitude(latitude);
    }

    @Override
    public List<String> getListReceived() {
        return super.getListReceived();
    }

    @Override
    public void setListReceived(List<String> listReceived) {
        super.setListReceived(listReceived);
    }

    @Override
    public List<String> getListSeen() {
        return super.getListSeen();
    }

    @Override
    public void setListSeen(List<String> listSeen) {
        super.setListSeen(listSeen);
    }

    @Override
    public double getLongitude() {
        return super.getLongitude();
    }

    @Override
    public void setLongitude(double longitude) {
        super.setLongitude(longitude);
    }

    @Override
    public long getTimeCreate() {
        return super.getTimeCreate();
    }

    @Override
    public void setTimeCreate(long timeCreate) {
        super.setTimeCreate(timeCreate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAuth() {
        return super.getAuth();
    }

    @Override
    public void setAuth(String auth) {
        super.setAuth(auth);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }
}
