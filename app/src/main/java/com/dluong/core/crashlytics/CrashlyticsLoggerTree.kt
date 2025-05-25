package com.dluong.core.crashlytics

import android.util.Log
import timber.log.Timber

/**
 * A custom Timber tree for logging messages to Crashlytics.
 *
 * This class extends the Timber.Tree class and overrides the isLoggable and log methods.
 * It filters log messages based on their priority level and sends them to Crashlytics.
 */
class CrashlyticsLoggerTree : Timber.Tree() {
    /**
     * Determines whether the log message should be printed based on the priority level.
     *
     * @param tag The tag used for logging.
     * @param priority The priority level of the log message.
     * @return True if the log message should be printed, false otherwise.
     */
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.INFO
    }
    /**
     * Logs a message to Crashlytics.
     *
     * @param priority The priority level of the log message.
     * @param tag The tag used for logging.
     * @param message The log message.
     * @param t An optional throwable associated with the log message.
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        t ?: return

        val priorityString = when (priority) {
            Log.INFO -> "Log.INFO"
            Log.WARN -> "Log.WARN"
            Log.ERROR -> "Log.ERROR"
            Log.ASSERT -> "Log.ASSERT"
            else -> "Log.$priority"
        }
        val tagString = tag?.ifEmpty { "<empty>" } ?: "<null>"
        val messageString = message.ifEmpty { "<empty>" }
        val throwableMessage = t.message ?: "<null>"
        /* TODO: Log all infos to Firebase Crashlytics
        FirebaseCrashlytics.getInstance().log(
            """
            $priorityString
            $tagString
            $messageString
            $throwableMessage
            """.trimIndent()
         */
    }
}