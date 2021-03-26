package com.jss.githubtopstars.framework.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jss.core.data.Repository
import com.jss.githubtopstars.framework.UseCases
import com.jss.githubtopstars.framework.di.ApplicationModule
import com.jss.githubtopstars.framework.di.DaggerViewModelComponent
import com.jss.githubtopstars.framework.networking.GitHubApi
import com.jss.githubtopstars.framework.networking.State
import com.jss.githubtopstars.framework.repository.ReposPageKeyedDataSource
import com.jss.githubtopstars.framework.repository.ReposPageKeyedDataSourceFactory
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var useCases: UseCases

    private var reposList: LiveData<PagedList<Repository>>
    private val gitHubApi = GitHubApi.create()
    private val reposPageKeyedDataSourceFactory: ReposPageKeyedDataSourceFactory
    private val coroutineContext = Dispatchers.IO

    init {
        DaggerViewModelComponent.builder()
                .applicationModule(ApplicationModule(getApplication()))
                .build()
                .inject(this)

        val config = PagedList.Config.Builder()
                .setPageSize(SEARCH_RESULT_LIMIT)
                .setEnablePlaceholders(false)
                .build()

        reposPageKeyedDataSourceFactory = ReposPageKeyedDataSourceFactory(gitHubApi, coroutineContext)
        reposList = LivePagedListBuilder(reposPageKeyedDataSourceFactory, config).build()
    }

    fun getRepoList(): LiveData<PagedList<Repository>> = reposList

    fun getState(): LiveData<State> = Transformations.switchMap(reposPageKeyedDataSourceFactory.reposDataSourceLiveData, ReposPageKeyedDataSource::state)

    fun listIsEmpty(): Boolean {
        return reposList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        invalidateDataSource()
    }

    private fun invalidateDataSource() =
            reposPageKeyedDataSourceFactory.reposDataSourceLiveData.value?.invalidate()

    companion object {
        const val SEARCH_RESULT_LIMIT = 10
    }
}