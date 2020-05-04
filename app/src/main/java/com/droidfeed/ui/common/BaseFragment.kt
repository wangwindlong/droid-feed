package com.droidfeed.ui.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.droidfeed.util.AnalyticsUtil
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>, private val viewTag: String) : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var analytics: AnalyticsUtil

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun initViewModel(viewModel: VM)

    open lateinit var mBinding: DB
    val mViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory).get(mViewModelClass)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding.lifecycleOwner = this@BaseFragment
        initViewModel(mViewModel)
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        activity?.let { analytics.logScreenView(it, viewTag) }
    }

}