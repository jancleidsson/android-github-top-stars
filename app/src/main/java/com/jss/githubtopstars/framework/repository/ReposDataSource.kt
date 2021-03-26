package com.jss.githubtopstars.framework.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jss.core.data.Repository
import com.jss.core.repository.RepositoryDataSource
import com.jss.githubtopstars.framework.networking.GitHubApi
import com.jss.githubtopstars.framework.vm.RepoListViewModel
import kotlinx.coroutines.Dispatchers

class ReposDataSource : RepositoryDataSource {

    private var reposList: LiveData<PagedList<Repository>>
    private val gitHubApi = GitHubApi.create()
    private val reposPageKeyedDataSourceFactory: ReposPageKeyedDataSourceFactory
    private val coroutineContext = Dispatchers.IO

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(RepoListViewModel.SEARCH_RESULT_LIMIT)
            .setEnablePlaceholders(false)
            .build()

        reposPageKeyedDataSourceFactory = ReposPageKeyedDataSourceFactory(gitHubApi, coroutineContext)
        reposList = LivePagedListBuilder(reposPageKeyedDataSourceFactory, config).build()
    }

//   fun getRepos() = reposList

    override suspend fun getAll(): List<Repository> {
        val repositories = arrayListOf<Repository>()
        val response = GitHubApi.create().getRepositories()
        if (response.isSuccessful) response.body()?.let { page -> page.items?.let { repositories.addAll(it) } }
        return repositories
    }

}