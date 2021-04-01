package com.jss.githubtopstars.core.repository

import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import kotlinx.coroutines.flow.Flow

interface RepositoryDataSource {
    fun getAll(): Flow<PagingData<Repo>>
}