package com.devsonflutter.reflex.notification.model;

import android.app.PendingIntent;
import androidx.core.app.RemoteInput;
import android.os.Bundle;

import java.util.List;

public class NotificationWear {
    private final String packageName;
    private final PendingIntent pendingIntent;
    private final List<RemoteInput> remoteInput;
    private final Bundle bundle;
    private final String tag;
    private final String id;

    public NotificationWear(String packageName,
                            PendingIntent pendingIntent,
                            List<RemoteInput> remoteInput,
                            Bundle bundle,
                            String tag,
                            String id) {
        this.id = id;
        this.bundle = bundle;
        this.packageName = packageName;
        this.pendingIntent = pendingIntent;
        this.tag = tag;
        this.remoteInput = remoteInput;
    }

    public String getTag() {
        return tag;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public List<RemoteInput> getRemoteInputs() {
        return remoteInput;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public String getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }
}
