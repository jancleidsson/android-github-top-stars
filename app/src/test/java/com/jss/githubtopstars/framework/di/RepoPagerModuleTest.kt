package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.any
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock

class RepoPagerModuleTest {

    @ExperimentalPagingApi
    @Test
    fun provideRepoPager_returnRepoPager() {
        val githubService = mock(GithubService::class.java)
        val databaseService = mock(DatabaseService::class.java)
        assertThat(RepoPagerModule().provideRepoPager(githubService, databaseService), `is`(any(Pager::class.java)))
    }
}