package com.droidfeed.ui.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import timber.log.Timber

open class BaseFragment : Fragment {

    var myTag: String? = null

    constructor() {
        Timber.d("BaseFragment init")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onFragmentHidden()
        } else {
            onFragmentShowen()
        }
    }

    fun onFragmentHidden(){}

    fun onFragmentShowen(){}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("BaseFragment onCreateView:$this")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun getFragmentTag(): String {
        return myTag ?: ""
    }

    fun setFragmentTag(tag: String) {
        myTag = tag
    }
}