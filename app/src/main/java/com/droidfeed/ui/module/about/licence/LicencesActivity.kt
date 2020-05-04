package com.droidfeed.ui.module.about.licence

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidfeed.R
import com.droidfeed.databinding.ActivityLicenceBinding
import com.droidfeed.ui.adapter.BaseUIModelAlias
import com.droidfeed.ui.adapter.UIModelAdapter
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.util.CustomTab
import com.droidfeed.util.event.EventObserver

class LicencesActivity : BaseActivity<LicencesViewModel, ActivityLicenceBinding>(LicencesViewModel::class.java) {

    override fun getLayoutRes(): Int {
        return R.layout.activity_licence
    }

    override fun initViewModel(viewModel: LicencesViewModel) {
        mBinding.toolbarTitle = getString(R.string.licences)
        mBinding.toolbarHomeNavClickListener = View.OnClickListener {
            licencesViewModel.onBackNavigation()
        }

        mBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            overScrollMode = View.OVER_SCROLL_NEVER
            adapter = licenceAdapter
        }
    }

    private val linearLayoutManager = LinearLayoutManager(this)
    private val licenceAdapter: UIModelAdapter by lazy {
        UIModelAdapter(
            lifecycleScope,
            linearLayoutManager
        )
    }

    private val licencesViewModel: LicencesViewModel by viewModels { viewModelFactory }

    private val customTab: CustomTab by lazy { CustomTab(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.apply {
            val pinkColor = ContextCompat.getColor(
                this@LicencesActivity,
                R.color.pink
            )
            statusBarColor = pinkColor
            navigationBarColor = pinkColor
        }

        super.onCreate(savedInstanceState)

        subscribeOpenUrl()
        subscribeLicenceUIModels()
        subscribeOnBackNavigation()
    }

    private fun subscribeOpenUrl() {
        licencesViewModel.openUrl.observe(this, EventObserver { url ->
            customTab.showTab(url)
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribeLicenceUIModels() {
        licencesViewModel.licenceUIModels.observe(this, Observer { uiModels ->
            licenceAdapter.addUIModels(uiModels as List<BaseUIModelAlias>)
        })
    }

    private fun subscribeOnBackNavigation() {
        licencesViewModel.onBackNavigation.observe(this, Observer {
            onBackPressed()
        })
    }
}