package com.devsonflutter.reflex.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.devsonflutter.reflex.notification.model.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class NotificationHelper {
    final private Context appContext;
    private static NotificationHelper _INSTANCE;
    private static NotificationManager notificationManager;
    private static final JSONObject appsList = new JSONObject();
    public Set<App> SUPPORTED_APPS = new HashSet<App>();

    private NotificationHelper(Context appContext) {
        this.appContext = appContext;
        init();
    }

    private void init() {
        notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("reflex", "reflex_channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        SUPPORTED_APPS.add(new App("WhatsApp", "com.whatsapp"));

        for (App supportedApp : SUPPORTED_APPS) {
            try {
                appsList.put(supportedApp.getPackageName(), false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static NotificationHelper getInstance(Context context) {
        if (_INSTANCE == null) {
            _INSTANCE = new NotificationHelper(context);
        }
        return _INSTANCE;
    }

    public void sendNotification(String title, String message, String packageName){
        SUPPORTED_APPS.add(new App("WhatsApp", "com.whatsapp"));
        for (App supportedApp : SUPPORTED_APPS) {
            if (supportedApp.getPackageName().equalsIgnoreCase(packageName)) {
                title = supportedApp.getName() + ":" + title;
                break;
            }
        }
        Intent intent = new Intent(appContext, ReflexNotification.class);
        intent.putExtra("package", packageName);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pIntent = PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext, "reflex")
                .setGroup("reflex-" + packageName)
                .setGroupSummary(false)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StatusBarNotification[] notifications = notificationManager.getActiveNotifications();
            for (App supportedApp : SUPPORTED_APPS) {
                try {
                    appsList.put(supportedApp.getPackageName(), false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (StatusBarNotification notification : notifications) {
                if (notification.getPackageName().equalsIgnoreCase("REFLEX")) {
                    setNotificationSummaryShown(notification.getNotification().getGroup());
                }
            }
        }
        int notifId = (int) System.currentTimeMillis();
        notificationManager.notify(notifId, notificationBuilder.build());

        try {
            if (!appsList.getBoolean(packageName)) {
                appsList.put(packageName, true);
                //Need to create one summary notification, this will help group all individual notifications
                NotificationCompat.Builder summaryNotificationBuilder = new NotificationCompat.Builder(appContext, "reflex")
                        .setGroup("reflex-" + packageName)
                        .setGroupSummary(true)
                        .setAutoCancel(true)
                        .setContentIntent(pIntent);
                notificationManager.notify(notifId + 1, summaryNotificationBuilder.build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setNotificationSummaryShown(String packageName) {
        if (packageName != null) {
            packageName = packageName.replace("Reflex-", "");
            try {
                appsList.put(packageName, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
