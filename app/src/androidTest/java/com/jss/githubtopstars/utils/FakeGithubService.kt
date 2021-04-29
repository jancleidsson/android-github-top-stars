package com.jss.githubtopstars.utils

import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.api.ReposResponseApi
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.jvm.Throws

class FakeGithubService : GithubService {

    private var responseApi = ReposResponseApi(0, false, listOf())
    private var responseDelay = 0L
    private var httpException = false

    @Throws(HttpException::class)
    override suspend fun getRepositories(
            sort: String?,
            page: Int?,
            itemsPerPage: Int?,
    ): ReposResponseApi {
        delay(responseDelay)
        if (httpException) { throw HttpException(Response.error<Any>(403, "".toResponseBody("plain/text".toMediaTypeOrNull()))) }
        return responseApi
    }

    fun buildServiceResponse(repoList: List<Repo> = listOf(), delay: Long = 0L, throwsHTTPException: Boolean = false) {
        responseApi.totalCount = repoList.size
        responseApi.items = repoList
        httpException = throwsHTTPException
        responseDelay = delay
    }
}