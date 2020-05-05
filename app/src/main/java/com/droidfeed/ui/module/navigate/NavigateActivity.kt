package com.droidfeed.ui.module.navigate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityNavigateBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.util.extension.logD
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import java.lang.reflect.Field
import javax.inject.Inject


class NavigateActivity: BaseActivity<NavigateViewModel, ActivityNavigateBinding>(NavigateViewModel::class.java)  {
    internal enum class Page(val clazz: Class<*>, val id: Int, val item: Int) {
        HOME(HomeFragment::class.java, R.id.navigation_find, 0),
        DASHBOARD(DashboardFragment::class.java, R.id.navigation_shelf, 1),
        NOTIFICATION(NotificationFragment::class.java, R.id.navigation_mind, 2),
        SETTINGS(SettingFragment::class.java, R.id.navigation_info, 3);

        companion object {
            fun from(s: String): Page? = values().find { it.clazz.name == s }
        }
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            logD("item=${item}")
            when (item.getItemId()) {
                R.id.navigation_find -> setPage(Page.HOME)
                R.id.navigation_shelf -> setPage(Page.DASHBOARD)
                R.id.navigation_mind -> setPage(Page.NOTIFICATION)
                R.id.navigation_info -> setPage(Page.SETTINGS)
                else -> return false
            }
            return true
        }
    }
    var navigation: BottomNavigationView? = null

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupFullScreenWindow()
        super.onCreate(savedInstanceState)
        Timber.d("onCreate sharedPrefs:${sharedPrefs},hasAcceptedTerms ${sharedPrefs.hasAcceptedTerms()}")
        navigation = mBinding.bottomAppBar
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation?.selectedItemId = R.id.navigation_find;
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_navigate
    }

    override fun initViewModel(viewModel: NavigateViewModel) {
        mBinding.text = "测试"
        mBinding.viewModel = mViewModel
    }


    private fun setPage(page: Page) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val tag: String = page.clazz.name
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // hide everything
        for (fragment in fragmentManager.fragments) {
            transaction.hide(fragment)
        }

        // Retrieve fragment instance, if it was already created
        var fragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) { // If not, crate new instance and add it
            try {
                fragment = page.clazz.newInstance() as Fragment
                fragment.arguments = Bundle()
                transaction.add(R.id.frame, fragment, tag)
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        } else { // otherwise just show it
            transaction.show(fragment)
        }
        transaction.addToBackStack(tag)
        transaction.commit()
    }

    fun getBackStackLastEntry():Int {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index < 0) return R.id.navigation_find
        val backEntry = supportFragmentManager.getBackStackEntryAt(index)
        val tag = backEntry.name

//        return Page.from(tag ?: HomeFragment::class.java.name)?.id ?: R.id.navigation_find
        return Page.from(tag ?: HomeFragment::class.java.name)?.item ?: 0
    }

    override fun onBackPressed() {
//        if (!supportFragmentManager.findFragmentByTag(Page.HOME.clazz.name)!!.isDetached) {
//            super.onBackPressed()
//        } else {
//            navigation!!.selectedItemId = R.id.navigation_find
//        }
        super.onBackPressed()
        logD("onBackPressed count=${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount >= 1) {
            navigation!!.menu.getItem(getBackStackLastEntry()).isChecked = true
        } else {
            finish()
        }
    }

    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode: Field = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.setAccessible(true)
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.setAccessible(false)
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Timber.e("Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Timber.e("Unable to change value of shift mode")
        }
    }

}