package com.droidfeed.util

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.droidfeed.BuildConfig

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
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message)
    }
}

fun logger(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, message)
    }
}

fun logd(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, message)
    }
}