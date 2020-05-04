package com.droidfeed.util.extension

import android.util.Log
import timber.log.Timber

fun Any.logE(message: String) = Timber.e(this::class.java.simpleName, message)

fun Any.logD(message: String) = Timber.d(this::class.java.simpleName, message)

fun Any.logV(message: String) = Timber.v(this::class.java.simpleName, message)

fun Any.logW(message: String) = Timber.w(this::class.java.simpleName, message)

fun Any.logI(message: String) = Timber.i(this::class.java.simpleName, message)

fun Any.emptyString() = ""