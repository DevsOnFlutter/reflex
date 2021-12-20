/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.ReflexPlugin;

import io.flutter.plugin.common.EventChannel.EventSink;
import java.util.HashMap;
import java.util.List;

/* Notification Receiver */
public class NotificationReceiver extends BroadcastReceiver {

    private final EventSink eventSink;

    public NotificationReceiver(EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra(NotificationUtils.NOTIFICATION_PACKAGE_NAME);
        String title = intent.getStringExtra(NotificationUtils.NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NotificationUtils.NOTIFICATION_MESSAGE);

        boolean sendData = false;

        List<String> packageNameList = ReflexPlugin.packageNameList;
        List<String> packageNameExceptionList = ReflexPlugin.packageNameExceptionList;

        if(packageNameList == null && packageNameExceptionList == null){
            sendData = true;
        }
        else if(packageNameList != null) {
            if(packageNameList.contains(packageName)) {
                sendData = true;
            }
        }
        else {
            sendData = !packageNameExceptionList.contains(packageName);
        }

        if(sendData) {
            // Sending Data from Java to Flutter
            HashMap<String, Object> data = new HashMap<>();
            data.put("type", "notification");
            data.put("packageName", packageName);
            data.put("title", title);
            data.put("message", message);

            eventSink.success(data);
        }
    }
}

