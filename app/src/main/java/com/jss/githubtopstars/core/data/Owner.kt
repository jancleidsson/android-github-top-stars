package com.jss.githubtopstars.core.data

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("login")
        var login: String,
        @SerializedName("avatar_url")
        var photo: String
)