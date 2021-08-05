package com.family.life24h.Views.UISafety;

/*
 *  Date created: 01/09/2020
 *  Last updated: 01/09/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    void onLocationChanged(Location location);

    void onProviderDisabled(String provider);

    void onProviderEnabled(String provider);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onGpsStatusChanged(int event);

}
