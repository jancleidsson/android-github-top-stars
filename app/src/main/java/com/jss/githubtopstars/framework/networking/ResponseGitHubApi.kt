package com.jss.githubtopstars.framework.networking

import com.google.gson.annotations.SerializedName
import com.jss.core.data.Repository

data class ResponseGitHubApi(
        @SerializedName("total_count")
        var totalCount:Int = 0,
        @SerializedName("incomplete_results")
        var incompleteResults: Boolean = false,
        @SerializedName("items")
        var items: List<Repository>? = null
)