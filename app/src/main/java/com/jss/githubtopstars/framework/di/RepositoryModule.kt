package com.jss.githubtopstars.framework.di

import android.app.Application
import com.jss.core.repository.RepoRepository
import com.jss.githubtopstars.framework.ApiReposDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(app: Application) = RepoRepository(ApiReposDataSource(app))
}