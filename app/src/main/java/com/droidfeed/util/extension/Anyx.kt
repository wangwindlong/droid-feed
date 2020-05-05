package com.droidfeed.util.extension

import android.util.Log
import timber.log.Timber

//fun Any.logE(message: String) = Timber.e(this::class.java.simpleName, message)
fun Any.logE(message: String) = Timber.e(message)

fun Any.logD(message: String) = Timber.d(message)

fun Any.logV(message: String) = Timber.v(message)

fun Any.logW(message: String) = Timber.w(message)

fun Any.logI(message: String) = Timber.i(message)

fun Any.emptyString() = ""