package com.jss.githubtopstars.utils

import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataSource(var repoPagingData: PagingData<Repo>) : RepoDataSource {
    override fun getAll(): Flow<PagingData<Repo>> {
        return flowOf(repoPagingData)
    }
}