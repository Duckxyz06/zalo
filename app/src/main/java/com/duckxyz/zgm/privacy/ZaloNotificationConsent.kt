package com.duckxyz.zgm.privacy

import android.content.Context

object ZaloNotificationConsent {
    private const val FILE_NAME = "zgm_notification_consent"
    private const val KEY_VERSION = "consent_version"
    private const val KEY_GRANTED_AT = "granted_at"
    private const val CURRENT_VERSION = 1

    fun hasConsent(context: Context): Boolean =
        preferences(context).getInt(KEY_VERSION, 0) == CURRENT_VERSION

    fun grant(context: Context) {
        preferences(context)
            .edit()
            .putInt(KEY_VERSION, CURRENT_VERSION)
            .putLong(KEY_GRANTED_AT, System.currentTimeMillis())
            .apply()
    }

    fun revoke(context: Context) {
        preferences(context)
            .edit()
            .remove(KEY_VERSION)
            .remove(KEY_GRANTED_AT)
            .apply()
    }

    fun grantedAt(context: Context): Long? =
        preferences(context)
            .getLong(KEY_GRANTED_AT, 0L)
            .takeIf { it > 0L }

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
}
