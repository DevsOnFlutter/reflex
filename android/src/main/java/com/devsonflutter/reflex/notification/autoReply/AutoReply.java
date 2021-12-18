package com.devsonflutter.reflex.notification.autoReply;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.RemoteInput;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.NotificationHelper;
import com.devsonflutter.reflex.notification.NotificationWear;
import com.devsonflutter.reflex.notification.ReflexNotification;

public class AutoReply {

    public AutoReply(Context context) {
        this.context = context;
    }

    private final Context context;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void sendReply(StatusBarNotification sbn){
        Log.d("[Reflex]","Replying...");
        NotificationWear notificationWear = ReflexNotification.extractWearNotification(sbn);
        Log.d("[Reflex]","Notification Wear Created...");
        if (notificationWear.getRemoteInputs().isEmpty()) {
            return;
        }

        RemoteInput[] remoteInputs = new RemoteInput[notificationWear.getRemoteInputs().size()];
        Log.d("[Reflex]","Remote Inputs Created...");

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // NotificationWear.bundle
        Bundle localBundle = new Bundle();
        int i = 0;
        for (RemoteInput remoteIn : notificationWear.getRemoteInputs()) {
            remoteInputs[i] = remoteIn;
            // This works. Might need additional parameter to make it for Hangouts? (notification_tag?)
            localBundle.putCharSequence(remoteInputs[i].getResultKey(), "[Reflex] Auto Reply");
            Log.d("[Reflex]","Auto Reply Sent...");
            i++;
        }

        RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);
        try {
            if (notificationWear.getPendingIntent() != null){
                notificationWear.getPendingIntent().send(context,0,localIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d("[Reflex]","Notification Helper Send Notification Called from Send Reply...");
                        NotificationHelper.getInstance(context).sendNotification(sbn.getNotification()
                                .extras.getString("android.title"),
                                sbn.getNotification().extras.getString("android.text"),
                                sbn.getPackageName());
                        Log.d("[Reflex]","Sent Successfully...");
                    }
            }
        } catch (PendingIntent.CanceledException e) {
            Log.d("[Reflex]","Error Caught while Notification Helper Send Notification...");
            e.printStackTrace();
        }
    }

    
}
