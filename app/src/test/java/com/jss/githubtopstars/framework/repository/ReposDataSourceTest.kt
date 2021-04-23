package com.jss.githubtopstars.framework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.jss.githubtopstars.core.data.Repo
import kotlinx.coroutines.flow.Flow
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalPagingApi
class ReposDataSourceTest {

    @Mock
    private val repoPager = Mockito.mock(Pager::class.java) as Pager<Int, Repo>

    @Mock
    private val repoListPageDataFlow = Mockito.mock(Flow::class.java) as Flow<PagingData<Repo>>

    @Test
    fun getAll_returnAllRepos() {
        `when`(repoPager.flow).thenReturn(repoListPageDataFlow)
        val repoDataSource = ReposDataSource(repoPager)
        assertThat(repoDataSource.getAll(), `is` (repoListPageDataFlow))
    }
}