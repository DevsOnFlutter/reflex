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

import com.devsonflutter.reflex.notification.model.App;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NotificationUtils {
    private final static int MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 2 * 60 * 1000;

    public static String NOTIFICATION_INTENT = "notification_event";
    public static String NOTIFICATION_PACKAGE_NAME = "notification_package_name";
    public static String NOTIFICATION_MESSAGE = "notification_message";
    public static String NOTIFICATION_TITLE = "notification_title";
    public static Set<App> SUPPORTED_APPS = new HashSet<App>();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static  NotificationWear extractWearNotification(StatusBarNotification sbn){
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
        //For apps targeting {@link android.os.Build.VERSION_CODES#N} and above, this time is not shown
        //by default unless explicitly set by the apps hence checking not 0
        return sbn.getNotification().when == 0 ||
                (System.currentTimeMillis() - sbn.getNotification().when) < MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTitle(StatusBarNotification sbn) {
        String title;
        if (sbn.getNotification().extras.getBoolean("android.isGroupConversation")) {
            title = sbn.getNotification().extras.getString("android.hiddenConversationTitle");
            //Just to avoid null cases, if by any chance hiddenConversationTitle comes null for group message
            // then extract group name from title
            if (title == null) {
                title = sbn.getNotification().extras.getString("android.title");
                int index = title.indexOf(':');
                if (index != -1) {
                    title = title.substring(0, index);
                }
            }

            //To eliminate the case where group title has number of messages count in it
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

    public static String getTitleRaw(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getString("android.title");
    }
}
