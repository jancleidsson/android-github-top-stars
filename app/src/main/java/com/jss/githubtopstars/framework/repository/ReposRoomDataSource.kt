package com.jss.githubtopstars.framework.repository

import android.content.Context
import com.jss.core.data.Repository
import com.jss.core.repository.RepositoryDataSource
import com.jss.githubtopstars.framework.db.DatabaseService

class ReposRoomDataSource(context: Context): RepositoryDataSource {
    private val repositoryDao = DatabaseService.getInstance(context).repositoryDao()
    override suspend fun getAll(): List<Repository> {
        TODO("Not yet implemented")
    }

    override suspend fun addAll(reposList: List<Repository>) {
        TODO("Not yet implemented")
    }
}