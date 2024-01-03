package com.xinhui.mobfinalproject.core.di

import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.core.service.AuthServiceImpl
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.core.service.StorageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthService() :AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideStorageService() :StorageService {
        return StorageServiceImpl()
    }
}