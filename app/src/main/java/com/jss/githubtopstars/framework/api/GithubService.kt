package com.jss.githubtopstars.framework.api

import com.jss.githubtopstars.utils.Constants
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/repositories?q=language:kotlin")
    suspend fun getRepositories(
            @Query("sort") sort: String? = "stars",
            @Query("page") page: Int? = Constants.GITHUB_STARTING_PAGE_INDEX,
            @Query("per_page") itemsPerPage: Int? = Constants.GITHUB_PAGE_SIZE
    ): ReposResponseApi

    companion object {
        fun create(): GithubService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(Constants.GITHUB_BASE_URL.toHttpUrlOrNull()!!)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubService::class.java)
        }
    }
}