package com.family.life24h.Views.autoStartService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.family.life24h.Views.UISafety.SpeedService;


/**
 * Author: Lucaswalker@jexpa.com
 * Class: UIBootReceiver handle restarting the application when the device reboot is finished
 * and send plush data to the server
 * History: 2019/12/17
 * Project: Life24h
 */

/*
 *  Date created: 02/12/2020
 *  Last updated: 02/11/2020
 *  Name project: life24h
 *  Description:
 *  Auth: James Ryan
 */

public class UIBootReceiver extends BroadcastReceiver {

    @Override
        public void onReceive(Context context, Intent arg1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context,GPSService.class));
                context.startForegroundService(new Intent(context, SpeedService.class));
            } else {
                context.startService(new Intent(context,GPSService.class));
                context.startService(new Intent(context,SpeedService.class));
            }

        }

}
