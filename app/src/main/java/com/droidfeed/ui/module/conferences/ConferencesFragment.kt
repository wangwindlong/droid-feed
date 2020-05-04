package com.droidfeed.ui.module.conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidfeed.R
import com.droidfeed.databinding.FragmentConferencesBinding
import com.droidfeed.ui.adapter.BaseUIModelAlias
import com.droidfeed.ui.adapter.UIModelAdapter
import com.droidfeed.ui.common.BaseFragment
import com.droidfeed.ui.common.CollapseScrollListener
import com.droidfeed.ui.common.WrapContentLinearLayoutManager
import com.droidfeed.ui.module.main.MainViewModel
import com.droidfeed.util.CustomTab
import com.droidfeed.util.event.EventObserver
import javax.inject.Inject

class ConferencesFragment : BaseFragment<ConferencesViewModel, FragmentConferencesBinding>(ConferencesViewModel::class.java,"conferences") {

    @Inject lateinit var customTab: CustomTab

    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    private val linearLayoutManager = LinearLayoutManager(context)
    private val uiModelAdapter: UIModelAdapter by lazy {
        UIModelAdapter(
            lifecycleScope,
            linearLayoutManager
        )
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_conferences
    }

    override fun initViewModel(viewModel: ConferencesViewModel) {
        mBinding.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        subscribeOpenUrl()
        subscribeConferences()
        initFeed()

        return view
    }

    private fun subscribeOpenUrl() {
        mViewModel.openUrl.observe(viewLifecycleOwner, EventObserver { url ->
            customTab.showTab(url)
        })
    }

    private fun subscribeConferences() {
        mViewModel.conferences.observe(viewLifecycleOwner, Observer { uiModels ->
            uiModelAdapter.addUIModels(uiModels as List<BaseUIModelAlias>)
        })
    }

    private fun initFeed() {
        mBinding.newsRecyclerView.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())

            addOnScrollListener(CollapseScrollListener(lifecycleScope) {
                mainViewModel.onCollapseMenu()
            })

            swapAdapter(
                uiModelAdapter,
                false
            )
        }
    }
}