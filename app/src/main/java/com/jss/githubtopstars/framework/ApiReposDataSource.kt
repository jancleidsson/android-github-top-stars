package com.jss.githubtopstars.framework

import android.app.Application
import android.content.Context
import com.jss.core.data.Owner
import com.jss.core.data.Repository
import com.jss.core.repository.RepositoryDataSource
import kotlinx.coroutines.delay

class ApiReposDataSource(context: Context) : RepositoryDataSource {
    override suspend fun getAll(): List<Repository> {
        val repoList = arrayListOf<Repository>()
        delay(4000)
        repeat(100) { index ->
            repoList.add(
                Repository(
                    "Repo $index",
                    index,
                    index,
                    Owner(
                        "https://avatars.githubusercontent.com/u/1627211?s=400&v=4",
                        "name $index"
                    )
                )
            )
        }
        return repoList
    }
}