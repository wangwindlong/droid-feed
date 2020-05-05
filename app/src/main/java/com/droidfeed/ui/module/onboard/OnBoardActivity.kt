package com.droidfeed.ui.module.onboard

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityOnboardBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.ui.module.main.MainActivity
import com.droidfeed.ui.module.navigate.NavigateActivity
import com.droidfeed.util.CustomTab
import com.droidfeed.util.event.EventObserver
import com.droidfeed.util.extension.getClickableSpan
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class OnBoardActivity : BaseActivity<OnBoardViewModel, ActivityOnboardBinding>(OnBoardViewModel::class.java) {
    val TAG = OnBoardActivity::class.java.simpleName

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    private val customTab = CustomTab(this)


    override fun getLayoutRes(): Int {
        return R.layout.activity_onboard
    }

    override fun initViewModel(viewModel: OnBoardViewModel) {
        mBinding.cbAgreement.movementMethod = LinkMovementMethod.getInstance()

        mBinding.onBoardViewModel = viewModel
        mBinding.termsOfServiceSpan = getTermsOfUseSpan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFullScreenWindow()
        super.onCreate(savedInstanceState)

        Timber.d("onCreate sharedPrefs:${sharedPrefs},hasAcceptedTerms ${sharedPrefs.hasAcceptedTerms()}")
        subscribeNavigationEvents()
    }

    private fun subscribeNavigationEvents() {
        mViewModel.openUrl.observe(this, EventObserver { url ->
            customTab.showTab(url)
        })

        mViewModel.showSnackBar.observe(this, EventObserver { stringId ->
            Snackbar.make(
                    mBinding.root,
                    stringId,
                    Snackbar.LENGTH_LONG
            ).setAnchorView(mBinding.cbAgreement).show()
        })

        mViewModel.openMainActivity.observe(this, EventObserver {
            continueToMainActivity()
            finish()
        })
    }

    private fun getTermsOfUseSpan() = getString(
            R.string.i_agree_to,
            getString(R.string.terms_of_service)
    ).getClickableSpan(
            getString(R.string.terms_of_service)
    ) {
        mViewModel.onTermsOfUseClicked()
    }

    private fun continueToMainActivity() {
        sharedPrefs.setHasAcceptedTerms(true)
        Timber.d("continueToMainActivity sharedPrefs:${sharedPrefs},hasAcceptedTerms ${sharedPrefs.hasAcceptedTerms()}")
        Intent(
                this,
                NavigateActivity::class.java
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.run {
            startActivity(this)
        }
    }


}