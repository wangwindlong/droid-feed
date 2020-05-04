package com.droidfeed.ui.module.about

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.droidfeed.BuildConfig
import com.droidfeed.R
import com.droidfeed.databinding.ActivityLicenceBinding
import com.droidfeed.databinding.FragmentAboutBinding
import com.droidfeed.ui.common.BaseFragment
import com.droidfeed.ui.module.about.licence.LicencesActivity
import com.droidfeed.ui.module.about.licence.LicencesViewModel
import com.droidfeed.util.AnimUtils.Companion.MEDIUM_ANIM_DURATION
import com.droidfeed.util.CustomTab
import com.droidfeed.util.IntentProvider
import com.droidfeed.util.event.EventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("ValidFragment")
class AboutFragment : BaseFragment<AboutViewModel, FragmentAboutBinding>(AboutViewModel::class.java,"about") {

    @Inject lateinit var customTab: CustomTab
    @Inject lateinit var intentProvider: IntentProvider

    override fun getLayoutRes(): Int {
        return R.layout.fragment_about
    }

    override fun initViewModel(viewModel: AboutViewModel) {
        mBinding.viewModel = viewModel
        mBinding.appVersion = getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        subscribeStartIntentEvent()
        subscribeOpenUrlEvent()
        subscribeOpenLicenceEvent()

        initAnimations()
        return view
    }

    private fun subscribeOpenLicenceEvent() {
        mViewModel.openLicences.observe(viewLifecycleOwner, EventObserver {
            Intent(context, LicencesActivity::class.java)
                .also { intent ->
                    startActivity(intent)
                }
        })
    }

    private fun subscribeOpenUrlEvent() {
        mViewModel.openUrl.observe(viewLifecycleOwner, EventObserver { url ->
            customTab.showTab(url)
        })
    }

    private fun subscribeStartIntentEvent() {
        mViewModel.startIntent.observe(viewLifecycleOwner, EventObserver { intentType ->
            startActivity(intentProvider.getIntent(intentType))
        })
    }

    private fun initAnimations() {
        mBinding.animView.setOnClickListener { view ->
            if (!(view as LottieAnimationView).isAnimating) {
                view.speed *= -1f
                view.resumeAnimation()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            mBinding.animView.frame = 0
            delay(MEDIUM_ANIM_DURATION)
            mBinding.animView.resumeAnimation()
        }
    }

}