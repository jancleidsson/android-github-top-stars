package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.framework.api.GithubService
import dagger.Module
import dagger.Provides

@Module
class GithubServiceModule {
    @Provides
    fun provideGithubService() = GithubService.create()
}