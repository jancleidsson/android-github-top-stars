package com.jss.githubtopstars.framework.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jss.githubtopstars.framework.UseCases
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.framework.db.RepositoryEntity
import com.jss.githubtopstars.framework.di.ApplicationModule
import com.jss.githubtopstars.framework.di.DaggerViewModelComponent
import com.jss.githubtopstars.framework.networking.GitHubApi
import com.jss.githubtopstars.framework.networking.State
import com.jss.githubtopstars.framework.repository.RepositoryBoundaryCallback
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var useCases: UseCases

    private var reposBoundaryCallback: RepositoryBoundaryCallback
    private var reposList: LiveData<PagedList<RepositoryEntity>>
    private val gitHubApiService = GitHubApi.create()
    private val databaseService = DatabaseService.getInstance(application)

    init {
        DaggerViewModelComponent.builder()
                .applicationModule(ApplicationModule(getApplication()))
                .build()
                .inject(this)

        val config = PagedList.Config.Builder()
                .setPageSize(SEARCH_RESULT_LIMIT)
                .setEnablePlaceholders(false)
                .build()

        val reposRoomDataSourceFactory = databaseService.repositoryDao().getRepos()
        reposBoundaryCallback = RepositoryBoundaryCallback(gitHubApiService, databaseService, Dispatchers.IO);
        reposList = LivePagedListBuilder(reposRoomDataSourceFactory, config).setBoundaryCallback(reposBoundaryCallback).build()
    }

    fun getRepoList(): LiveData<PagedList<RepositoryEntity>> = reposList

    fun getState(): LiveData<State> = reposBoundaryCallback.state

    fun repoListIsEmpty(): Boolean = reposList.value?.isEmpty() ?: true

    companion object {
        const val SEARCH_RESULT_LIMIT = 10
    }
}