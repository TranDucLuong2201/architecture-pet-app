package com.dluong.designsystem.core.utils

/*
Copy from: https://github.com/android/architecture-samples/blob/03d9af0b6a1ab82e450ca7c45f973a119412a9ab/app/src/main/java/com/example/android/architecture/blueprints/todoapp/util/CoroutinesUtils.kt#L29
and RxMobile Team
 */

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow


private const val STOP_TIMEOUT_MILLIS = 5000L

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */

val WHILE_UI_SUBSCRIBED: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS)