package com.devsonflutter.reflex.notification
import android.app.PendingIntent
import android.os.Bundle
import androidx.core.app.RemoteInput

data class NotificationWear(
    val packageName: String,
    val pendingIntent: PendingIntent?,
    val remoteInputs: List<RemoteInput>,
    val bundle: Bundle?,
    val tag: String?,
    val id: String
)