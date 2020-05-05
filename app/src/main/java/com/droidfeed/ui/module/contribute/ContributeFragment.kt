package com.droidfeed.ui.module.contribute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.droidfeed.R
import com.droidfeed.databinding.FragmentContributeBinding
import com.droidfeed.ui.common.BaseInjectFragment
import com.droidfeed.util.AnimUtils.Companion.MEDIUM_ANIM_DURATION
import com.droidfeed.util.CustomTab
import com.droidfeed.util.event.EventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributeFragment : BaseInjectFragment<ContributeViewModel, FragmentContributeBinding>(ContributeViewModel::class.java,"contribute") {

    @Inject
    lateinit var customTab: CustomTab

    override fun getLayoutRes(): Int {
        return R.layout.fragment_contribute
    }

    override fun initViewModel(viewModel: ContributeViewModel) {
        mBinding.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        subscribeOpenRepositoryEvent()
        initAnimations()
        return view
    }

    private fun subscribeOpenRepositoryEvent() {
        mViewModel.openRepositoryEvent.observe(viewLifecycleOwner, EventObserver { url ->
            customTab.showTab(url)
        })
    }

    private fun initAnimations() {
        mBinding.animView.setOnClickListener {
            if (!mBinding.animView.isAnimating) {
                mBinding.animView.speed *= -1f
                mBinding.animView.resumeAnimation()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            mBinding.animView.frame = 0
            delay(MEDIUM_ANIM_DURATION)
            mBinding.animView.resumeAnimation()
        }
    }

}