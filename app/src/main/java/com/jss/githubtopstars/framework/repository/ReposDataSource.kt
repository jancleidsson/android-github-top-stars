package com.jss.githubtopstars.framework.repository

import com.jss.core.data.Repository
import com.jss.core.repository.RepositoryDataSource

class ReposDataSource: RepositoryDataSource {
    override suspend fun getAll(): List<Repository> {
        TODO("Not yet implemented")
    }

    override suspend fun addAll(reposList: List<Repository>) {
        TODO("Not yet implemented")
    }
}