package com.devsonflutter.reflex.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
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

public class ReflexNotification {
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

    public void  sendNotification(String title, String message, String packageName)
    {

    }
}
