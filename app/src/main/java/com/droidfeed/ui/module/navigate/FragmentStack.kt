package com.droidfeed.ui.module.navigate

import android.text.TextUtils
import com.droidfeed.util.extension.logD
import java.lang.Exception

class FragmentStack {
    var arrayList: ArrayList<String>
    var count: Int = 0

    constructor(count: Int) {
        arrayList = ArrayList()
        this.count = count
    }

    companion object {
        private var stack_object: FragmentStack? = null
        fun getInstance(count: Int): FragmentStack {
            if (stack_object == null)
                stack_object = FragmentStack(count)
            return stack_object!!
        }
    }

    fun addAll(array: ArrayList<String>?) {
        arrayList.addAll(array!!)
    }

    fun push(tag: String) {
        logD("push tag=$tag")
        remove(tag)
        arrayList.add(tag)
    }

    fun pop(tag: String): String? {
        if (remove(tag)) {
            if (arrayList.size == 0) {
                return null
            } else {
                return arrayList.get(arrayList.size - 1)
            }
        } else {
            return null
        }
    }

    fun remove(tag: String): Boolean {
        try {
            var index_value = arrayList.indexOf(tag)
            arrayList.removeAt(index_value)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun toString(): String {
        return TextUtils.join(", ", arrayList);
    }
}