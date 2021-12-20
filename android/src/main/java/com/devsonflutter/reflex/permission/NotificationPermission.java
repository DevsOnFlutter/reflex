/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.permission;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

import com.devsonflutter.reflex.ReflexPlugin;

/* Notification Permission */
public class NotificationPermission {

    @SuppressLint("StaticFieldLeak")
    private static Context context = null;

    public NotificationPermission(Context context) {
        NotificationPermission.context = context;
    }

    public void requestPermission() {
        // Sort out permissions for notifications
        if (!permissionGranted()) {
            Intent permissionScreen = new Intent("android.settings" +
                    ".ACTION_NOTIFICATION_LISTENER_SETTINGS");
            permissionScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ReflexPlugin.debugPrint("Starting Permission Screen");
            context.startActivity(permissionScreen);
        } else {
            ReflexPlugin.debugPrint("Notification Listener Permission already granted!");
        }
    }

    public boolean permissionGranted() {
        String packageName = context.getPackageName();
        String flat = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            String[] names = flat.split(":");
            for (String name : names) {
                ComponentName componentName = ComponentName.unflattenFromString(name);
                boolean nameMatch = TextUtils.equals(packageName, componentName.getPackageName());
                if (nameMatch) {
                    return true;
                }
            }
        }
        return false;
    }
}
