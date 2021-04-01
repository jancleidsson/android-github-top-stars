package com.jss.githubtopstars.framework.api

import com.google.gson.annotations.SerializedName
import com.jss.githubtopstars.core.data.Repo

data class ReposResponseApi(
        @SerializedName("total_count")
        var totalCount: Int,
        @SerializedName("incomplete_results")
        var incompleteResults: Boolean,
        @SerializedName("items")
        var items: List<Repo>
)