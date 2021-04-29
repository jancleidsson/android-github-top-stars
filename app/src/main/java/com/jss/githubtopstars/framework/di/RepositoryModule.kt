package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepoRepository
import com.jss.githubtopstars.framework.repository.RepoDataSource
import dagger.Module
import dagger.Provides

@Module
@ExperimentalPagingApi
class RepositoryModule {
    @Provides
    fun provideRepository(repoPager: Pager<Int, Repo>) = RepoRepository(RepoDataSource(repoPager))
}
