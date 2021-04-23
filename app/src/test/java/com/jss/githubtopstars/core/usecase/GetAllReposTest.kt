package com.jss.githubtopstars.core.usecase

import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.repository.RepoDataSource
import com.jss.githubtopstars.core.repository.RepoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class GetAllReposTest {

    @Mock
    private val repoDataSource = Mockito.mock(RepoDataSource::class.java)

    @Mock
    private val repoListPagingData = Mockito.mock(PagingData::class.java) as PagingData<Repo>

    @Test
    fun invoke_returnAllRepo() = runBlockingTest {
        Mockito.`when`(repoDataSource.getAll()).thenReturn(flowOf(repoListPagingData))
        val fakeRepository = RepoRepository(repoDataSource)
        val getAllRepos =  GetAllRepos(fakeRepository)
        assertThat(getAllRepos.invoke().single(), `is`(repoListPagingData))
    }
}