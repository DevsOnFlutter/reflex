/*

               Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.model.NotificationWear;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NotificationUtils {
    private final static int MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 2 * 60 * 1000;

    public static String NOTIFICATION_INTENT = "notification_event";
    public static String NOTIFICATION_PACKAGE_NAME = "notification_package_name";
    public static String NOTIFICATION_MESSAGE = "notification_message";
    public static String NOTIFICATION_TITLE = "notification_title";

    private static final List<String> listeningPackageNameList = ReflexPlugin.packageNameList;
    private static final List<String> listeningExceptionPackageNameList = ReflexPlugin.packageNameExceptionList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static NotificationWear extractWearNotification(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        Bundle bundle = new Bundle();
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(notification);
        List<NotificationCompat.Action> actions = wearableExtender.getActions();
        List<RemoteInput> remoteInputs = new ArrayList<>(actions.size());
        PendingIntent pendingIntent = null;
        for (NotificationCompat.Action act : actions) {
            if (act != null && act.getRemoteInputs() != null) {
                for (int x = 0; x < act.getRemoteInputs().length; x++) {
                    RemoteInput remoteInput = act.getRemoteInputs()[x];
                    remoteInputs.add(remoteInput);
                    pendingIntent = act.actionIntent;
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            bundle = notification.extras;
        }
        return  new NotificationWear(sbn.getPackageName(),
                pendingIntent,
                remoteInputs,
                bundle,
                sbn.getTag(),
                UUID.randomUUID().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isNewNotification(StatusBarNotification sbn) {
        return sbn.getNotification().when == 0 ||
                (System.currentTimeMillis() - sbn.getNotification().when)
                        < MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTitle(StatusBarNotification sbn) {
        String title;
        if (sbn.getNotification().extras.getBoolean("android.isGroupConversation")) {
            title = sbn.getNotification().extras.getString("android.hiddenConversationTitle");
            // Checking if title is null
            if (title == null) {
                title = sbn.getNotification().extras.getString("android.title");
                int index = title.indexOf(':');
                if (index != -1) {
                    title = title.substring(0, index);
                }
            }

            // Eliminate message count in groups
            Parcelable[] b = (Parcelable[]) sbn.getNotification().extras.get("android.messages");
            if (b != null && b.length > 1) {
                int startIndex = title.lastIndexOf('(');
                if (startIndex != -1) {
                    title = title.substring(0, startIndex);
                }
            }
        } else {
            title = sbn.getNotification().extras.getString("android.title");
        }
        return title;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTitleRaw(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getString("android.title");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static boolean canReply(StatusBarNotification notification) {
        String notificationPackageName = notification.getPackageName();

        List<String> autoReplyPackageNameList = null;

        final Map<String, Object> autoReply = ReflexPlugin.autoReply;

        if(autoReply != null) {

            autoReplyPackageNameList = (List<String>) autoReply.get("packageNameList");

            return isSupportedPackage(notificationPackageName,autoReplyPackageNameList) &&
                    checkListeningAndReplyPackages(notificationPackageName,autoReplyPackageNameList) &&
                    NotificationUtils.isNewNotification(notification);
        }

        // If AutoReply object coming from flutter side is null, AutoReply feature is disabled
        return false;
    }

    private static boolean checkListeningAndReplyPackages(String notificationPackageName,
                                                          List<String> replyPackageNameList) {

        if (listeningPackageNameList == null && listeningExceptionPackageNameList == null &&
                replyPackageNameList == null) {
            return true;
        } else if (listeningExceptionPackageNameList == null && listeningPackageNameList == null) {
            return replyPackageNameList.contains(notificationPackageName);
        } else if (listeningPackageNameList == null && replyPackageNameList == null) {
            return !listeningExceptionPackageNameList.contains(notificationPackageName);
        } else if (listeningPackageNameList == null) {
            return replyPackageNameList.contains(notificationPackageName);
        }

        return listeningPackageNameList.contains(notificationPackageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static boolean isSupportedPackage(String notificationPackageName,
                                              List<String> replyPackageNameList) {
        // If packageNameList coming from flutter is null,
        // then AutoReply is enabled for all packageNames
        if(replyPackageNameList == null) {
            return true;
        }

        // Check notification's package name contained in AutoReply's PackageNameList
        return replyPackageNameList.contains(notificationPackageName);
    }
}
