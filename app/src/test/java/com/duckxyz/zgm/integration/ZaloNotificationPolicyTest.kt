package com.duckxyz.zgm.integration

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ZaloNotificationPolicyTest {

    @Test
    fun allowsZaloOnlyAfterExplicitConsent() {
        assertTrue(
            shouldProcessZaloNotification(
                consentGranted = true,
                packageName = ZALO_PACKAGE_NAME
            )
        )
    }

    @Test
    fun blocksZaloBeforeConsent() {
        assertFalse(
            shouldProcessZaloNotification(
                consentGranted = false,
                packageName = ZALO_PACKAGE_NAME
            )
        )
    }

    @Test
    fun blocksOtherPackagesEvenWithConsent() {
        assertFalse(
            shouldProcessZaloNotification(
                consentGranted = true,
                packageName = "com.example.other"
            )
        )
    }

    @Test
    fun blocksMissingPackageName() {
        assertFalse(
            shouldProcessZaloNotification(
                consentGranted = true,
                packageName = null
            )
        )
    }
}
