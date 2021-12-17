package com.devsonflutter.reflex.notification.autoReply;

import android.app.PendingIntent;
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

    public AutoReply(Context context) {
        this.context = context;
    }

    private final Context context;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void sendReply(StatusBarNotification sbn){
        NotificationWear notificationWear = ReflexNotification.extractWearNotification(sbn);
        if (notificationWear.getRemoteInputs().isEmpty()) {
            return;
        }

        RemoteInput[] remoteInputs = new RemoteInput[notificationWear.getRemoteInputs().size()];

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // NotificationWear.bundle
        Bundle localBundle = new Bundle();
        int i = 0;
        for (RemoteInput remoteIn : notificationWear.getRemoteInputs()) {
            remoteInputs[i] = remoteIn;
            // This works. Might need additional parameter to make it for Hangouts? (notification_tag?)
            localBundle.putCharSequence(remoteInputs[i].getResultKey(), "[Reflex] Auto Reply");
            i++;
        }

        RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);
        try {
            if (notificationWear.getPendingIntent() != null){
                notificationWear.getPendingIntent().send(context,0,localIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    NotificationHelper.getInstance(context).sendNotification(sbn.getNotification()
                            .extras.getString("android.title"),
                            sbn.getNotification().extras.getString("android.text"),
                            sbn.getPackageName());
                }
            }
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
