package com.jss.githubtopstars.framework.di

import android.app.Application
import com.jss.core.repository.RepoRepository
import com.jss.githubtopstars.framework.repository.ReposDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(app: Application) = RepoRepository(ReposDataSource())
}