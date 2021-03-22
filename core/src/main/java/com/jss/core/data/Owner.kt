package com.jss.core.data

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("avatar_url")
        var photo: String? = null,
        @SerializedName("login")
        var name: String? = null
)