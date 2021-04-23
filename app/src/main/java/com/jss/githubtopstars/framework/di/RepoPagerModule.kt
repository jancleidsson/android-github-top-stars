package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.framework.repository.ReposRemoteMediator
import com.jss.githubtopstars.utils.Constants
import dagger.Module
import dagger.Provides

@Module
@ExperimentalPagingApi
class RepoPagerModule {

    @Provides
    fun provideRepoPager(githubService: GithubService, database: DatabaseService) =
        Pager(
            config = PagingConfig(pageSize = Constants.GITHUB_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ReposRemoteMediator(githubService, database),
            pagingSourceFactory = { database.repositoryDao().getRepos() }
        )
}