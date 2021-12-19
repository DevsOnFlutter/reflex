package com.devsonflutter.reflex;

import android.util.Log;

import androidx.annotation.NonNull;

import com.devsonflutter.reflex.permission.NotificationPermission;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandler implements MethodChannel.MethodCallHandler {

    private static final String TAG = ReflexPlugin.getPluginTag();
    private static final boolean debug = ReflexPlugin.debug;

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "isPermissionGranted":
                NotificationPermission notificationPermission = new NotificationPermission(ReflexPlugin.context);
                boolean isPermissionGranted = notificationPermission.permissionGranted();
                result.success(isPermissionGranted);
                break;
            case "requestNotificationPermission":
                break;
            case "sendReply":
                Log.d(TAG,"Send Reply Invoked");
                break;
            default:
                result.notImplemented();
                break;
        }

    }
}
