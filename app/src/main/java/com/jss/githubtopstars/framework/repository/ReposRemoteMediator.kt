package com.jss.githubtopstars.framework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.core.data.Repo
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ReposRemoteMediator(
    private val service: GithubService,
    private val database: DatabaseService
) : RemoteMediator<Int, Repo>() {

    override suspend fun load( loadType: LoadType,  state: PagingState<Int, Repo> ):MediatorResult {
        return try {
            val response = service.getRepositories()
            val reposList = response.body()?.items.orEmpty()
            database.repositoryDao().addRepos(reposList)
            val endOfPagination = reposList.isEmpty()
            MediatorResult.Success(
                endOfPaginationReached = endOfPagination
            )
        } catch (exception: IOException){
            MediatorResult.Error(exception)
        } catch (httpException: HttpException) {
            MediatorResult.Error(httpException)
        }
    }
}