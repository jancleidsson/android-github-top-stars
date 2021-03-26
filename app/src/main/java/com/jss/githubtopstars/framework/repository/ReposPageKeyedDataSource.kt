package com.jss.githubtopstars.framework.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jss.githubtopstars.framework.networking.State
import com.jss.core.data.Repository
import com.jss.githubtopstars.framework.networking.GitHubApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ReposPageKeyedDataSource(
        private val gitHubApiService: GitHubApi,
        coroutineContext: CoroutineContext
) : PageKeyedDataSource<Int, Repository>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private val job = Job()
    private val scope = CoroutineScope(coroutineContext + job)

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Repository>
    ) {
        updateState(State.LOADING)
        scope.launch {
            val response = gitHubApiService.getRepositories()
            if (response.isSuccessful) {
                updateState(State.DONE)
                val reposList = response.body()?.items.orEmpty()
                callback.onResult(reposList, null, 2)
            } else {
                //TODO: Check error case
                updateState(State.ERROR)
                Log.e("DataSourceError", "Failed to fetch data.")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        updateState(State.LOADING)
        scope.launch {
            val response = gitHubApiService.getRepositories(null, params.key)
            if (response.isSuccessful) {
                updateState(State.DONE)
                val reposList = response.body()?.items.orEmpty()
                callback.onResult(reposList, params.key.dec())
            } else {
                updateState(State.ERROR)
                Log.e("DataSourceError", "Failed to fetch data.")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        updateState(State.LOADING)
        scope.launch {
            val response = gitHubApiService.getRepositories(null, params.key)
            if (response.isSuccessful) {
                updateState(State.DONE)
                val reposList = response.body()?.items.orEmpty()
                callback.onResult(reposList, params.key.inc())
            } else {
                updateState(State.ERROR)
                Log.e("DataSourceError", "Failed to fetch data.")
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}