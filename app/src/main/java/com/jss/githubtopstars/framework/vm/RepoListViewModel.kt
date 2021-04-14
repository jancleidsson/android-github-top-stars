package com.jss.githubtopstars.framework.vm

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.UseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class RepoListViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    fun getReposList(): Flow<PagingData<Repo>> = useCases.getAllRepos()
}