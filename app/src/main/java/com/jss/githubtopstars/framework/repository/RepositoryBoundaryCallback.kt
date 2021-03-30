package com.jss.githubtopstars.framework.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList.BoundaryCallback
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.framework.db.RepositoryEntity
import com.jss.githubtopstars.framework.networking.GitHubApi
import com.jss.githubtopstars.framework.networking.State
import com.jss.githubtopstars.framework.vm.RepoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RepositoryBoundaryCallback(
    private val gitHubApiService: GitHubApi,
    private val databaseService: DatabaseService,
    coroutineContext: CoroutineContext
) : BoundaryCallback<RepositoryEntity>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private val scope = CoroutineScope(coroutineContext)

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        if (state.value == State.LOADING) return
        updateState(State.LOADING)
        scope.launch {
            val response = gitHubApiService.getRepositories()
            if (response.isSuccessful) {
                val reposList = response.body()?.items.orEmpty()
                databaseService.repositoryDao()
                    .addRepos(reposList.map { RepositoryEntity.fromRepository(it) })
                updateState(State.DONE)
            } else {
                updateState(State.ERROR)
                Log.e("DataSourceError", "Failed to fetch data.")
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepositoryEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
        if (state.value == State.LOADING) return
        updateState(State.LOADING)
        //TODO: Change next page store
        val nextPage = (itemAtEnd.repositoryId / RepoListViewModel.SEARCH_RESULT_LIMIT) + 1
        Log.d("JAN", "Next Page: $nextPage  Item at End Loaded: $itemAtEnd")
        scope.launch {
            val response = gitHubApiService.getRepositories(null, nextPage.toInt())
            if (response.isSuccessful) {
                val reposList = response.body()?.items.orEmpty()
                databaseService.repositoryDao()
                    .addRepos(reposList.map { RepositoryEntity.fromRepository(it) })
                updateState(State.DONE)
            } else {
                updateState(State.ERROR)
                Log.e("DataSourceError", "Failed to fetch data.")
            }
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}