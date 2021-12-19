package com.devsonflutter.reflex.notification.autoReply;

import static java.lang.Double.max;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.RemoteInput;

import com.devsonflutter.reflex.notification.NotificationUtils;
import com.devsonflutter.reflex.notification.model.NotificationWear;

public class AutoReply {

    public AutoReply(Context context) {
        this.context = context;
    }

    private final Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendReply(StatusBarNotification sbn){
        if (canReply(sbn)) {
            reply(sbn);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void reply(StatusBarNotification sbn) {
        Log.d("[Reflex]","Replying...");
        NotificationWear notificationWear = NotificationUtils.extractWearNotification(sbn);
        Log.d("[Reflex]","Notification Wear Created...");
        if (notificationWear.getRemoteInputs().isEmpty()) {
            Log.d("[Reflex]","Remote Inputs Empty Returning...");
            return;
        }

        RemoteInput[] remoteInputs = new RemoteInput[notificationWear.getRemoteInputs().size()];
        Log.d("[Reflex]","Remote Inputs Created...");

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // NotificationWear.bundle
        Bundle localBundle = new Bundle();
        int i = 0;
        for (RemoteInput remoteIn : notificationWear.getRemoteInputs()) {
            remoteInputs[i] = remoteIn;
            Log.d("[Reflex]","Remote Inputs " + i + remoteIn.toString());
            // This works. Might need additional parameter to make it for Hangouts? (notification_tag?)
            localBundle.putCharSequence(remoteInputs[i].getResultKey(), "[Reflex] Auto Reply");
            Log.d("[Reflex]","Auto Reply Sent...");
            i++;
        }

        RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);

        try {
            if (notificationWear.getPendingIntent() != null) {
                Log.d("[Reflex]", notificationWear.getPendingIntent().toString());
                notificationWear.getPendingIntent().send(context, 0, localIntent);
                Log.d("[Reflex]","Sent Successfully...");
            }
        } catch (PendingIntent.CanceledException e) {
            Log.d("[Reflex]","notificationWear pending intent send...");
            e.printStackTrace();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean canReply(StatusBarNotification sbn) {
        return isServiceEnabled() &&
                isSupportedPackage(sbn) &&
                NotificationUtils.isNewNotification(sbn) &&
                isGroupMessageAndReplyAllowed(sbn) &&
                canSendReplyNow(sbn);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isGroupMessageAndReplyAllowed(StatusBarNotification sbn) {
        String rawTitle = NotificationUtils.getTitleRaw(sbn);
        //android.text returning SpannableString
        SpannableString rawText = SpannableString.valueOf("" + sbn.getNotification().extras.get("android.text"));
        // Detect possible group image message by checking for colon and text starts with camera icon #181
        boolean isPossiblyAnImageGrpMsg = ((rawTitle != null) && (": ".contains(rawTitle) || "@ ".contains(rawTitle)))
                && ((rawText != null) && rawText.toString().contains("\uD83D\uDCF7"));
        if (!sbn.getNotification().extras.getBoolean("android.isGroupConversation")) {
            return !isPossiblyAnImageGrpMsg;
        }
        // Input from Flutter Side
        return true;
    }

    private boolean isSupportedPackage(StatusBarNotification sbn) {
        // TODO: true if sbn's package name is equal to PackageName in AutoReply
        return true;
    }

    private boolean isServiceEnabled() {
        // TODO: true if AutoReply object coming from Flutter is not null, else false
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean canSendReplyNow(StatusBarNotification sbn) {
        // Do not reply to consecutive notifications from same person/group that arrive in below time
        // This helps to prevent infinite loops when users on both end uses Watomatic or similar app
        int DELAY_BETWEEN_REPLY_IN_MILLISECONDS = 10 * 1000;

        String title = NotificationUtils.getTitle(sbn);
        String selfDisplayName = sbn.getNotification().extras.getString("android.selfDisplayName");
        if (title != null && title.equalsIgnoreCase(selfDisplayName)) { //to protect double reply in case where if notification is not dismissed and existing notification is updated with our reply
            return false;
        }
        long timeDelay = 1000;
        return (System.currentTimeMillis() - 1000 >= max(timeDelay, DELAY_BETWEEN_REPLY_IN_MILLISECONDS));
    }
}
