package com.droidfeed.ui.module.empty

import android.os.Bundle
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityEmptyBinding
import com.droidfeed.ui.common.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class EmptyActivity: BaseActivity<EmptyViewModel, ActivityEmptyBinding>(EmptyViewModel::class.java)  {
    val TAG = EmptyActivity::class.java.simpleName

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFullScreenWindow()
        super.onCreate(savedInstanceState)
        Timber.d("onCreate sharedPrefs:${sharedPrefs},hasAcceptedTerms ${sharedPrefs.hasAcceptedTerms()}")
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_empty
    }

    override fun initViewModel(viewModel: EmptyViewModel) {
        mBinding.text = "测试"
        mBinding.viewModel = mViewModel
    }

}