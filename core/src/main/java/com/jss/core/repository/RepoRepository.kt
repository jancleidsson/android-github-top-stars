package com.jss.core.repository

class RepoRepository(private val dataSource: RepositoryDataSource) {
    suspend fun getAll() = dataSource.getAll()
}