package com.droidfeed.util.extension

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.droidfeed.util.AnimUtils
import com.google.android.material.textfield.TextInputLayout

/**
 * Hides the soft keyboard.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.fadeIn(duration: Long = AnimUtils.SHORT_ANIM_DURATION) {
    if (isVisible && alpha != 1f) {
        animate().apply {
            this.duration = duration
            alpha(1f)
            start()
        }
    }
}

fun View.fadeOut(
    toAlpha: Float = 0f,
    duration: Long = AnimUtils.SHORT_ANIM_DURATION
) {
    if (isVisible && alpha != toAlpha) {
        animate().apply {
            this.duration = duration
            alpha(toAlpha)
            start()
        }
    }
}


fun ViewGroup.inflate(@LayoutRes resourceId: Int) =
        LayoutInflater.from(context).inflate(
                resourceId,
                this,
                false
        )

fun <T : ViewDataBinding?> ViewGroup.bindingInflate(@LayoutRes resourceId: Int) =
        DataBindingUtil.inflate<T>(
                LayoutInflater.from(context),
                resourceId,
                this,
                false
        )

inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}

fun TextInputLayout.disableHintAnimation() {
    isHintAnimationEnabled = false
}

fun EditText.clearBackground() {
    val paddingBottom = paddingBottom
    val paddingTop = paddingTop
    val paddingLeft = paddingLeft
    val paddingRight = paddingRight
    background = null
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}


fun View.hide() {
    visibility = GONE
}

fun View.show() {
    visibility = VISIBLE
}