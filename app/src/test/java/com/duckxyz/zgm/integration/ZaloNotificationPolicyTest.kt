package com.duckxyz.zgm.integration

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ZaloNotificationPolicyTest {

    @Test
    fun rejectsAllNotificationProcessingBeforeUserConsent() {
        assertFalse(
            shouldProcessZaloNotification(
                consentGranted = false,
                packageName = ZALO_PACKAGE_NAME
            )
        )
    }

    @Test
    fun acceptsOnlyZaloNotificationsAfterConsent() {
        assertTrue(
            shouldProcessZaloNotification(
                consentGranted = true,
                packageName = ZALO_PACKAGE_NAME
            )
        )
        assertFalse(
            shouldProcessZaloNotification(
                consentGranted = true,
                packageName = "com.example.other"
            )
        )
    }
}
