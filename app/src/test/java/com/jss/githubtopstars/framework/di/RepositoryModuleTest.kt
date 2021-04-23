package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepoRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.any
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock

class RepositoryModuleTest {

    @ExperimentalPagingApi
    @Test
    fun provideRepository() {
        val pager = mock(Pager::class.java) as Pager<Int, Repo>
        assertThat(RepositoryModule().provideRepository(pager), `is`(any(RepoRepository::class.java)))
    }
}