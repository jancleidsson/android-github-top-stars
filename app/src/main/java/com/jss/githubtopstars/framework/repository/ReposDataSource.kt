package com.jss.githubtopstars.framework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepositoryDataSource
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.utils.Constants
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class ReposDataSource(
        private val service: GithubService,
        private val database: DatabaseService,
) : RepositoryDataSource {

    var repoPager: Pager<Int, Repo>

    init {
        val pagingConfig = PagingConfig(pageSize = Constants.GITHUB_PAGE_SIZE, enablePlaceholders = false)
        val reposRemoteMediator = ReposRemoteMediator(this.service, this.database)
        val pagingSourceFactory = { database.repositoryDao().getRepos() }
        repoPager =
                Pager(pagingConfig, null, remoteMediator = reposRemoteMediator, pagingSourceFactory)
    }

    override fun getAll(): Flow<PagingData<Repo>> = repoPager.flow
}