package com.jss.core.repository

import com.jss.core.data.Repository

interface RepositoryDataSource {
    suspend fun getAll(): List<Repository>
    suspend fun addAll(reposList: List<Repository>)
}