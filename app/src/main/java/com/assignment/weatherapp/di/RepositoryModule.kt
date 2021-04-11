package com.assignment.weatherapp.di

import com.assignment.weatherapp.repo.HomeRepository
import com.assignment.weatherapp.repo.HomeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}