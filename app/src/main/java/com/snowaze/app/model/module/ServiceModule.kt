package com.snowaze.app.model.module

import com.snowaze.app.model.AccountService
import com.snowaze.app.model.TrackService
import com.snowaze.app.model.impl.AccountServiceImpl
import com.snowaze.app.model.impl.TrackServiceImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideTrackService(impl: TrackServiceImpl): TrackService
}