package com.xinhui.mobfinalproject.core.di

import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.data.repo.user.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun provideUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }
}