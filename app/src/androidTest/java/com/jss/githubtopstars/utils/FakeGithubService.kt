package com.jss.githubtopstars.utils

import com.jss.githubtopstars.core.data.Owner
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.api.GithubService
import com.jss.githubtopstars.framework.api.ReposResponseApi
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeGithubService : GithubService {

    private var mResponseDelay = 0L
    private var mHttpExceptionOnPage = 0
    private var mResponsePages = 1

    @Throws(HttpException::class)
    override suspend fun getRepositories(
            sort: String?,
            page: Int?,
            itemsPerPage: Int?,
    ): ReposResponseApi {
        val repoList = generateRepoListFromRequest(page!!, itemsPerPage!!)
        delay(mResponseDelay)
        return ReposResponseApi(repoList.size, false, repoList)
    }

    fun configServiceResponse(delay: Long = 0, httpExceptionOnPage: Int = 0, responsePages: Int = 1) {
        mResponseDelay = delay
        mHttpExceptionOnPage = httpExceptionOnPage
        mResponsePages = responsePages
    }

    @Throws(HttpException::class)
    private fun generateRepoListFromRequest(page: Int, itemsPerPage: Int): List<Repo> {

        if (mHttpExceptionOnPage == page) {
            throw HttpException(Response.error<Any>(403, "".toResponseBody("plain/text".toMediaTypeOrNull())))
        }

        val repoList = ArrayList<Repo>()
        if (page <= mResponsePages) {
            for (index in 1..itemsPerPage) {
                val currentIndex = index + ((page - 1) * itemsPerPage)
                val reversedCount = Int.MAX_VALUE - currentIndex
                repoList.add(Repo(currentIndex.toLong(), "jss $currentIndex", reversedCount, reversedCount, Owner("jss $currentIndex", "")))
            }
        } else {
            IdlingResourcesHelper.decrementCounting()
        }
        return repoList
    }
}