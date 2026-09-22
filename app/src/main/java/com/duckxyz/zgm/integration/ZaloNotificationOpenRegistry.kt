package com.duckxyz.zgm.integration

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.concurrent.ConcurrentHashMap

object ZaloNotificationOpenRegistry {
    private val conversationIntents =
        ConcurrentHashMap<String, PendingIntent>()

    fun register(
        conversationKey: String,
        pendingIntent: PendingIntent?
    ) {
        if (pendingIntent != null) {
            conversationIntents[conversationKey] = pendingIntent
        }
    }

    fun clear() {
        conversationIntents.clear()
    }

    fun openConversationOrZalo(
        context: Context,
        conversationKey: String
    ): Boolean {
        conversationIntents[conversationKey]?.let { pending ->
            try {
                pending.send()
                return true
            } catch (_: PendingIntent.CanceledException) {
                conversationIntents.remove(conversationKey)
            }
        }

        val launchIntent = context.packageManager
            .getLaunchIntentForPackage(ZALO_PACKAGE_NAME)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ?: return false

        context.startActivity(launchIntent)
        return true
    }
}
