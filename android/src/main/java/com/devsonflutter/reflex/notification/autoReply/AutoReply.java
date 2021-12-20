/*

               Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

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

import com.devsonflutter.reflex.EventCallHandler;
import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.NotificationUtils;
import com.devsonflutter.reflex.notification.model.NotificationWear;

import java.util.HashMap;

import io.flutter.plugin.common.EventChannel;

public class AutoReply {

    public AutoReply(Context context) {
        this.context = context;
    }

    private final Context context;

    private static final String TAG = ReflexPlugin.getPluginTag();


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendReply(StatusBarNotification sbn, String packageName, CharSequence title,String message){
        NotificationWear notificationWear = NotificationUtils.extractWearNotification(sbn);

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
            localBundle.putCharSequence(remoteInputs[i].getResultKey(), message);
            i++;
        }

        RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);

        try {
            if (notificationWear.getPendingIntent() != null) {
                notificationWear.getPendingIntent().send(context, 0, localIntent);
                ReflexPlugin.debugPrint("Auto Reply Sent Successfully...");

                // Sending Reply Data from Java to Flutter
                HashMap<String, Object> data = new HashMap<>();
                data.put("type", "reply");
                data.put("packageName", packageName);
                data.put("title", title);
                data.put("message", message);

                EventChannel.EventSink eventSink = EventCallHandler.getEventSink();
                eventSink.success(data);
            }
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "PendingIntent.CanceledException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
