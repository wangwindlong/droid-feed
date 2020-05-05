@file:Suppress("unused")

package com.droidfeed.di.navigation

import com.droidfeed.ui.module.navigate.DashboardFragment
import com.droidfeed.ui.module.navigate.HomeFragment
import com.droidfeed.ui.module.navigate.NotificationFragment
import com.droidfeed.ui.module.navigate.SettingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NavigationFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeFindFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingFragment
}