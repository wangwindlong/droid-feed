package com.droidfeed.ui.module.main

import android.animation.ArgbEvaluator
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidfeed.R
import com.droidfeed.databinding.ActivityMainBinding
import com.droidfeed.ui.adapter.BaseUIModelAlias
import com.droidfeed.ui.adapter.UIModelAdapter
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.ui.module.onboard.OnBoardActivity
import com.droidfeed.util.AnimUtils
import com.droidfeed.util.ColorPalette
import com.droidfeed.util.event.EventObserver
import com.droidfeed.util.extension.hideKeyboard
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(MainViewModel::class.java) {
    val TAG = MainActivity::class.java.simpleName

    @Inject lateinit var navController: MainNavController
    @Inject lateinit var animUtils: AnimUtils
    @Inject lateinit var colorPalette: ColorPalette

    private var currentMenuColor = 0
    private var previousScreenColor = 0
    private var previousMenuButton: View? = null
    private val linearLayoutManager = LinearLayoutManager(this)
    private val uiModelAdapter = UIModelAdapter(lifecycleScope, linearLayoutManager)

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(viewModel: MainViewModel) {
        mBinding.viewModel = viewModel
        mBinding.appbar.containerView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Timber.d("onCreate mainViewModel:${mViewModel},isUserTermsAccepted ${mViewModel.isUserTermsAccepted.value}")
        subscribeUserTerms()
        subscribeNavigation()
        subscribeScrollTopEvent()
        subscribeSources()
        subscribeMenuVisibility()
        subscribeFilterVisibility()
        subscribeCloseKeyboard()
        subscribeSourceShareEvent()
        subscribeSourceRemoveUndoSnack()

        initFilterDrawer()
    }

    private fun subscribeSourceShareEvent() {
        mViewModel.shareSourceEvent.observe(this, EventObserver { content ->
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, content)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }.also(this::startActivity)
        })
    }

    private fun subscribeSourceRemoveUndoSnack() {
        mViewModel.showUndoSourceRemoveSnack.observe(this, EventObserver { onUndo ->
            Snackbar.make(
                mBinding.root,
                R.string.info_source_removed,
                Snackbar.LENGTH_LONG
            ).apply {
                setActionTextColor(Color.YELLOW)
                animationMode = Snackbar.ANIMATION_MODE_SLIDE
                setAction(R.string.undo) { onUndo() }
            }.run {
                show()
            }
        })
    }

    private fun subscribeCloseKeyboard() {
        mViewModel.closeKeyboardEvent.observe(this, EventObserver {
            edtFeedUrl.hideKeyboard()
        })
    }

    private fun subscribeSources() {
        mViewModel.sourceUIModelData.observe(this, Observer { sourceUIModels ->
            uiModelAdapter.addUIModels(sourceUIModels as List<BaseUIModelAlias>)
        })
    }

    private fun subscribeFilterVisibility() {
        mViewModel.isSourceFilterVisible.observe(this, EventObserver { isVisible ->
            if (isVisible) {
                mBinding.drawerLayout.openDrawer(GravityCompat.END)
            } else {
                mBinding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        })
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun subscribeNavigation() {
        mViewModel.onNavigation.observe(this, Observer { destination ->
            if (navController.activeDestination != destination) {
                navController.open(destination)

                when (destination) {
                    Destination.FEED -> {
                        highlightSelectedMenuItem(mBinding.appbar.menu.btnNavHome)
                        onMenuItemSelected(colorPalette.transparent)
                        lightStatusBarTheme()
                    }
                    Destination.CONTRIBUTE -> {
                        highlightSelectedMenuItem(mBinding.appbar.menu.btnNavContribute)
                        onMenuItemSelected(colorPalette.gray)
                        darkStatusBarTheme()
                    }
                    Destination.ABOUT -> {
                        highlightSelectedMenuItem(mBinding.appbar.menu.btnNavAbout)
                        onMenuItemSelected(colorPalette.pink)
                        darkStatusBarTheme()
                    }
                    Destination.CONFERENCES -> {
                        highlightSelectedMenuItem(mBinding.appbar.menu.btnNavConferences)
                        onMenuItemSelected(colorPalette.transparent)
                        lightStatusBarTheme()
                    }
                }
            }
        })
    }

    private fun subscribeUserTerms() {
        Timber.d("subscribeUserTerms mViewModel:${mViewModel},sharedPrefs.isUserTermsAccepted ${mViewModel.sharedPrefs.hasAcceptedTerms()}")
        mViewModel.isUserTermsAccepted.observe(this, Observer { isUserTermsAccepted ->
            Timber.d("subscribeUserTerms Observer isUserTermsAccepted:${isUserTermsAccepted},isUserTermsAccepted ${mViewModel.isUserTermsAccepted.value}")
            if (!isUserTermsAccepted) {
                startOnBoardActivity()
            }
        })
    }

    private fun subscribeScrollTopEvent() {
        mViewModel.scrollTop.observe(this, EventObserver {
            navController.scrollToTop()
        })
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

    private fun highlightSelectedMenuItem(menuItem: View) {
        menuItem.isSelected = true
        previousMenuButton?.isSelected = false
        previousMenuButton = menuItem
    }

    private fun onMenuItemSelected(color: Int) {
        mBinding.appbar.btnMenu.isSelected = false
        animateMenuButton()
        animateMenuColor(color)
        animateTitleColor(false)

        if (color != colorPalette.transparent) {
            animateNavigationBarColor(color)
        } else {
            animateNavigationBarColor(colorPalette.black)
        }

        currentMenuColor = color
    }

    private fun subscribeMenuVisibility() {
        mViewModel.isMenuVisible.observe(this, Observer { isVisible ->
            if (mBinding.appbar.menu.containerMenu.isVisible != isVisible) {
                val color = when {
                    isVisible -> colorPalette.accent
                    else -> currentMenuColor
                }

                mBinding.appbar.btnMenu.run {
                    speed = if (isVisible) 1f else -1f
                    resumeAnimation()
                }

                animateMenuColor(color)
                animateTitleColor(isVisible)

                if (isVisible) {
                    mBinding.root.hideKeyboard()
                }
            }
        })
    }

    private fun animateMenuColor(color: Int) {
        ValueAnimator.ofArgb(
            colorPalette.accent,
            color
        ).apply {
            interpolator = animUtils.linearOutSlowInInterpolator
            duration = ANIM_DURATION
            addUpdateListener { animator ->
                window.statusBarColor = animator.animatedValue as Int
                mBinding.appbar.containerToolbar.setBackgroundColor(animator.animatedValue as Int)
            }
        }.run {
            start()
        }
    }

    private fun animateNavigationBarColor(color: Int) {
        ValueAnimator.ofArgb(
            previousScreenColor,
            color
        ).apply {
            interpolator = animUtils.linearOutSlowInInterpolator
            duration = ANIM_DURATION
            addUpdateListener { animator ->
                window.navigationBarColor = animator.animatedValue as Int
            }
        }.run {
            start()
        }

        previousScreenColor = color
    }

    private fun animateMenuButton() {
        mBinding.appbar.btnMenu.run {
            speed = if (isSelected) 1f else -1f
            resumeAnimation()
        }
    }

    private fun animateTitleColor(isActive: Boolean) {
        val isLightThemed = when (navController.activeDestination) {
            Destination.CONFERENCES,
            Destination.FEED -> true
            else -> false
        }

        val commonColor = when {
            isLightThemed -> colorPalette.grayDark
            else -> colorPalette.white
        }

        val (fromColor, toColor) = when {
            isActive -> commonColor to colorPalette.white
            else -> colorPalette.white to commonColor
        }

        ObjectAnimator.ofInt(
            mBinding.appbar.txtToolbarTitle,
            "textColor",
            fromColor,
            toColor
        ).apply {
            setEvaluator(ArgbEvaluator())
            interpolator = animUtils.linearOutSlowInInterpolator
            duration = ANIM_DURATION
        }.run {
            start()
        }
    }

    private fun initFilterDrawer() {
        mBinding.filterRecycler.apply {
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            adapter = uiModelAdapter
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = linearLayoutManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setFilterPaddingForCutout()
        }

        mBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                mViewModel.onFilterDrawerClosed()
                drawerView.hideKeyboard()
            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setFilterPaddingForCutout() {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        mBinding.filterView.setOnApplyWindowInsetsListener { _, insets ->
            val displayCutout = insets.displayCutout
            mBinding.filterView.setPadding(0, displayCutout?.safeInsetTop ?: 0, 0, 0)
            insets.consumeDisplayCutout()
        }
    }

    override fun onBackPressed() {
        val isFilterDrawerOpen = mBinding.drawerLayout.isDrawerOpen(GravityCompat.END)

        mViewModel.onBackPressed(isFilterDrawerOpen) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val ANIM_DURATION = 300L
    }
}