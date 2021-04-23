package com.jss.githubtopstars.framework.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.jss.githubtopstars.R
import com.jss.githubtopstars.core.data.PageIndex
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.api.ReposResponseApi
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.framework.db.PageIndexDao
import com.jss.githubtopstars.framework.db.RepoDao
import com.jss.githubtopstars.utils.Constants
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class ReposRemoteMediatorTest {
    @MockK
    private lateinit var databaseService: DatabaseService

    @Mock
    private val githubService = mock(GithubService::class.java)

    @Mock
    private val pageIndexDao = mock(PageIndexDao::class.java)

    @Mock
    private val repoDao = mock(RepoDao::class.java)

    @Mock
    private val responseApi = mock(ReposResponseApi::class.java)

    @Mock
    private val repo = mock(Repo::class.java)

    private val repoList = listOf(repo)
    private val emptyRepoList = listOf<Repo>()
    private val pagingConfig = PagingConfig(pageSize = Constants.GITHUB_PAGE_SIZE, enablePlaceholders = false)
    private var pageList = listOf(PagingSource.LoadResult.Page(data = repoList, prevKey = 1, nextKey = 3))
    private var currentPagingState = PagingState(pageList, anchorPosition = 2, pagingConfig, leadingPlaceholderCount = 1)

    private lateinit var reposRemoteMediator: ReposRemoteMediator

    @Before
    fun initMocks() {
        //Mock .withTransaction static suspend fun call
        MockKAnnotations.init(this)
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> R>()
        coEvery { databaseService.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        reposRemoteMediator = ReposRemoteMediator(database = databaseService, service = githubService)

        every { databaseService.repositoryDao() } returns repoDao
        every { databaseService.pageIndexDao() } returns pageIndexDao

        `when`(repo.id).thenReturn(1)
        runBlockingTest {
            `when`(pageIndexDao.pageIndexByRepoId(1)).thenReturn(PageIndex(1, 1, 3))
        }
    }

    @Test
    fun initialize_withInitialRefresh() = runBlockingTest {
        assertThat(reposRemoteMediator.initialize(), `is`(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH))
    }

    @Test
    fun load_typeRefreshNoPageState_returnSuccessNoMorePages() = runBlockingTest {
        `when`(githubService.getRepositories(page = Constants.GITHUB_STARTING_PAGE_INDEX, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(emptyRepoList)

        preparePageStateWithNoRepo()
        val value = reposRemoteMediator.load(LoadType.REFRESH, currentPagingState)

        verify(pageIndexDao, times(0)).deleteAllPageIndexes()
        verify(repoDao, times(0)).deleteAllRepos()
        verify(pageIndexDao, times(0)).addPageIndexes(anyList())
        verify(repoDao, times(0)).addRepos(anyList())
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(true))
    }

    @Test
    fun load_typeRefreshPageState_returnSuccessMorePages() = runBlockingTest {
        //Verifying it is requesting current page
        `when`(githubService.getRepositories(page = 2, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(repoList)

        val value = reposRemoteMediator.load(LoadType.REFRESH, currentPagingState)

        verify(pageIndexDao, times(1)).deleteAllPageIndexes()
        verify(repoDao, times(1)).deleteAllRepos()
        verify(pageIndexDao, times(1)).addPageIndexes(anyList())
        verify(repoDao, times(1)).addRepos(repoList)
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(false))
    }

    @Test
    fun load_typePrepend_withNoPageIndexNextKey_returnSuccessMorePages() = runBlockingTest {
        //Verifying it is requesting previous page
        `when`(githubService.getRepositories(page = 1, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(emptyRepoList)

        preparePageStateWithNoRepo()
        val value = reposRemoteMediator.load(LoadType.PREPEND, currentPagingState)

        verify(pageIndexDao, times(0)).deleteAllPageIndexes()
        verify(repoDao, times(0)).deleteAllRepos()
        verify(pageIndexDao, times(0)).addPageIndexes(anyList())
        verify(repoDao, times(0)).addRepos(anyList())
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(false))
    }

    @Test
    fun load_typePrepend_withPageIndexNextKey_returnSuccessMorePages() = runBlockingTest {
        //Verifying it is requesting previous page
        `when`(githubService.getRepositories(page = 1, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(repoList)

        val value = reposRemoteMediator.load(LoadType.PREPEND, currentPagingState)

        verify(pageIndexDao, times(0)).deleteAllPageIndexes()
        verify(repoDao, times(0)).deleteAllRepos()
        verify(pageIndexDao, times(1)).addPageIndexes(anyList())
        verify(repoDao, times(1)).addRepos(repoList)
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(false))
    }

    @Test
    fun load_typeAppend_withNoPageIndexPreviousKey_returnSuccessMorePages() = runBlockingTest {
        //Verifying it is requesting next page
        preparePageStateWithNoRepo()
        `when`(githubService.getRepositories(page = 3, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)

        val value = reposRemoteMediator.load(LoadType.APPEND, currentPagingState)

        verify(pageIndexDao, times(0)).deleteAllPageIndexes()
        verify(repoDao, times(0)).deleteAllRepos()
        verify(pageIndexDao, times(0)).addPageIndexes(anyList())
        verify(repoDao, times(0)).addRepos(anyList())
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(false))
    }

    @Test
    fun load_typeAppend_withPageIndexPreviousKey_returnSuccessMorePages() = runBlockingTest {
        //Verifying it is requesting next page
        `when`(githubService.getRepositories(page = 3, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(repoList)

        val value = reposRemoteMediator.load(LoadType.APPEND, currentPagingState)

        verify(pageIndexDao, times(0)).deleteAllPageIndexes()
        verify(repoDao, times(0)).deleteAllRepos()
        verify(pageIndexDao, times(1)).addPageIndexes(anyList())
        verify(repoDao, times(1)).addRepos(repoList)
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Success).endOfPaginationReached, `is`(false))
    }

    @Test
    fun load_returnErrorThrowsIOException() = runBlockingTest {
        //Verifying it is requesting next page
        `when`(githubService.getRepositories(page = 3, itemsPerPage = pagingConfig.pageSize)).thenReturn(responseApi)
        `when`(responseApi.items).thenReturn(repoList)
        doAnswer { throw IOException() }.`when`(repoDao).addRepos(repoList)

        val value = reposRemoteMediator.load(LoadType.APPEND, currentPagingState)
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Error).throwable, instanceOf(IOException::class.java))
    }

    @Test
    fun load_returnErrorThrowsHttpException() = runBlockingTest {
        doThrow(HttpException::class.java).`when`(githubService).getRepositories(page = 3, itemsPerPage = pagingConfig.pageSize)

        val value = reposRemoteMediator.load(LoadType.APPEND, currentPagingState)
        assertThat(value, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
        assertThat((value as RemoteMediator.MediatorResult.Error).throwable, instanceOf(HttpException::class.java))
    }

    private fun preparePageStateWithNoRepo() {
        pageList = listOf(PagingSource.LoadResult.Page(data = emptyRepoList, prevKey = 1, nextKey = 3))
        currentPagingState = PagingState(pageList, anchorPosition = 2, pagingConfig, leadingPlaceholderCount = 1)
    }
}