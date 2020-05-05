package com.droidfeed.util

import com.crashlytics.android.Crashlytics
import com.droidfeed.BuildConfig
import timber.log.Timber

private const val TAG = "DroidFeed"

/**
 * Exception logger.
 *
 * @param throwable
 */
fun logThrowable(throwable: Throwable?) {
    if (BuildConfig.DEBUG) {
        throwable?.printStackTrace()
    } else {
        Crashlytics.logException(throwable)
    }
}

/**
 * Debug console logger.
 *
 * @param message
 */
fun logConsole(message: String) {
    Timber.d(message)
}

fun logd(tag: String, message: String) {
    Timber.d(message)
}