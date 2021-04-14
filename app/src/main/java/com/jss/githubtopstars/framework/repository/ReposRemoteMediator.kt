package com.jss.githubtopstars.framework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jss.githubtopstars.core.data.PageIndex
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.utils.Constants
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ReposRemoteMediator(
        private val service: GithubService,
        private val database: DatabaseService,
) : RemoteMediator<Int, Repo>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {

        val currentPageIndex = when (loadType) {
            LoadType.REFRESH -> {
                getPageIndexClosestToCurrentPosition(state)?.nextPageIndex?.minus(1)
                        ?: Constants.GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                getPageIndexForFirstItem(state)?.prevPageIndex
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.APPEND -> {
                getPageIndexForLastItem(state)?.nextPageIndex
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
            }
        }

        try {
            val response = service.getRepositories(page = currentPageIndex, itemsPerPage = state.config.pageSize)
            val reposList = response.items
            val endOfPagination = reposList.isEmpty()

            database.withTransaction {
                //Clearing cache after refresh
                if (loadType == LoadType.REFRESH) {
                    database.repositoryDao().deleteAllRepos()
                    database.pageIndexDao().deleteAllPageIndexes()
                }
                val pageIndexesList = generatePageIndexesFromReposLists(reposList, currentPageIndex, endOfPagination)
                database.pageIndexDao().addPageIndexes(pageIndexesList)
                database.repositoryDao().addRepos(reposList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (httpException: HttpException) {
            return MediatorResult.Error(httpException)
        }
    }

    private suspend fun getPageIndexClosestToCurrentPosition(
            state: PagingState<Int, Repo>,
    ): PageIndex? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.pageIndexDao().pageIndexByRepoId(repoId)
            }
        }
    }

    private suspend fun getPageIndexForFirstItem(state: PagingState<Int, Repo>): PageIndex? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { repo ->
                    database.pageIndexDao().pageIndexByRepoId(repo.id)
                }
    }

    private suspend fun getPageIndexForLastItem(state: PagingState<Int, Repo>): PageIndex? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { repo ->
                    database.pageIndexDao().pageIndexByRepoId(repo.id)
                }
    }

    private fun generatePageIndexesFromReposLists(reposList: List<Repo>, currentPageIndex: Int, endOfPagination: Boolean): List<PageIndex> {
        val prevKey = if (currentPageIndex == Constants.GITHUB_STARTING_PAGE_INDEX) null else currentPageIndex.minus(1)
        val nextKey = if (endOfPagination) null else currentPageIndex.plus(1)
        return reposList.map { repo ->
            PageIndex(repo.id, prevKey, nextKey)
        }
    }
}