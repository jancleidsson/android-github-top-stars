package com.jss.githubtopstars.utils

import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.api.ReposResponseApi
import kotlinx.coroutines.delay

class FakeGithubService : GithubService {

    private var responseApi = ReposResponseApi(0, false, listOf())
    private var responseDelay = 0L

    override suspend fun getRepositories(
            sort: String?,
            page: Int?,
            itemsPerPage: Int?
    ): ReposResponseApi {
        delay(responseDelay)
        return responseApi
    }

    fun buildServiceResponse(repoList: List<Repo>, responseDelay: Long = 0L) {
        responseApi.totalCount = repoList.size
        responseApi.items = repoList
        this.responseDelay = responseDelay
    }
}