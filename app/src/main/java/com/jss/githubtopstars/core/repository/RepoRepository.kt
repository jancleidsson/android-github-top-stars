package com.jss.githubtopstars.core.repository

class RepoRepository (private val dataSource: RepoDataSource) {
    fun getAll() = dataSource.getAll()
}