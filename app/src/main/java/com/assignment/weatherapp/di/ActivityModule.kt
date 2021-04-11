package com.assignment.weatherapp.di

import com.assignment.weatherapp.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector()
    abstract fun contributesHomeActivity(): HomeActivity
}