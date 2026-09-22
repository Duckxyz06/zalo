package com.duckxyz.zgm.integration

import android.content.Context

object ZaloAccessibilityPreferences {
    private const val FILE_NAME = "zgm_zalo_accessibility"
    private const val KEY_ENABLED = "experimental_enabled"

    fun isEnabled(context: Context): Boolean =
        context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_ENABLED, false)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }
}
