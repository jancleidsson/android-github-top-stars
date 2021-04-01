package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.framework.api.GithubService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GithubServiceModule {

    @Provides
    @Singleton
    fun provideGithubService() = GithubService.create()
}