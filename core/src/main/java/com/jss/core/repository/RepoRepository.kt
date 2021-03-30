package com.jss.core.repository

import com.jss.core.data.Repository

class RepoRepository(private val dataSource: RepositoryDataSource) {
    suspend fun getAll() = dataSource.getAll()
    suspend fun addAll(reposList: List<Repository>) = dataSource.addAll(reposList)
}