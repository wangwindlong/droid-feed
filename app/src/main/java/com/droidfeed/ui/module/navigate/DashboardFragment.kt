package com.droidfeed.ui.module.navigate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.droidfeed.R
import com.droidfeed.databinding.FragmentHomeBinding
import com.droidfeed.ui.common.BaseInjectFragment
import com.droidfeed.ui.module.empty.CountingFragment
import com.droidfeed.ui.module.empty.EmptyViewModel
import com.droidfeed.util.event.EventObserver

class DashboardFragment : BaseInjectFragment<EmptyViewModel, FragmentHomeBinding>(EmptyViewModel::class.java, "feed") {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initViewModel(viewModel: EmptyViewModel) {
        mBinding.viewModel = mViewModel
        mBinding.text = "DashboardFragment"
        mBinding.index = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mViewModel.jump2Fragment.observe(viewLifecycleOwner, EventObserver{ index ->
            // Instantiate a new fragment.
            val newFragment = CountingFragment.newInstance(index)

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            newFragment?.let { ft.replace(R.id.simple_fragment, it) }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.addToBackStack("DashboardFragment\$fragment$index")
            ft.commit()
        })
        return view
    }
}