package com.droidfeed.ui.module.navigate

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.databinding.ActivityNavigateBinding
import com.droidfeed.ui.common.BaseActivity
import com.droidfeed.util.extension.logD
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject


class NavigateActivity : BaseActivity<NavigateViewModel, ActivityNavigateBinding>(NavigateViewModel::class.java) {
    internal enum class Page(val clazz: Class<*>, val id: Int, val item: Int) {
        HOME(HomeFragment::class.java, R.id.navigation_find, 0),
        DASHBOARD(DashboardFragment::class.java, R.id.navigation_shelf, 1),
        NOTIFICATION(NotificationFragment::class.java, R.id.navigation_mind, 2),
        SETTINGS(SettingFragment::class.java, R.id.navigation_info, 3);

        companion object {
            fun from(s: String): Page? = values().find { it.toString()== s }
            fun from(id: Int): Page? = values().find { it.id== id }
        }

        override fun toString(): String {
            return clazz.name
        }
    }

    private var currPage: Page? = null

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val page = Page.from(item.itemId)
            if (page != null) setPage(page)
            return true
        }
    }
    var navigation: BottomNavigationView? = null

    @Inject
    lateinit var sharedPrefs: SharedPrefsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupFullScreenWindow()
        super.onCreate(savedInstanceState)
        navigation = mBinding.bottomAppBar
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState != null) {
            var stack = savedInstanceState.getStringArrayList("fragStack")
            if (stack != null && stack.size > 0) {
                logD("stack=${TextUtils.join("", stack)}")
                FragmentStack.getInstance().addAll(stack)
            }
            currPage = savedInstanceState.getSerializable("currentPage") as? Page
        }
        if (currPage == null) currPage = Page.HOME
        navigation?.selectedItemId = currPage!!.id
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (currPage != null) {
            outState.putSerializable("currentPage", currPage!!)
            outState.putStringArrayList("fragStack", FragmentStack.getInstance().arrayList)
        }
        super.onSaveInstanceState(outState)
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
        transaction.commit()
        FragmentStack.getInstance().push(tag)
        currPage = page
    }

    override fun onBackPressed() {
        val currentTag = currPage.toString()
        val currentFrag = supportFragmentManager.findFragmentByTag(currentTag)
        val childfragmanager: FragmentManager? = currentFrag?.childFragmentManager
        if (childfragmanager?.popBackStackImmediate() != true) {
            val toptag = FragmentStack.getInstance().pop(currentTag)
            if (toptag != null) {
                currPage = Page.from(toptag)
                navigation?.selectedItemId = currPage?.id ?: Page.HOME.id
            } else {
                if (currentTag == Page.HOME.toString()) {
                    super.onBackPressed()
                } else {
                    navigation?.selectedItemId = Page.HOME.id
                }
            }
        }
    }

}