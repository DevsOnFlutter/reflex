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
import com.devsonflutter.reflex.notification.autoReply.AutoReply;
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
        mEventSink = events;

        // Get arguments from flutter side
        List<Map<String, Object>> list = (List<Map<String, Object>>) arguments;
        Map<String, Object> args = (Map<String, Object>) list.get(0);

        boolean debug = Boolean.parseBoolean(String.valueOf(args.get("debug")));
        List<String> packageNameList = (List<String>) args.get("packageNameList");
        List<String> packageNameExceptionList = (List<String>) args.get("packageNameExceptionList");
        Map<String, Object> autoReply = (Map<String,Object>) args.get("autoReply");

        ReflexPlugin.debug = debug;
        ReflexPlugin.packageNameList = packageNameList;
        ReflexPlugin.packageNameExceptionList = packageNameExceptionList;
        if(autoReply != null) {
            ReflexPlugin.autoReply = autoReply;
        }

        // Start listening notification
        listenNotification(mEventSink);

        ReflexPlugin.debugPrint("Listening Reflex Stream...");
    }

    @Override
    public void onCancel(Object arguments) {
        mEventSink = null;
        ReflexPlugin.debugPrint("Closing Reflex Stream...");
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

            ReflexPlugin.debugPrint("Notification Listening Service Started...");
        } else {
            Log.e(TAG, "Failed to start notification listener; Permission not granted.");
            Log.i(TAG,"Call requestPermission before Initialising!");
        }
    }
}
