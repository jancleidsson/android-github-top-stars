package com.jss.githubtopstars.framework.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jss.core.data.Repository
import com.jss.githubtopstars.framework.networking.GitHubApi
import kotlin.coroutines.CoroutineContext

class ReposPageKeyedDataSourceFactory(
    private val gitHubApi: GitHubApi,
    private val coroutineContext: CoroutineContext
) :
    DataSource.Factory<Int, Repository>() {

    var reposDataSourceLiveData = MutableLiveData<ReposPageKeyedDataSource>()

    override fun create(): DataSource<Int, Repository> {
        val reposDataSource = ReposPageKeyedDataSource(gitHubApi, coroutineContext)
        reposDataSourceLiveData.postValue(reposDataSource)
        return reposDataSource
    }
}