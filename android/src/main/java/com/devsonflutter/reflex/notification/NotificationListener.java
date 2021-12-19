/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.autoReply.AutoReply;

import java.util.List;
import java.util.Map;

import io.flutter.Log;

/* Notification Listener */
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = ReflexPlugin.getPluginTag();

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        // Package name as title
        String packageName = notification.getPackageName();
        ReflexPlugin.debugPrint("Notification Received From: " + packageName);

        // Extra Payload
        Bundle extras = notification.getNotification().extras;

        Intent intent = new Intent(NotificationUtils.NOTIFICATION_INTENT);
        intent.putExtra(NotificationUtils.NOTIFICATION_PACKAGE_NAME, packageName);

        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

        if(title==null) {
           title = "Untitled Notification";
        }

        if(text == null){
            text = "No message!";
        }

        intent.putExtra(NotificationUtils.NOTIFICATION_TITLE, title.toString());
        intent.putExtra(NotificationUtils.NOTIFICATION_MESSAGE, text.toString());

        // Notification Receiver listens to this broadcast
        sendBroadcast(intent);

        // Sending AutoReply
        Map<String, Object> autoReply = ReflexPlugin.autoReply;
        if(autoReply != null) {
            List<String> autoReplyPackageNameList = (List<String>) autoReply.get("packageNameList");
            assert autoReplyPackageNameList != null;
            if(autoReplyPackageNameList.contains(packageName)) {
                new AutoReply(ReflexPlugin.context).sendReply(notification, (String) autoReply.get("message"));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}

