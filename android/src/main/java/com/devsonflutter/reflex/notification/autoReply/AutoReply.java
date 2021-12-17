package com.devsonflutter.reflex.notification.autoReply;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;
import androidx.core.app.RemoteInput;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.NotificationHelper;
import com.devsonflutter.reflex.notification.NotificationWear;
import com.devsonflutter.reflex.notification.ReflexNotification;

public class AutoReply {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendReply(StatusBarNotification sbn){
        NotificationWear notificationWear = ReflexNotification.extractWearNotification(sbn);
        if (notificationWear.getRemoteInputs().isEmpty()) {
            return;
        }

        RemoteInput[] remoteInputs = new RemoteInput[notificationWear.getRemoteInputs().size()];

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle localBundle = new Bundle();//notificationWear.bundle;
        int i = 0;
        for (RemoteInput remoteIn : notificationWear.getRemoteInputs()) {
            remoteInputs[i] = remoteIn;
            // This works. Might need additional parameter to make it for Hangouts? (notification_tag?)
            localBundle.putCharSequence(remoteInputs[i].getResultKey(), "Hii");
            i++;
        }

        RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);
        try {
            if (notificationWear.getPendingIntent() != null){
                notificationWear.getPendingIntent().send(this,0,localIntent);
                NotificationHelper.getInstance(getApplicationContext()).sendNotification(sbn.getNotification().extras.getString("android.title"), sbn.getNotification().extras.getString("android.text"), sbn.getPackageName());
            }
        }

    }
}
