package com.jss.githubtopstars.framework.vm

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.usecase.GetAllRepos
import com.jss.githubtopstars.framework.UseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class RepoListViewModelTest {
    @Test
    fun getReposList_returnAllRepos() = runBlockingTest {
        val getAllReposMock = Mockito.mock(GetAllRepos::class.java)
        val repoPageDataMock =  Mockito.mock(PagingData::class.java) as PagingData<Repo>
        `when`(getAllReposMock.invoke()).thenReturn(flowOf(repoPageDataMock))
        val useCases = UseCases(getAllReposMock)

        val repoListViewModel = RepoListViewModel(useCases)
        assertThat(repoListViewModel.getReposList().single(), `is`(repoPageDataMock))
    }
}