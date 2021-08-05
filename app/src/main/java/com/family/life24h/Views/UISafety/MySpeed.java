package com.family.life24h.Views.UISafety;

/*
 *  Date created: 01/09/2020
 *  Last updated: 01/09/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.location.Location;

public class MySpeed extends Location {

    public MySpeed(Location location) {
        // TODO Auto-generated constructor stub
        super(location);
    }

    @Override
    public float distanceTo(Location dest) {
        // TODO Auto-generated method stub
        float nDistance = super.distanceTo(dest);
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        // TODO Auto-generated method stub
        float nAccuracy = super.getAccuracy();
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        // TODO Auto-generated method stub
        double nAltitude = super.getAltitude();
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        // TODO Auto-generated method stub
        float nSpeed = super.getSpeed() * 3.6f;
        return nSpeed;
    }

}
