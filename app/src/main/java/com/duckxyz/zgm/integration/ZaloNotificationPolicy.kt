package com.duckxyz.zgm.integration

fun shouldProcessZaloNotification(
    consentGranted: Boolean,
    packageName: String?
): Boolean =
    consentGranted && packageName == ZALO_PACKAGE_NAME
