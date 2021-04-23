package com.jss.githubtopstars.framework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepoDataSource
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class ReposDataSource(
    private var repoPager: Pager<Int, Repo>
) : RepoDataSource {
    override fun getAll(): Flow<PagingData<Repo>> = repoPager.flow
}