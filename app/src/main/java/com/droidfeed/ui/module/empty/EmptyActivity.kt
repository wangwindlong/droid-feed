package com.droidfeed.ui.module.empty

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityEmptyBinding
import com.droidfeed.databinding.ActivityOnboardBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.ui.common.BaseViewModel
import com.droidfeed.util.logd
import javax.inject.Inject

class EmptyActivity: BaseActivity<EmptyViewModel, ActivityEmptyBinding>(EmptyViewModel::class.java)  {
    val TAG = EmptyActivity::class.java.simpleName

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFullScreenWindow()
        super.onCreate(savedInstanceState)
        logd(TAG, "onCreate sharedPrefs:${sharedPrefs},hasAcceptedTerms ${sharedPrefs.hasAcceptedTerms()}")
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_empty
    }

    override fun initViewModel(viewModel: EmptyViewModel) {
        mBinding.lifecycleOwner = this@EmptyActivity
        mBinding.text = "测试"
        mBinding.viewModel = mViewModel
    }

}