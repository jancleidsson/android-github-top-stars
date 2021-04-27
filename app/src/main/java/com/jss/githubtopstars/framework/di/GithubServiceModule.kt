package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.utils.ServiceLocator
import dagger.Module
import dagger.Provides

@Module
class GithubServiceModule {
    @Provides
    fun provideGithubService() = ServiceLocator.provideGithubService()
}