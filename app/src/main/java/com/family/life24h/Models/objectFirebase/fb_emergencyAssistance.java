package com.family.life24h.Models.objectFirebase;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 *  Date created: 02/04/2020
 *  Last updated: 02/04/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class fb_emergencyAssistance implements Serializable {

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("auth")
    @Expose
    private String auth;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("listReceived")
    @Expose
    private List<String> listReceived = null;
    @SerializedName("listSeen")
    @Expose
    private List<String> listSeen = null;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("timeCreate")
    @Expose
    private long timeCreate;


    public fb_emergencyAssistance() {
    }

    public fb_emergencyAssistance(double latitude, String auth, List<String> listReceived, List<String> listSeen, double longitude, String message, String type, long timeCreate) {
        this.latitude = latitude;
        this.auth = auth;
        this.listReceived = listReceived;
        this.listSeen = listSeen;
        this.longitude = longitude;
        this.timeCreate = timeCreate;
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<String> getListReceived() {
        return listReceived;
    }

    public void setListReceived(List<String> listReceived) {
        this.listReceived = listReceived;
    }

    public List<String> getListSeen() {
        return listSeen;
    }

    public void setListSeen(List<String> listSeen) {
        this.listSeen = listSeen;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

}
