package com.jss.githubtopstars.core.repository

class RepoRepository(private val dataSource: RepositoryDataSource) {
    fun getAll() = dataSource.getAll()
}