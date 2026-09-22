package com.duckxyz.zgm.integration

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.duckxyz.zgm.data.local.ZgmDatabase
import com.duckxyz.zgm.data.local.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ZaloNotificationListenerService : NotificationListenerService() {

    private val serviceScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database by lazy {
        ZgmDatabase.getInstance(applicationContext)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

        activeNotifications
            .orEmpty()
            .filter { it.packageName == ZALO_PACKAGE_NAME }
            .forEach { statusBarNotification ->
                processNotification(
                    statusBarNotification,
                    snapshotOnly = true
                )
            }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != ZALO_PACKAGE_NAME) return
        processNotification(sbn, snapshotOnly = false)
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification,
        rankingMap: RankingMap,
        reason: Int
    ) {
        if (sbn.packageName != ZALO_PACKAGE_NAME) return

        serviceScope.launch {
            if (reason == REASON_CLICK) {
                database.zaloSignalDao()
                    .markReadByNotificationKey(sbn.key)
            } else {
                database.zaloSignalDao()
                    .markInactive(sbn.key)
            }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun processNotification(
        sbn: StatusBarNotification,
        snapshotOnly: Boolean
    ) {
        val notification = sbn.notification
        val extras = notification.extras

        val title =
            extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE)
                ?.toString()
                ?: extras.getCharSequence(Notification.EXTRA_TITLE)
                    ?.toString()

        val text =
            extras.getCharSequence(Notification.EXTRA_TEXT)
                ?.toString()

        val bigText =
            extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
                ?.toString()

        val signal = buildZaloSignal(
            title = title,
            text = text,
            bigText = bigText,
            postedAt = sbn.postTime
        )?.copy(notificationKey = sbn.key)
            ?: return

        ZaloNotificationOpenRegistry.register(
            conversationKey = signal.conversationKey,
            pendingIntent = notification.contentIntent
        )

        serviceScope.launch {
            if (snapshotOnly) {
                database.zaloSignalDao()
                    .recordSnapshot(signal.toEntity())
            } else {
                database.zaloSignalDao()
                    .recordPosted(signal.toEntity())
            }
        }
    }
}
