package com.droidfeed.ui.module.navigate

import android.text.TextUtils
import java.lang.Exception

class FragmentStack {
    var arrayList: ArrayList<String> = ArrayList()

    companion object {
        private var stack_object: FragmentStack? = null
        fun getInstance(): FragmentStack {
            if (stack_object == null)
                stack_object = FragmentStack()
            return stack_object!!
        }
    }

    fun addAll(array: ArrayList<String>?) {
        arrayList.addAll(array!!)
    }

    fun push(tag: String) {
        remove(tag)
        arrayList.add(tag)
    }

    fun pop(tag: String): String? {
        return if (remove(tag)) {
            if (this.arrayList.size == 0) {
                null
            } else {
                arrayList[arrayList.size - 1]
            }
        } else {
            null
        }
    }

    private fun remove(tag: String): Boolean {
        try {
            arrayList.removeAt(arrayList.indexOf(tag))
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun toString(): String {
        return TextUtils.join(", ", arrayList);
    }
}