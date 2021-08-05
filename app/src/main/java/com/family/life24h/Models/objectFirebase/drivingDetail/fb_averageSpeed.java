package com.family.life24h.Models.objectFirebase.drivingDetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 *  Date created: 02/17/2020
 *  Last updated: 02/17/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */


public class fb_averageSpeed {

    @SerializedName("listAverageSpeed")
    @Expose
    private List<Double> listAverageSpeed = null;
    @SerializedName("timeUpdateAverageSpeed")
    @Expose
    private long timeUpdateAverageSpeed;

    public fb_averageSpeed() {
    }



    public fb_averageSpeed(List<Double> listAverageSpeed, long timeUpdateAverageSpeed) {
        this.listAverageSpeed = listAverageSpeed;
        this.timeUpdateAverageSpeed = timeUpdateAverageSpeed;
    }

    public List<Double> getListAverageSpeed() {
        return listAverageSpeed;
    }

    public void setListAverageSpeed(List<Double> listAverageSpeed) {
        this.listAverageSpeed = listAverageSpeed;
    }

    public long getTimeUpdateAverageSpeed() {
        return timeUpdateAverageSpeed;
    }

    public void setTimeUpdateAverageSpeed(long timeUpdateAverageSpeed) {
        this.timeUpdateAverageSpeed = timeUpdateAverageSpeed;
    }

}
