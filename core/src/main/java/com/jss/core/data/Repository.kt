package com.jss.core.data

import com.google.gson.annotations.SerializedName

data class Repository(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("stargazers_count")
        var stars: Int = 0,
        @SerializedName("forks_count")
        var forks: Int = 0,
        @SerializedName("owner")
        var owner: Owner? = null,
        val repositoryId: Long = 0
)
