/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.notification.NotificationListener;
import com.devsonflutter.reflex.notification.NotificationReceiver;
import com.devsonflutter.reflex.notification.NotificationUtils;
import com.devsonflutter.reflex.permission.NotificationPermission;

import java.util.List;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;

public class EventCallHandler implements EventChannel.StreamHandler {

    private EventChannel.EventSink mEventSink = null;
    private final Context context;
    private final NotificationPermission notificationPermission;

    EventCallHandler(Context context) {
        this.context = context;
        notificationPermission = new NotificationPermission(context);
    }

    private static final String TAG = ReflexPlugin.getPluginTag();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) arguments;

        Map<String, Object> map = (Map<String, Object>) list.get(0);
        Boolean debug = (Boolean) map.get("debug");
        List<String> packageNameList = (List<String>) map.get("packageNameList");
        List<String> packageNameExceptionList = (List<String>) map.get("packageNameExceptionList");
        Map<String, String> autoReply = (Map<String,String>) map.get("autoReply");

        ReflexPlugin.packageNameList = packageNameList;
        ReflexPlugin.packageNameExceptionList = packageNameExceptionList;

        mEventSink = events;
        listenNotification(mEventSink);
        Log.w(TAG,"Listening Reflex Stream...");
    }

    @Override
    public void onCancel(Object arguments) {
        mEventSink = null;
        Log.w(TAG,"Closing Reflex Stream...");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void listenNotification(EventChannel.EventSink eventSink) {
        if (notificationPermission.permissionGranted()) {
            // Set up receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NotificationUtils.NOTIFICATION_INTENT);

            NotificationReceiver receiver = new NotificationReceiver(eventSink);
            context.registerReceiver(receiver, intentFilter);

            // Listener intent
            Intent intent = new Intent(context, NotificationListener.class);
            context.startService(intent);
            Log.i(TAG, "Started the notification tracking service.");
        } else {
            notificationPermission.requestPermission();
            Log.e(TAG, "Failed to start notification tracking; Permissions were not yet granted.");
        }
    }
}
