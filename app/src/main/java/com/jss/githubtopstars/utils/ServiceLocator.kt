package com.jss.githubtopstars.utils

import android.app.Application
import androidx.annotation.VisibleForTesting

import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService

object ServiceLocator {

    @Volatile
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var githubService: GithubService? = null

    @Volatile
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var databaseService: DatabaseService? = null

    fun provideGithubService(): GithubService {
        synchronized(this) {
            return githubService ?: createGitHubService()
        }
    }

    fun provideDatabaseService(app: Application): DatabaseService {
        synchronized(this) {
            return databaseService ?: getDatabaseServiceInstance(app)
        }
    }

    private fun createGitHubService(): GithubService {
        return GithubService.create()
    }

    private fun getDatabaseServiceInstance(app: Application): DatabaseService {
        return DatabaseService.getInstance(app)
    }
}

