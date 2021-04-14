package com.jss.githubtopstars.framework.vm

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.core.usecase.GetAllRepos
import com.jss.githubtopstars.framework.UseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class RepoListViewModelTest {

    @Mock
    private val useCases = Mockito.mock(UseCases::class.java)

    @Mock
    private val getAllRepos = Mockito.mock(GetAllRepos::class.java)

    @Mock
    private val repo = Mockito.mock(Repo::class.java)

    private lateinit var repoList: List<Repo>
    private lateinit var  repoListPageData: PagingData<Repo>

    @Before
    fun setup() {
        repoList = listOf(repo)
        repoListPageData = PagingData.from(repoList)
    }

    @Test
    fun getReposList_returnAllRepos() = runBlockingTest {
        val repoListFlow = flow{
            delay(10)
            emit(repoListPageData)
        }
        `when`(getAllRepos.invoke()).thenReturn(repoListFlow)
        `when`(useCases.getAllRepos).thenReturn(getAllRepos)

        val repoListViewModel = RepoListViewModel(useCases)
        assertThat(repoListViewModel.getReposList().single(), `is`(repoListPageData))
    }
}