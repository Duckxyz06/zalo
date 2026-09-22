package com.duckxyz.zgm.integration

import android.content.Context

private const val CONSENT_PREFS = "zgm_notification_consent"
private const val KEY_CONSENT_GRANTED = "zalo_notification_consent_granted"

fun shouldProcessZaloNotification(
    consentGranted: Boolean,
    packageName: String?
): Boolean =
    consentGranted && packageName == ZALO_PACKAGE_NAME

object ZaloNotificationConsent {

    fun isGranted(context: Context): Boolean =
        context.getSharedPreferences(
            CONSENT_PREFS,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_CONSENT_GRANTED, false)

    fun grant(context: Context) {
        context.getSharedPreferences(
            CONSENT_PREFS,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(KEY_CONSENT_GRANTED, true)
            .apply()
    }

    fun revoke(context: Context) {
        context.getSharedPreferences(
            CONSENT_PREFS,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(KEY_CONSENT_GRANTED, false)
            .apply()
    }
}
