package com.jss.core.data

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("login")
        var login: String = "",
        @SerializedName("avatar_url")
        var photo: String = ""
)