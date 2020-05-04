package com.droidfeed.ui.module.webview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.droidfeed.R
import com.droidfeed.databinding.ActivityWebviewBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.ui.common.BaseViewModel

class WebViewActivity : BaseActivity<BaseViewModel, ActivityWebviewBinding>(BaseViewModel::class.java) {

    companion object {
        const val EXTRA_URL = "url"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_webview
    }

    override fun initViewModel(viewModel: BaseViewModel) {
        val webSiteUrl = intent.getStringExtra(EXTRA_URL)

        mBinding.apply {
            webView.settings.javaScriptEnabled = true

            url = webSiteUrl
            toolbarTitle = webSiteUrl
            setToolbarHomeNavClickListener {
                onBackPressed()
            }
        }
    }
}