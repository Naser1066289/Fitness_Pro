package com.runora_dev.runoraf.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.runora_dev.runoraf.R;


/*
    Class for activating the reminder
 */
public class AlarmReceiver extends BroadcastReceiver {

    NotificationsController nc = new NotificationsController();
    int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) { //this function will be called depends on the time slice given
        Log.d("RECEIVE", "Testing");
        Toast.makeText(context.getApplicationContext(), "Drink a Cup of Water", Toast.LENGTH_LONG).show();
        NotificationsController.notify(context, context.getString(R.string.notification_string), id);   //message displayed as notification
        id++;
    }
}
