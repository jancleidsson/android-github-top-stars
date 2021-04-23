package com.jss.githubtopstars.core.repository

import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.utils.FakeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class RepoRepositoryTest {
    @Test
    fun getAll_returnsReposPageDataFlow() = runBlockingTest {
        val repo = Mockito.mock(Repo::class.java)
        val fakePagingData = PagingData.from(listOf(repo))
        val fakeRepoRepository = FakeDataSource(fakePagingData)
        val repoRepository =  RepoRepository(fakeRepoRepository)
        assertThat(repoRepository.getAll().single(), `is`(fakePagingData))
    }
}