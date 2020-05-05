package com.droidfeed.ui.module.feed

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import com.droidfeed.R
import com.droidfeed.databinding.FragmentFeedBinding
import com.droidfeed.ui.adapter.BaseUIModelAlias
import com.droidfeed.ui.adapter.UIModelPaginatedAdapter
import com.droidfeed.ui.common.BaseInjectFragment
import com.droidfeed.ui.common.CollapseScrollListener
import com.droidfeed.ui.common.Scrollable
import com.droidfeed.ui.common.WrapContentLinearLayoutManager
import com.droidfeed.ui.module.main.MainViewModel
import com.droidfeed.util.AppRateHelper
import com.droidfeed.util.CustomTab
import com.droidfeed.util.IntentProvider
import com.droidfeed.util.event.EventObserver
import com.droidfeed.util.extension.isOnline
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed.*
import javax.inject.Inject


class FeedFragment : BaseInjectFragment<FeedViewModel, FragmentFeedBinding>(FeedViewModel::class.java, "feed"), Scrollable {

    @Inject
    lateinit var customTab: CustomTab
    @Inject
    lateinit var appRateHelper: AppRateHelper
    @Inject
    lateinit var intentProvider: IntentProvider

    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val paginatedAdapter by lazy { UIModelPaginatedAdapter(lifecycleScope) }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_feed
    }

    override fun initViewModel(viewModel: FeedViewModel) {
        mBinding.viewModel = viewModel
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        subscribePosts()
        subscribeAppRateEvent()
        subscribeIsRefreshing()
        subscribePostOpenEvent()
        subscribePostShareEvent()
        subscribePlayStoreEvent()
        subscribeUnBookmarkEvent()
        subscribeBookmarksOpenEvent()
        mViewModel.setFeedType(FeedType.POSTS)
        initFeed()
        initSwipeRefresh()

        return view
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribePosts() {
        mViewModel.postsLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            paginatedAdapter.submitList(pagedList as PagedList<BaseUIModelAlias>)
        })
    }

    private fun subscribePlayStoreEvent() {
        mViewModel.intentToStart.observe(viewLifecycleOwner, EventObserver { intentType ->
            startActivity(intentProvider.getIntent(intentType))
        })
    }

    private fun initFeed() {
        mBinding.newsRecyclerView.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())

            addOnScrollListener(CollapseScrollListener(lifecycleScope) {
                mainViewModel.onCollapseMenu()
            })

            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

            swapAdapter(
                    paginatedAdapter,
                    false
            )
        }
    }

    private fun subscribeAppRateEvent() {
        mViewModel.showAppRateSnack.observe(viewLifecycleOwner, EventObserver { onAction ->
            appRateHelper.showRateSnackbar(mBinding.root) {
                onAction()
            }
        })
    }

    private fun subscribeIsRefreshing() {
        mViewModel.isRefreshing.observe(viewLifecycleOwner, Observer { isRefreshing ->
            swipeRefreshPosts.isRefreshing = isRefreshing
        })
    }

    private fun initSwipeRefresh() {
        mBinding.swipeRefreshPosts.setOnRefreshListener {
            when {
                context?.isOnline() == true -> mViewModel.refresh()
                else -> {
                    mBinding.swipeRefreshPosts.isRefreshing = false
                    Snackbar.make(
                            mBinding.root,
                            com.droidfeed.R.string.info_no_internet,
                            Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun subscribePostOpenEvent() {
        mViewModel.openPostDetail.observe(viewLifecycleOwner, EventObserver { post ->
            customTab.showTab(post.link)
        })
    }

    private fun subscribePostShareEvent() {
        mViewModel.sharePost.observe(viewLifecycleOwner, EventObserver { post ->
            startActivityForResult(post.getShareIntent(), REQUEST_CODE_SHARE)
        })
    }

    private fun subscribeUnBookmarkEvent() {
        mViewModel.showUndoBookmarkSnack.observe(viewLifecycleOwner, EventObserver { onUndo ->
            Snackbar.make(
                    mBinding.root,
                    com.droidfeed.R.string.info_bookmark_removed,
                    Snackbar.LENGTH_LONG
            ).apply {
                setActionTextColor(Color.YELLOW)
                animationMode = Snackbar.ANIMATION_MODE_SLIDE
                setAction(com.droidfeed.R.string.undo) { onUndo() }
            }.run {
                show()
            }
        })
    }

    private fun subscribeBookmarksOpenEvent() {
        mainViewModel.isBookmarksShown.observe(viewLifecycleOwner, Observer { isEnabled ->
            val feedType = if (isEnabled) FeedType.BOOKMARKS else FeedType.POSTS
            mViewModel.setFeedType(feedType)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SHARE -> mViewModel.onReturnedFromPost()
        }
    }

    override fun scrollToTop() {
        mBinding.newsRecyclerView.smoothScrollToPosition(0)
    }

    companion object {
        private const val REQUEST_CODE_SHARE = 4122
    }


}