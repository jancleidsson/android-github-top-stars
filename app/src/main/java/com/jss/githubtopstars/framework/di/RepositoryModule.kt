package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import com.jss.githubtopstars.core.repository.RepoRepository
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.framework.repository.ReposDataSource
import dagger.Module
import dagger.Provides

@Module
@ExperimentalPagingApi
class RepositoryModule {
    @Provides
    fun provideRepository(githubService: GithubService, database: DatabaseService) =
        RepoRepository(ReposDataSource(githubService, database))
}
