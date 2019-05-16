package com.flymrgmail.kioskdima;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;


public class ScreenOfReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF))) {
            String action = intent.getAction();

            if(Intent.ACTION_SCREEN_OFF.equals(action))
            {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
                wakeLock.acquire();

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  0, pi);
            }
        }
    }

    public IntentFilter getFilter() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        return filter;
    }

}
