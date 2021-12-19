package com.devsonflutter.reflex.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.ReflexPlugin;
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
            NotificationChannel notificationChannel = new NotificationChannel("reflex",
                    "reflex_channel", NotificationManager.IMPORTANCE_DEFAULT);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendNotification(String title, String message, String packageName){
        Log.d("[Reflex]","Sending Notification...");
        SUPPORTED_APPS.add(new App("WhatsApp", "com.whatsapp"));
        for (App supportedApp : SUPPORTED_APPS) {
            if (supportedApp.getPackageName().equalsIgnoreCase(packageName)) {
                title = supportedApp.getName() + ":" + title;
                break;
            }
        }
        Intent intent = new Intent(appContext, ReflexPlugin.class);
        Log.d("[Reflex]","Intent Created...");

        intent.putExtra("package", packageName);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pIntent = PendingIntent.getActivity(appContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("[Reflex]","Pending Intent Created..");


//        int appIconResourceId = appContext.getApplicationInfo().icon;
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext, "reflex")
//                .setGroup("reflex-" + packageName)
//                .setGroupSummary(false)
//                .setContentTitle("Reflex Running...")
//                .setContentText("Auto Reply Service Running...")
//                .setSmallIcon(appIconResourceId)
//                .setAutoCancel(true)
//                .setContentIntent(pIntent);
//
//        Log.d("[Reflex]","notificationBuilder Created..");
//
//
//        int notificationId = (int) System.currentTimeMillis();
//        notificationManager.notify(notificationId, notificationBuilder.build());
//        Log.d("[Reflex]","Notification Manager Notified..");
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
