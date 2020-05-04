package com.droidfeed.ui.module.launch

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityLauncherBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.ui.module.main.MainActivity
import com.droidfeed.ui.module.onboard.OnBoardActivity
import com.droidfeed.util.logd
import javax.inject.Inject

class LauncherActivity : BaseActivity<LaunchViewModel, ActivityLauncherBinding>(LaunchViewModel::class.java) {
    val TAG = LauncherActivity::class.java.simpleName

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    override fun getLayoutRes(): Int {
        return R.layout.activity_launcher
    }

    override fun initViewModel(viewModel: LaunchViewModel) {
        mBinding.text = "测试"
        mBinding.viewModel = viewModel

        mViewModel.isUserTermsAccepted.observe(this, Observer { isUserTermsAccepted ->
            logd(TAG, "subscribeUserTerms Observer isUserTermsAccepted:${isUserTermsAccepted},isUserTermsAccepted ${mViewModel.isUserTermsAccepted.value}")
            if (!isUserTermsAccepted) {
                startOnBoardActivity()
            } else {
                continueToMainActivity()
            }
            finish()
        })
        logd(TAG, "onCreate sharedPrefs:${sharedPrefs},hasAcceptedTerms ${mViewModel.isUserTermsAccepted.value}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFullScreenWindow()
        super.onCreate(savedInstanceState)
    }


    private fun startOnBoardActivity() {
        Intent(
                this,
                OnBoardActivity::class.java
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.run {
            startActivity(this)
            overridePendingTransition(0, 0)
        }
    }

    private fun continueToMainActivity() {
        sharedPrefs.setHasAcceptedTerms(true)
        Intent(
                this,
                MainActivity::class.java
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.run {
            startActivity(this)
        }
    }




}